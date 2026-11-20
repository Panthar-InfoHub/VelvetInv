package org.sharad.velvetinvestment.features.webview

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitView
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCSignatureOverride
import org.sharad.velvetinvestment.features.webview.WebViewState
import org.sharad.velvetinvestment.utils.deeplink.ExternalAppUrl
import platform.CoreGraphics.CGRectMake
import platform.Foundation.NSError
import platform.Foundation.NSHTTPURLResponse
import platform.Foundation.NSURL
import platform.Foundation.NSURLRequest
import platform.UIKit.UIDevice
import platform.WebKit.WKAudiovisualMediaTypeNone
import platform.WebKit.WKFrameInfo
import platform.WebKit.WKNavigation
import platform.WebKit.WKNavigationAction
import platform.WebKit.WKNavigationActionPolicy
import platform.WebKit.WKNavigationDelegateProtocol
import platform.WebKit.WKNavigationResponse
import platform.WebKit.WKNavigationResponsePolicy
import platform.WebKit.WKScriptMessage
import platform.WebKit.WKScriptMessageHandlerProtocol
import platform.WebKit.WKUIDelegateProtocol
import platform.WebKit.WKUserContentController
import platform.WebKit.WKUserScript
import platform.WebKit.WKUserScriptInjectionTime
import platform.WebKit.WKWebView
import platform.WebKit.WKWebViewConfiguration
import platform.WebKit.WKWindowFeatures
import platform.darwin.NSObject

// Debug logging for the web view. println goes to the Xcode console; filter on the tag.
// Flip to false to silence it.
private const val WEB_LOG_ENABLED = true
private const val WEB_LOG_TAG = "[VelvetWebView]"
private const val JS_LOG_HANDLER = "velvetLog"

private fun webLog(message: String) {
    if (WEB_LOG_ENABLED) println("$WEB_LOG_TAG $message")
}

// Forwards the page's console output and uncaught errors to the native log, so what the site
// itself reports (e.g. why it decided the browser is unsupported) shows up in Xcode.
private val CONSOLE_BRIDGE_JS = """
    (function() {
      var h = window.webkit && window.webkit.messageHandlers && window.webkit.messageHandlers.$JS_LOG_HANDLER;
      if (!h) return;
      function fmt(a) {
        try { return Array.prototype.map.call(a, function(x) {
          if (x instanceof Error) return x.name + ': ' + x.message + '\n' + x.stack;
          return typeof x === 'object' ? JSON.stringify(x) : String(x);
        }).join(' '); } catch (e) { return String(a); }
      }
      ['log','info','warn','error','debug'].forEach(function(level) {
        var orig = console[level];
        console[level] = function() {
          try { h.postMessage('console.' + level + ': ' + fmt(arguments)); } catch (e) {}
          if (orig) orig.apply(console, arguments);
        };
      });
      window.addEventListener('error', function(e) {
        h.postMessage('window.onerror: ' + e.message + ' @ ' + e.filename + ':' + e.lineno + ':' + e.colno);
      });
      window.addEventListener('unhandledrejection', function(e) {
        h.postMessage('unhandledrejection: ' + (e.reason && (e.reason.stack || e.reason.message) || e.reason));
      });
      h.postMessage('bridge ready on ' + location.href + ' | UA: ' + navigator.userAgent);
    })();
""".trimIndent()

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun PlatformWebView(
    state: WebViewState,
    modifier: Modifier,
    onUrlChanged: (String) -> Unit,
    onExternalAppUrl: ((String) -> Unit)?
) {
    val currentOnUrlChanged by rememberUpdatedState(onUrlChanged)
    val currentOnExternalAppUrl by rememberUpdatedState(onExternalAppUrl)

    // WKWebView holds its delegates weakly, so this one must live as long as the web view: keyed
    // on the state alone, with the callbacks read through, rather than rebuilt per new lambda.
    val delegate = remember(state) {
        WebViewNavigationDelegate(
            state = state,
            onUrlChanged = { currentOnUrlChanged(it) },
            onExternalAppUrl = { url -> currentOnExternalAppUrl?.invoke(url) }
        )
    }

    UIKitView(
        factory = {
            val configuration = WKWebViewConfiguration()
            // WKWebView's default UA ends at "Mobile/15E148" with no "Version/x Safari/x" token,
            // so sites that sniff for Safari (e.g. HyperVerge video KYC) reject it outright.
            // Append the token real Mobile Safari sends, versioned to the running OS.
            val osVersion = UIDevice.currentDevice.systemVersion.split(".").take(2).joinToString(".")
            configuration.applicationNameForUserAgent = "Version/$osVersion Mobile/15E148 Safari/604.1"
            // Video KYC streams the camera into a <video>; without these it is forced fullscreen
            // or never starts playing.
            configuration.allowsInlineMediaPlayback = true
            configuration.mediaTypesRequiringUserActionForPlayback = WKAudiovisualMediaTypeNone
            if (WEB_LOG_ENABLED) {
                configuration.userContentController.addUserScript(
                    WKUserScript(
                        source = CONSOLE_BRIDGE_JS,
                        injectionTime = WKUserScriptInjectionTime.WKUserScriptInjectionTimeAtDocumentStart,
                        forMainFrameOnly = false
                    )
                )
                configuration.userContentController.addScriptMessageHandler(delegate, JS_LOG_HANDLER)
            }

            val webView = WKWebView(frame = CGRectMake(0.0, 0.0, 0.0, 0.0), configuration = configuration)
            webView.navigationDelegate = delegate
            webView.UIDelegate = delegate
            webLog("created; customUserAgent=${webView.customUserAgent}, applicationNameForUserAgent=${configuration.applicationNameForUserAgent}")
            webView.evaluateJavaScript("navigator.userAgent") { result, error ->
                webLog("navigator.userAgent = ${result ?: error?.localizedDescription}")
            }

            webLog("initial load: ${state.currentUrl}")
            val nsUrl = NSURL.URLWithString(state.currentUrl)
            if (nsUrl != null) {
                webView.loadRequest(NSURLRequest.requestWithURL(nsUrl))
            } else {
                webLog("initial URL could not be parsed by NSURL, nothing loaded")
            }
            webView
        },
        modifier = modifier
    )
}

private class WebViewNavigationDelegate(
    private val state: WebViewState,
    private val onUrlChanged: (String) -> Unit,
    /** Gets each app link the web view refused to load, to open it in its app if it wants to. */
    private val onExternalAppUrl: (String) -> Unit
) : NSObject(), WKNavigationDelegateProtocol, WKUIDelegateProtocol, WKScriptMessageHandlerProtocol {

    // The URL a flow ends on is often posted to a host the app owns but nothing serves, so the
    // navigation never commits and no load callback ever carries it. The policy decision is the
    // one point it is always seen, so the URL is reported from there and the load still allowed.
    override fun webView(
        webView: WKWebView,
        decidePolicyForNavigationAction: WKNavigationAction,
        decisionHandler: (WKNavigationActionPolicy) -> Unit
    ) {
        val action = decidePolicyForNavigationAction
        val url = action.request.URL?.absoluteString
        webLog(
            "decidePolicy(action): $url " +
                "type=${action.navigationType} mainFrame=${action.targetFrame?.mainFrame} " +
                "source=${action.sourceFrame.request.URL?.absoluteString}"
        )

        // An app link (gpay://, phonepe:// …) is always cancelled here — tapped or redirected to
        // by the page itself — before WebKit tries it and fails with an unsupported-URL error,
        // and handed off to be opened in the app itself.
        if (handOffExternalAppUrl(url)) {
            webLog("  -> CANCEL (external app link handed off)")
            decisionHandler(WKNavigationActionPolicy.WKNavigationActionPolicyCancel)
            return
        }

        url?.let(onUrlChanged)
        decisionHandler(WKNavigationActionPolicy.WKNavigationActionPolicyAllow)
    }

    override fun webView(
        webView: WKWebView,
        decidePolicyForNavigationResponse: WKNavigationResponse,
        decisionHandler: (WKNavigationResponsePolicy) -> Unit
    ) {
        val response = decidePolicyForNavigationResponse.response
        val status = (response as? NSHTTPURLResponse)?.statusCode
        webLog(
            "response: ${response.URL?.absoluteString} status=$status mime=${response.MIMEType} " +
                "mainFrame=${decidePolicyForNavigationResponse.forMainFrame} " +
                "canShow=${decidePolicyForNavigationResponse.canShowMIMEType}"
        )
        decisionHandler(WKNavigationResponsePolicy.WKNavigationResponsePolicyAllow)
    }

    @ObjCSignatureOverride
    override fun webView(webView: WKWebView, didStartProvisionalNavigation: WKNavigation?) {
        webLog("didStartProvisionalNavigation: ${webView.URL?.absoluteString}")
        state.isLoading = true
        updateUrl(webView)
    }

    @ObjCSignatureOverride
    override fun webView(webView: WKWebView, didReceiveServerRedirectForProvisionalNavigation: WKNavigation?) {
        webLog("server redirect -> ${webView.URL?.absoluteString}")
    }

    @ObjCSignatureOverride
    override fun webView(webView: WKWebView, didCommitNavigation: WKNavigation?) {
        webLog("didCommitNavigation: ${webView.URL?.absoluteString}")
    }

    @ObjCSignatureOverride
    override fun webView(webView: WKWebView, didFinishNavigation: WKNavigation?) {
        webLog("didFinishNavigation: ${webView.URL?.absoluteString} title=\"${webView.title}\"")
        state.isLoading = false
        state.canGoBack = webView.canGoBack
        updateUrl(webView)
        // Dump the visible text so a page-level message like "only supported in Safari" is logged.
        webView.evaluateJavaScript(
            "document.body ? document.body.innerText.substring(0, 1000) : ''"
        ) { result, _ ->
            webLog("page text (first 1000 chars):\n$result")
        }
    }

    @ObjCSignatureOverride
    override fun webView(
        webView: WKWebView,
        didFailProvisionalNavigation: WKNavigation?,
        withError: NSError
    ) {
        webLog("didFailProvisionalNavigation: ${withError.logString()}")
        state.isLoading = false
    }

    @ObjCSignatureOverride
    override fun webView(webView: WKWebView, didFailNavigation: WKNavigation?, withError: NSError) {
        webLog("didFailNavigation: ${withError.logString()}")
        state.isLoading = false
    }

    override fun webViewWebContentProcessDidTerminate(webView: WKWebView) {
        webLog("web content process TERMINATED (crash / memory) at ${webView.URL?.absoluteString}")
    }

    // Payment/e-sign checkout widgets often render their modal via
    // window.open() rather than an in-page element. WKWebView never opens
    // a real new window on its own; without this delegate the call is a
    // silent no-op. Loading the request in the same webview instead is
    // the standard fix for keeping everything inside one embedded view.
    @ObjCSignatureOverride
    override fun webView(
        webView: WKWebView,
        createWebViewWithConfiguration: WKWebViewConfiguration,
        forNavigationAction: WKNavigationAction,
        windowFeatures: WKWindowFeatures
    ): WKWebView? {
        webLog(
            "window.open: ${forNavigationAction.request.URL?.absoluteString} " +
                "targetFrame=${forNavigationAction.targetFrame}"
        )
        if (forNavigationAction.targetFrame == null &&
            !handOffExternalAppUrl(forNavigationAction.request.URL?.absoluteString)
        ) {
            webView.loadRequest(forNavigationAction.request)
        }
        return null
    }

    // Not shown to the user (same as before, WKWebView drops alerts without this); only logged.
    override fun webView(
        webView: WKWebView,
        runJavaScriptAlertPanelWithMessage: String,
        initiatedByFrame: WKFrameInfo,
        completionHandler: () -> Unit
    ) {
        webLog("JS alert(): $runJavaScriptAlertPanelWithMessage")
        completionHandler()
    }

    override fun userContentController(
        userContentController: WKUserContentController,
        didReceiveScriptMessage: WKScriptMessage
    ) {
        webLog("JS ${didReceiveScriptMessage.body}")
    }

    /** True for any app link, which must not load; the listener, if any, gets to open it. */
    private fun handOffExternalAppUrl(url: String?): Boolean {
        if (url == null || !ExternalAppUrl.isExternalAppUrl(url)) return false
        onExternalAppUrl(url)
        return true
    }

    private fun updateUrl(webView: WKWebView) {
        val url = webView.URL?.absoluteString ?: return
        if (ExternalAppUrl.isExternalAppUrl(url)) return
        state.currentUrl = url
        onUrlChanged(url)
    }
}

private fun NSError.logString(): String =
    "domain=$domain code=$code desc=$localizedDescription " +
        "url=${userInfo["NSErrorFailingURLStringKey"]}"
