package org.sharad.velvetinvestment.features.fireonboarding.presentation.onboarding.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.sharad.velvetinvestment.data.remote.model.onboarding.OnBoardingBodyDto
import org.sharad.velvetinvestment.domain.usecases.user.OnBoardUserUseCase
import org.sharad.velvetinvestment.utils.SnackBarController
import org.sharad.velvetinvestment.utils.networking.onError
import org.sharad.velvetinvestment.utils.networking.onSuccess

data class OnBoardingConfirmationUiState(
    val isSubmitting: Boolean = false
)

sealed interface OnBoardingConfirmationEvent {
    /** Sends everything the questionnaire collected; [body] is assembled by the summary screen. */
    data class OnSubmitClicked(val body: OnBoardingBodyDto) : OnBoardingConfirmationEvent
}

sealed interface OnBoardingConfirmationEffect {
    /** The FIRE profile is saved; the flow is over. */
    data object Completed : OnBoardingConfirmationEffect
}

class OnBoardingConfirmationViewModel(
    private val onBoardUserUseCase: OnBoardUserUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(OnBoardingConfirmationUiState())
    val uiState = _uiState.asStateFlow()

    private val _effect = Channel<OnBoardingConfirmationEffect>()
    val effect = _effect.receiveAsFlow()

    fun handleEvent(event: OnBoardingConfirmationEvent) {
        when (event) {
            is OnBoardingConfirmationEvent.OnSubmitClicked -> submit(event.body)
        }
    }

    private fun submit(body: OnBoardingBodyDto) {
        if (_uiState.value.isSubmitting) return
        viewModelScope.launch {
            _uiState.update { it.copy(isSubmitting = true) }
            onBoardUserUseCase(body)
                .onSuccess {
                    _uiState.update { s -> s.copy(isSubmitting = false) }
                    _effect.send(OnBoardingConfirmationEffect.Completed)
                }
                .onError {
                    _uiState.update { s -> s.copy(isSubmitting = false) }
                    SnackBarController.showError(it.message)
                }
        }
    }
}
