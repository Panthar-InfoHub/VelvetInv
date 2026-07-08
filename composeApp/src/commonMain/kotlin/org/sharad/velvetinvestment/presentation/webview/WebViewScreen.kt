package org.sharad.velvetinvestment.presentation.webview

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.sharad.velvetinvestment.domain.webview.WebViewConfig
import org.sharad.velvetinvestment.domain.webview.WebViewExitUrlMatcher
import org.sharad.velvetinvestment.shared.compose.BackHeader

@Composable
fun WebViewScreen(
    config: WebViewConfig,
    onExitUrlReached: (String) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state = rememberWebViewState(config.url)

    Column(modifier = modifier.fillMaxSize()) {
        BackHeader(
            heading = config.title.orEmpty(),
            showBack = true,
            onBackClick = onBackClick
        )

        Box(modifier = Modifier.fillMaxSize()) {
            PlatformWebView(
                state = state,
                modifier = Modifier.fillMaxSize(),
                onUrlChanged = { url ->
                    if (WebViewExitUrlMatcher.matches(url, config.exitUrlPatterns, config.matchType)) {
                        onExitUrlReached(url)
                    }
                }
            )
        }
    }
}
