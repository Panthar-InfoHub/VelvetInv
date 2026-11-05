package org.sharad.velvetinvestment.features.onboarding.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import org.sharad.velvetinvestment.features.onboarding.ui.viewmodels.OnboardingStageLoaderEvent
import org.sharad.velvetinvestment.features.onboarding.ui.viewmodels.OnboardingStageLoaderUiState
import org.sharad.velvetinvestment.shared.compose.ErrorScreen
import org.sharad.velvetinvestment.shared.compose.LoaderScreen

/** Shown only while the stage to resume at is read from the server; it has no content of its own. */
@Composable
fun OnboardingStageLoaderScreen(
    state: OnboardingStageLoaderUiState,
    handleEvent: (OnboardingStageLoaderEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val error = state.error
    if (error != null) {
        ErrorScreen(
            errorMessage = error,
            onRetryClick = { handleEvent(OnboardingStageLoaderEvent.OnRetryClick) },
            modifier = modifier.fillMaxSize().background(White)
        )
    } else {
        LoaderScreen(modifier = modifier.background(White))
    }
}
