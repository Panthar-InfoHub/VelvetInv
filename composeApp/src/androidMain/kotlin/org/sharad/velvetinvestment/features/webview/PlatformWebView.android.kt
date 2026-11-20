package org.sharad.velvetinvestment.features.webview

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Message
import android.util.Log
import android.view.ViewGroup
import android.webkit.ConsoleMessage
import android.webkit.CookieManager
import android.webkit.GeolocationPermissions
import android.webkit.PermissionRequest
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import org.sharad.velvetinvestment.features.webview.WebViewState
import org.sharad.velvetinvestment.utils.deeplink.ExternalAppUrl

@SuppressLint("SetJavaScriptEnabled")
@Composable
actual fun PlatformWebView(
    state: WebViewState,
    modifier: Modifier,
    onUrlChanged: (String) -> Unit,
    onExternalAppUrl: ((String) -> Unit)?
) {
    val context = LocalContext.current

    val currentOnUrlChanged by rememberUpdatedState(onUrlChanged)
    val currentOnExternalAppUrl by rememberUpdatedState(onExternalAppUrl)

    /**
     * Hands a main-frame URL to the caller without touching [WebViewState.currentUrl] — that
     * field drives the loader, and a URL seen before it loads must not be fed back into it.
     * Interception runs off the main thread, so the callback is posted.
     */
    fun WebView?.reportUrl(url: String?) {
        if (url.isNullOrBlank()) return
        if (this == null) currentOnUrlChanged(url) else post { currentOnUrlChanged(url) }
    }

    /**
     * Keeps an app link (`gpay://`, `intent://` …) out of the web view, which can only fail on it
     * with an unknown-scheme error page — whether the user tapped it or the page redirected to it
     * on its own. The caller, when listening, gets the link to open in its app. Returns true for
     * any app link, so the navigation must be cancelled; false leaves a web URL to load.
     */
    fun WebView?.handOffExternalAppUrl(url: String?): Boolean {
        if (url == null || !ExternalAppUrl.isExternalAppUrl(url)) return false
        currentOnExternalAppUrl?.let { handler ->
            if (this == null) handler(url) else post { handler(url) }
        }
        return true
    }

    // A page asking for camera/mic/location (video KYC) is only granted what the app itself holds.
    // Whatever the app lacks is asked of the user first; the page's request waits on the answer.
    var pendingPageGrant by remember { mutableStateOf<((Map<String, Boolean>) -> Unit)?>(null) }
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { results ->
        Log.d("WEB-PERMISSION", "user answered: $results")
        pendingPageGrant?.invoke(results)
        pendingPageGrant = null
    }

    fun isGranted(permission: String) =
        ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED

    /** Calls [onResult] with each of [permissions] mapped to whether the app now holds it. */
    fun withAppPermissions(permissions: List<String>, onResult: (Map<String, Boolean>) -> Unit) {
        val missing = permissions.filterNot(::isGranted)
        if (missing.isEmpty()) {
            onResult(permissions.associateWith { true })
            return
        }
        // Only one request can be in flight; a newer one supersedes (and denies) the older.
        pendingPageGrant?.invoke(emptyMap())
        pendingPageGrant = { results ->
            onResult(permissions.associateWith { results[it] ?: isGranted(it) })
        }
        permissionLauncher.launch(missing.toTypedArray())
    }

    val webView = remember {
        WebView.setWebContentsDebuggingEnabled(true)

        WebView(context).apply {
            setLayerType(WebView.LAYER_TYPE_HARDWARE, null)

            settings.apply {
                databaseEnabled = true
                javaScriptEnabled = true
                domStorageEnabled = true
                blockNetworkLoads = false
                blockNetworkImage = false

                javaScriptCanOpenWindowsAutomatically = true
                setSupportMultipleWindows(true)

                allowFileAccess = true
                allowContentAccess = true

                useWideViewPort = true
                loadWithOverviewMode = true
                loadsImagesAutomatically = true

                mediaPlaybackRequiresUserGesture = false
                cacheMode = WebSettings.LOAD_DEFAULT
                mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            }

            Log.d(
                "WEB-UA",
                settings.userAgentString
            )

            CookieManager.getInstance().setAcceptCookie(true)
            CookieManager.getInstance().setAcceptThirdPartyCookies(this, true)

            webViewClient = object : WebViewClient() {

                override fun shouldInterceptRequest(
                    view: WebView?,
                    request: WebResourceRequest?
                ): WebResourceResponse? {

                    Log.d(
                        "WEB-REQUEST",
                        """
        URL: ${request?.url}
        Method: ${request?.method}
        Headers:
        ${request?.requestHeaders}
        """.trimIndent()
                    )

                    // The return hop is typically a POST to a URL the app owns but no server
                    // serves, so it never commits and no page callback ever carries it. Reporting
                    // the request itself is the only point the URL is reliably seen.
                    if (request?.isForMainFrame == true) {
                        view?.reportUrl(request.url?.toString())
                    }

                    return super.shouldInterceptRequest(view, request)
                }

                override fun onReceivedHttpError(
                    view: WebView?,
                    request: WebResourceRequest?,
                    errorResponse: WebResourceResponse?
                ) {

                    Log.e(
                        "WEB-HTTP",
                        """
        URL: ${request?.url}
        Status: ${errorResponse?.statusCode}
        Reason: ${errorResponse?.reasonPhrase}
        """.trimIndent()
                    )

                    super.onReceivedHttpError(view, request, errorResponse)
                }

                override fun onReceivedError(
                    view: WebView?,
                    request: WebResourceRequest?,
                    error: WebResourceError?
                ) {

                    Log.e(
                        "WEB-ERROR",
                        """
        URL: ${request?.url}
        Code: ${error?.errorCode}
        Description: ${error?.description}
        """.trimIndent()
                    )

                    // A return URL that resolves to nothing still errors on the exact URL the
                    // flow signals completion with, so it counts as having been reached.
                    if (request?.isForMainFrame == true) {
                        view.reportUrl(request.url?.toString())
                    }

                    super.onReceivedError(view, request, error)
                }

                override fun onPageStarted(
                    view: WebView?,
                    url: String?,
                    favicon: Bitmap?
                ) {
                    super.onPageStarted(view, url, favicon)

                    Log.d("WEB", "Started : $url")

                    // WebView can still announce an app link it was told not to load. It is not a
                    // page, so it must never become the current URL — the loader below would
                    // otherwise load it directly, past shouldOverrideUrlLoading.
                    if (ExternalAppUrl.isExternalAppUrl(url)) return

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

                    // A cancelled redirect to an app link (a page that sends itself to gpay://
                    // on load) is still reported here. Recording it as the current URL is what
                    // made the loader reload it and land on the error page.
                    if (ExternalAppUrl.isExternalAppUrl(url)) {
                        Log.d("WEB", "Ignored finish of app link : $url")
                        return
                    }

                    view?.evaluateJavascript(
                        """
    (function(){

        if(window.__networkHookInstalled)
            return;

        window.__networkHookInstalled = true;

        const oldFetch = window.fetch;

        window.fetch = function(){

            console.log(
                "[FETCH]",
                arguments[0]
            );

            return oldFetch.apply(this, arguments);
        };

        const oldOpen =
            XMLHttpRequest.prototype.open;

        XMLHttpRequest.prototype.open =
            function(method, url){

                console.log(
                    "[XHR]",
                    method,
                    url
                );

                return oldOpen.apply(this, arguments);
            };

    })();
    """.trimIndent(),
                        null
                    )

                    CookieManager.getInstance().flush()

                    Log.d(
                        "WEB-COOKIE",
                        CookieManager.getInstance().getCookie(url ?: "").orEmpty()
                    )

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

                    // Checked for every frame, not just the main one: a payment widget living in
                    // an iframe links to the UPI apps from there.
                    if (view.handOffExternalAppUrl(request?.url?.toString())) {
                        return true
                    }

                    if (request?.isForMainFrame == true) {
                        view.reportUrl(request.url?.toString())
                    }

                    return false
                }
            }

            webChromeClient = object : WebChromeClient() {

                override fun onConsoleMessage(
                    consoleMessage: ConsoleMessage
                ): Boolean {

                    Log.d(
                        "WEB-CONSOLE",
                        """
        ${consoleMessage.messageLevel()}
        ${consoleMessage.message()}
        ${consoleMessage.sourceId()}
        line ${consoleMessage.lineNumber()}
        """.trimIndent()
                    )

                    return super.onConsoleMessage(consoleMessage)
                }

                // getUserMedia() from the page lands here. Without this override WebView denies
                // it outright, so the KYC video call never gets the camera or microphone.
                override fun onPermissionRequest(request: PermissionRequest) {
                    val resourceToPermission = mapOf(
                        PermissionRequest.RESOURCE_VIDEO_CAPTURE to Manifest.permission.CAMERA,
                        PermissionRequest.RESOURCE_AUDIO_CAPTURE to Manifest.permission.RECORD_AUDIO
                    )
                    val wanted = request.resources.filter { it in resourceToPermission }
                    Log.d("WEB-PERMISSION", "${request.origin} asks for ${request.resources.toList()}")
                    if (wanted.isEmpty()) {
                        request.deny()
                        return
                    }
                    withAppPermissions(wanted.map { resourceToPermission.getValue(it) }) { result ->
                        val granted = wanted.filter { result[resourceToPermission.getValue(it)] == true }
                        Log.d("WEB-PERMISSION", "granting page $granted")
                        if (granted.isEmpty()) request.deny() else request.grant(granted.toTypedArray())
                    }
                }

                override fun onPermissionRequestCanceled(request: PermissionRequest) {
                    Log.d("WEB-PERMISSION", "page cancelled ${request.resources.toList()}")
                    pendingPageGrant = null
                }

                // navigator.geolocation from the page; KYC flows commonly capture it.
                override fun onGeolocationPermissionsShowPrompt(
                    origin: String,
                    callback: GeolocationPermissions.Callback
                ) {
                    withAppPermissions(
                        listOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                    ) { result ->
                        val allowed = result.values.any { it }
                        Log.d("WEB-PERMISSION", "geolocation for $origin: $allowed")
                        callback.invoke(origin, allowed, false)
                    }
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

                    popup.webViewClient = object : WebViewClient() {

                        override fun shouldOverrideUrlLoading(
                            child: WebView?,
                            request: WebResourceRequest?
                        ): Boolean {

                            val url = request?.url?.toString()
                            if (!view.handOffExternalAppUrl(url)) {
                                view?.loadUrl(url.toString())
                            }

                            child?.destroy()

                            return true
                        }

                        override fun onPageStarted(
                            child: WebView?,
                            url: String?,
                            favicon: Bitmap?
                        ) {

                            if (!view.handOffExternalAppUrl(url)) {
                                url?.let {
                                    view?.loadUrl(it)
                                }
                            }

                            child?.destroy()
                        }
                    }
                    transport.webView = popup
                    resultMsg.sendToTarget()

                    return true
                }
            }

            loadUrl(state.currentUrl)
        }
    }

    webView.settings.userAgentString =
        "Mozilla/5.0 (Linux; Android 15; Pixel 9) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/150.0.0.0 Mobile Safari/537.36"

    Log.d("UA", webView.settings.userAgentString)

    DisposableEffect(webView) {
        onDispose {
            webView.stopLoading()
            webView.destroy()
        }
    }

    LaunchedEffect(state.currentUrl) {
        // loadUrl() skips shouldOverrideUrlLoading, so this is the last point an app link could
        // slip into the page; it never loads one.
        if (webView.url != state.currentUrl && !ExternalAppUrl.isExternalAppUrl(state.currentUrl)) {
            webView.loadUrl(state.currentUrl)
        }
    }

    AndroidView(
        modifier = modifier.fillMaxSize(),
        factory = {
            FrameLayout(it).apply {
                if (webView.parent != null) {
                    (webView.parent as ViewGroup).removeView(webView)
                }
                addView(webView)
            }
        }
    )
}