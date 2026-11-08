package org.sharad.velvetinvestment.features.webview

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import org.sharad.velvetinvestment.domain.webview.WebViewConfig

@Composable
fun UpiPaymentWebViewScreen(
    config: WebViewConfig,
    onAppLinkCaught: (String) -> Unit,
    onExitUrlReached: (String) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentOnAppLinkCaught by rememberUpdatedState(onAppLinkCaught)
    val currentOnExitUrlReached by rememberUpdatedState(onExitUrlReached)
    val currentOnBackClick by rememberUpdatedState(onBackClick)

    var finished by remember { mutableStateOf(false) }

    fun finish(action: () -> Unit) {
        if (!finished) {
            finished = true
            action()
        }
    }

    WebViewScreen(
        config = config,
        onExitUrlReached = { url -> finish { currentOnExitUrlReached(url) } },
        onBackClick = { finish { currentOnBackClick() } },
        modifier = modifier
    )
}
