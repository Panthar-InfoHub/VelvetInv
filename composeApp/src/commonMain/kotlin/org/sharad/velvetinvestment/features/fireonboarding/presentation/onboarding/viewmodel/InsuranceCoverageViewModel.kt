package org.sharad.velvetinvestment.features.fireonboarding.presentation.onboarding.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.sharad.velvetinvestment.features.fireonboarding.data.mapper.insuranceStepBody
import org.sharad.velvetinvestment.features.fireonboarding.domain.repository.FireOnboardingRepository
import org.sharad.velvetinvestment.utils.SnackBarController
import org.sharad.velvetinvestment.utils.networking.onError
import org.sharad.velvetinvestment.utils.networking.onSuccess
import org.sharad.velvetinvestment.utils.parseSafeLong

data class InsuranceCoverageUiState(
    val healthInsuranceAmount: Long = 0L,
    val lifeInsuranceAmount: Long = 0L,
    val isSubmitting: Boolean = false
) {
    val totalInsuranceAmount: Long get() = healthInsuranceAmount + lifeInsuranceAmount

    /** No cover entered means nothing to save; the user should skip instead. */
    val canSubmit: Boolean get() = totalInsuranceAmount > 0L
}

sealed interface InsuranceCoverageEvent {
    data class OnHealthInsuranceChanged(val value: String) : InsuranceCoverageEvent
    data class OnLifeInsuranceChanged(val value: String) : InsuranceCoverageEvent
    /** `POST /onboarding/insurance` with both cover amounts. */
    data object OnSubmitClicked : InsuranceCoverageEvent
}

class InsuranceCoverageViewModel(
    private val repository: FireOnboardingRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(InsuranceCoverageUiState())
    val uiState = _uiState.asStateFlow()

    private val _effect = Channel<FireStepEffect>()
    val effect = _effect.receiveAsFlow()

    fun handleEvent(event: InsuranceCoverageEvent) {
        when (event) {
            is InsuranceCoverageEvent.OnHealthInsuranceChanged -> _uiState.update {
                it.copy(healthInsuranceAmount = parseSafeLong(event.value, it.healthInsuranceAmount) ?: 0L)
            }
            is InsuranceCoverageEvent.OnLifeInsuranceChanged -> _uiState.update {
                it.copy(lifeInsuranceAmount = parseSafeLong(event.value, it.lifeInsuranceAmount) ?: 0L)
            }
            InsuranceCoverageEvent.OnSubmitClicked -> submit()
        }
    }

    private fun submit() {
        val state = _uiState.value
        if (state.isSubmitting || !state.canSubmit) return
        viewModelScope.launch {
            _uiState.update { it.copy(isSubmitting = true) }
            repository.submitInsurance(
                insuranceStepBody(
                    lifeInsurance = state.lifeInsuranceAmount,
                    healthInsurance = state.healthInsuranceAmount
                )
            )
                .onSuccess {
                    _uiState.update { s -> s.copy(isSubmitting = false) }
                    _effect.send(FireStepEffect.Submitted)
                }
                .onError {
                    _uiState.update { s -> s.copy(isSubmitting = false) }
                    SnackBarController.showError(it.message)
                }
        }
    }
}
