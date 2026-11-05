package org.sharad.velvetinvestment.features.fireonboarding.presentation.onboarding.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.sharad.velvetinvestment.features.fireonboarding.domain.models.FireStage
import org.sharad.velvetinvestment.features.fireonboarding.domain.models.nextFireStage
import org.sharad.velvetinvestment.features.fireonboarding.domain.repository.FireOnboardingRepository
import org.sharad.velvetinvestment.shared.domain.usecase.GetUserDataUseCase
import org.sharad.velvetinvestment.utils.AppEventsController
import org.sharad.velvetinvestment.utils.SnackBarController
import org.sharad.velvetinvestment.utils.networking.onError
import org.sharad.velvetinvestment.utils.networking.onSuccess

data class FireOnboardingUiState(
    /** Reading `GET /user/` to find the step; full-screen loader on the first read only. */
    val isLoading: Boolean = true,
    val error: String? = null,
    /** The step on screen; null until the first read lands. */
    val stage: FireStage? = null,
    /** A skip, or the reload after a step was saved, is running. */
    val isAdvancing: Boolean = false
) {
    val stepNumber: Int get() = stage?.stepNumber ?: 0
    val totalSteps: Int get() = FireStage.total
}

sealed interface FireOnboardingEvent {
    data object Retry : FireOnboardingEvent
    /** The header's Skip: skips the step on screen. */
    data object OnSkipClicked : FireOnboardingEvent
    /** The step on screen saved its data (its view model sent [FireStepEffect.Submitted]). */
    data object OnStepSubmitted : FireOnboardingEvent
    /** Back on a step: leave the flow; what was saved stays saved and the flow resumes there. */
    data object OnExitClicked : FireOnboardingEvent
}

sealed interface FireOnboardingEffect {
    data class NavigateToStage(val stage: FireStage) : FireOnboardingEffect
    /** Every step is verified or skipped, or the user left: close the flow. */
    data object Close : FireOnboardingEffect
}

/**
 * Coordinates the FIRE onboarding flow. The step shown is always worked out from the server:
 * after every save or skip `GET /user/` is re-read and the first still-pending step in
 * finance → assets → loan → insurance → goals order is opened. When none is left the flow closes
 * and the home screen and FIRE report are told to refresh.
 */
class FireOnboardingViewModel(
    private val getUserDataUseCase: GetUserDataUseCase,
    private val repository: FireOnboardingRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FireOnboardingUiState())
    val uiState = _uiState.asStateFlow()

    private val _effect = Channel<FireOnboardingEffect>()
    val effect = _effect.receiveAsFlow()

    /** Something was saved or skipped, so the screens behind the flow are out of date. */
    private var changed = false

    init {
        resolveStage()
    }

    fun handleEvent(event: FireOnboardingEvent) {
        when (event) {
            FireOnboardingEvent.Retry -> resolveStage()
            FireOnboardingEvent.OnSkipClicked -> skipCurrent()
            FireOnboardingEvent.OnStepSubmitted -> {
                changed = true
                resolveStage()
            }
            FireOnboardingEvent.OnExitClicked -> viewModelScope.launch { close() }
        }
    }

    private fun skipCurrent() {
        val state = _uiState.value
        val stage = state.stage ?: return
        if (state.isAdvancing) return
        viewModelScope.launch {
            _uiState.update { it.copy(isAdvancing = true) }
            repository.skip(stage)
                .onSuccess {
                    changed = true
                    resolveStage()
                }
                .onError {
                    _uiState.update { s -> s.copy(isAdvancing = false) }
                    SnackBarController.showError(it.message)
                }
        }
    }

    /** Re-reads the user and opens the next pending step, or closes the flow when none is left. */
    private fun resolveStage() {
        viewModelScope.launch {
            val first = _uiState.value.stage == null
            _uiState.update {
                if (first) it.copy(isLoading = true, error = null) else it.copy(isAdvancing = true)
            }
            getUserDataUseCase()
                .onSuccess { user ->
                    val next = user.onboarding.nextFireStage()
                    if (next == null) {
                        changed = true
                        close()
                        return@onSuccess
                    }
                    _uiState.update {
                        it.copy(isLoading = false, isAdvancing = false, error = null, stage = next)
                    }
                    _effect.send(FireOnboardingEffect.NavigateToStage(next))
                }
                .onError { error ->
                    if (first) {
                        _uiState.update { it.copy(isLoading = false, error = error.message) }
                    } else {
                        // The step was saved; only the reload failed. Stay put and let the user retry.
                        _uiState.update { it.copy(isAdvancing = false) }
                        SnackBarController.showError(error.message)
                    }
                }
        }
    }

    private suspend fun close() {
        if (changed) {
            // One event on purpose: AppEventsController replays only the latest one to a listener
            // that is off screen. Home reloads on it, and the FIRE report reloads and then
            // forwards a home refresh itself.
            AppEventsController.sendFireRefreshEvent()
        }
        _effect.send(FireOnboardingEffect.Close)
    }
}
