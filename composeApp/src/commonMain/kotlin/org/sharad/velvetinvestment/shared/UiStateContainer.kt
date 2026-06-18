package org.sharad.velvetinvestment.shared

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.sharad.velvetinvestment.shared.compose.ErrorScreen
import org.sharad.velvetinvestment.shared.compose.LoaderScreen
import org.sharad.velvetinvestment.utils.UiState
import org.sharad.velvetinvestment.utils.clearFocusOnTap

@Composable
fun <T> UiStateContainer(
    uiState: UiState<T>,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
    loadingContent: @Composable () -> Unit = { LoaderScreen() },
    errorContent: @Composable (String) -> Unit = {
        ErrorScreen(
            errorMessage = it,
            onRetryClick = onRetry
        )
    },
    successContent: @Composable (T) -> Unit
) {
    Box(
        modifier = modifier
            .clearFocusOnTap(),
        contentAlignment = Alignment.Center
    ) {
        when (uiState) {
            UiState.Loading -> {
                loadingContent()
            }

            is UiState.Error -> {
                errorContent(uiState.message)
            }

            is UiState.Success -> {
                successContent(uiState.data)
            }
        }
    }
}