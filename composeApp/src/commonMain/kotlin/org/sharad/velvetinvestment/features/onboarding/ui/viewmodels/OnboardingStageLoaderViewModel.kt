package org.sharad.velvetinvestment.features.onboarding.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.sharad.velvetinvestment.features.core.domain.models.OnboardingStage
import org.sharad.velvetinvestment.features.core.domain.models.isAfterPanVerification
import org.sharad.velvetinvestment.utils.networking.NetworkResponse
import org.sharad.velvetinvestment.shared.domain.usecase.GetUserDataUseCase

data class OnboardingStageLoaderUiState(
    val isLoading: Boolean = true,
    /** Set when the user data could not be read; the screen offers a retry. */
    val error: String? = null
)

sealed interface OnboardingStageLoaderEvent {
    data object OnRetryClick : OnboardingStageLoaderEvent
}

sealed interface OnboardingStageLoaderEffect {
    /**
     * [isKycRequired] marks the branch taken when PAN verification fails. It is only set for a
     * stage after PAN verification; before that, the PAN screen picks the branch itself.
     */
    data class StageResolved(
        val stage: OnboardingStage,
        val isKycRequired: Boolean
    ) : OnboardingStageLoaderEffect
}

/**
 * The entry point of every onboarding session. Reads `GET /user/` and resolves the stage to resume
 * at from `onboarding.current_stage` alone. Nothing local and nothing passed in by a caller is
 * consulted. Past PAN verification, the branch comes from the server's `is_kyc_completed`: still
 * false there means the KYC branch.
 */
class OnboardingStageLoaderViewModel(
    private val getUserData: GetUserDataUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(OnboardingStageLoaderUiState())
    val uiState = _uiState.asStateFlow()

    private val _effect = Channel<OnboardingStageLoaderEffect>()
    val effect = _effect.receiveAsFlow()

    init {
        loadStage()
    }

    fun handleEvent(event: OnboardingStageLoaderEvent) {
        when (event) {
            OnboardingStageLoaderEvent.OnRetryClick -> loadStage()
        }
    }

    private fun loadStage() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            when (val result = getUserData()) {
                is NetworkResponse.Error -> _uiState.update {
                    it.copy(isLoading = false, error = result.error.message)
                }

                is NetworkResponse.Success -> {
                    val user = result.data
                    val stage = OnboardingStage.fromServer(user.onboarding.currentStage)
                    _effect.send(
                        OnboardingStageLoaderEffect.StageResolved(
                            stage = stage,
                            isKycRequired = stage.isAfterPanVerification && !user.isKycCompleted
                        )
                    )
                }
            }
        }
    }
}
