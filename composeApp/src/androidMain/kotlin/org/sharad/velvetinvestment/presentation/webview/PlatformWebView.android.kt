package org.sharad.velvetinvestment.presentation.webview

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Message
import android.util.Log
import android.view.ViewGroup
import android.webkit.ConsoleMessage
import android.webkit.CookieManager
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView

@SuppressLint("SetJavaScriptEnabled")
@Composable
actual fun PlatformWebView(
    state: WebViewState,
    modifier: Modifier,
    onUrlChanged: (String) -> Unit
) {
    val context = LocalContext.current

    val webView = remember {
        WebView.setWebContentsDebuggingEnabled(true)

        WebView(context).apply {
            setLayerType(WebView.LAYER_TYPE_HARDWARE, null)

            settings.apply {
                javaScriptEnabled = true
                domStorageEnabled = true

                javaScriptCanOpenWindowsAutomatically = true
                setSupportMultipleWindows(true)

                allowFileAccess = true
                allowContentAccess = true

                useWideViewPort = true
                loadWithOverviewMode = true
                loadsImagesAutomatically = true

                mediaPlaybackRequiresUserGesture = false

                mixedContentMode = WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE
            }

            CookieManager.getInstance().setAcceptCookie(true)
            CookieManager.getInstance().setAcceptThirdPartyCookies(this, true)

            webViewClient = object : WebViewClient() {

                override fun onPageStarted(
                    view: WebView?,
                    url: String?,
                    favicon: Bitmap?
                ) {
                    super.onPageStarted(view, url, favicon)

                    Log.d("WEB", "Started : $url")

                    state.isLoading = true
                    state.canGoBack = view?.canGoBack() ?: false

                    url?.let {
                        state.currentUrl = it
                        onUrlChanged(it)
                    }
                }

                override fun onPageFinished(
                    view: WebView?,
                    url: String?
                ) {
                    super.onPageFinished(view, url)

                    Log.d("WEB", "Finished : $url")

                    state.isLoading = false
                    state.canGoBack = view?.canGoBack() ?: false

                    url?.let {
                        state.currentUrl = it
                        onUrlChanged(it)
                    }
                }

                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?
                ): Boolean {
                    Log.d("WEB", "Loading : ${request?.url}")
                    return false
                }
            }

            webChromeClient = object : WebChromeClient() {

                override fun onConsoleMessage(consoleMessage: ConsoleMessage): Boolean {
                    Log.d(
                        "ANDWEB-CONSOLE",
                        "${consoleMessage.message()} @ ${consoleMessage.sourceId()}:${consoleMessage.lineNumber()}"
                    )
                    return true
                }

                override fun onCreateWindow(
                    view: WebView?,
                    isDialog: Boolean,
                    isUserGesture: Boolean,
                    resultMsg: Message?
                ): Boolean {

                    Log.d("ANDWEB", "onCreateWindow()")

                    val transport =
                        resultMsg?.obj as? WebView.WebViewTransport ?: return false

                    val popup = WebView(context).apply {
                        settings.javaScriptEnabled = true
                        settings.domStorageEnabled = true
                        settings.javaScriptCanOpenWindowsAutomatically = true
                        settings.setSupportMultipleWindows(true)
                    }

                    transport.webView = popup
                    resultMsg.sendToTarget()

                    return true
                }
            }

            loadUrl(state.currentUrl)
        }
    }

    DisposableEffect(webView) {
        onDispose {
            webView.stopLoading()
            webView.destroy()
        }
    }

    LaunchedEffect(state.currentUrl) {
        if (webView.url != state.currentUrl) {
            webView.loadUrl(state.currentUrl)
        }
    }

    AndroidView(
        modifier = modifier.fillMaxSize(),
        factory = {
            FrameLayout(it).apply {
                if (webView.parent != null){
                    (webView.parent as ViewGroup).removeView(webView)
                }
                addView(webView)
            }
        }
    )
}