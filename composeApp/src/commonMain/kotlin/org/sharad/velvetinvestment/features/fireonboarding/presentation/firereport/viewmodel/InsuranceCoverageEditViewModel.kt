package org.sharad.velvetinvestment.features.fireonboarding.presentation.firereport.viewmodel

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
import org.sharad.velvetinvestment.utils.AppEventsController
import org.sharad.velvetinvestment.utils.SnackBarController
import org.sharad.velvetinvestment.utils.UiState
import org.sharad.velvetinvestment.utils.networking.onError
import org.sharad.velvetinvestment.utils.networking.onSuccess
import org.sharad.velvetinvestment.utils.parseSafeLong
import org.sharad.velvetinvestment.shared.domain.usecase.GetUserDataUseCase

data class InsuranceFlowDetails(
    val healthInsurance: Long? = null,
    val lifeInsurance: Long? = null
)

data class InsuranceCoverageEditUiState(
    val insuranceInfo: UiState<InsuranceFlowDetails> = UiState.Loading,
    val isSubmitting: Boolean = false
) {
    val totalInsuranceAmount: Long
        get() = (insuranceInfo as? UiState.Success)?.data
            ?.let { (it.healthInsurance ?: 0L) + (it.lifeInsurance ?: 0L) } ?: 0L
}

sealed interface InsuranceCoverageEditEvent {
    data object Retry : InsuranceCoverageEditEvent
    data object OnBackClicked : InsuranceCoverageEditEvent
    data class OnHealthInsuranceChanged(val value: String) : InsuranceCoverageEditEvent
    data class OnLifeInsuranceChanged(val value: String) : InsuranceCoverageEditEvent
    data object OnSubmitClicked : InsuranceCoverageEditEvent
}

sealed interface InsuranceCoverageEditEffect {
    /** Back out, whether by the back button or after the changes saved. */
    data object NavigateBack : InsuranceCoverageEditEffect
}

class InsuranceCoverageEditViewModel(
    private val getUserDataUseCase: GetUserDataUseCase,
    private val repository: FireOnboardingRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(InsuranceCoverageEditUiState())
    val uiState = _uiState.asStateFlow()

    private val _effect = Channel<InsuranceCoverageEditEffect>()
    val effect = _effect.receiveAsFlow()

    init {
        loadData()
    }

    fun handleEvent(event: InsuranceCoverageEditEvent) {
        when (event) {
            InsuranceCoverageEditEvent.Retry -> loadData()
            InsuranceCoverageEditEvent.OnBackClicked -> sendEffect(InsuranceCoverageEditEffect.NavigateBack)
            is InsuranceCoverageEditEvent.OnHealthInsuranceChanged ->
                edit { it.copy(healthInsurance = parseSafeLong(event.value, it.healthInsurance)) }
            is InsuranceCoverageEditEvent.OnLifeInsuranceChanged ->
                edit { it.copy(lifeInsurance = parseSafeLong(event.value, it.lifeInsurance)) }
            InsuranceCoverageEditEvent.OnSubmitClicked -> submit()
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(insuranceInfo = UiState.Loading) }
            getUserDataUseCase()
                .onError { error -> _uiState.update { it.copy(insuranceInfo = UiState.Error(error.message)) } }
                .onSuccess { data ->
                    val insurance = data.insurance
                    _uiState.update {
                        it.copy(
                            insuranceInfo = UiState.Success(
                                InsuranceFlowDetails(
                                    healthInsurance = insurance?.healthInsurance,
                                    lifeInsurance = insurance?.lifeInsurance
                                )
                            )
                        )
                    }
                }
        }
    }

    private fun edit(change: (InsuranceFlowDetails) -> InsuranceFlowDetails) {
        _uiState.update { state ->
            val current = state.insuranceInfo as? UiState.Success ?: return@update state
            state.copy(insuranceInfo = UiState.Success(change(current.data)))
        }
    }

    private fun submit() {
        val state = _uiState.value
        val details = (state.insuranceInfo as? UiState.Success)?.data ?: return
        if (state.isSubmitting) return
        val body = insuranceStepBody(
            lifeInsurance = details.lifeInsurance ?: 0L,
            healthInsurance = details.healthInsurance ?: 0L
        )
        viewModelScope.launch {
            _uiState.update { it.copy(isSubmitting = true) }
            repository.submitInsurance(body)
                .onError {
                    _uiState.update { s -> s.copy(isSubmitting = false) }
                    SnackBarController.showError(it.message)
                }
                .onSuccess {
                    _uiState.update { s -> s.copy(isSubmitting = false) }
                    AppEventsController.sendFireRefreshEvent()
                    _effect.send(InsuranceCoverageEditEffect.NavigateBack)
                }
        }
    }

    private fun sendEffect(effect: InsuranceCoverageEditEffect) {
        viewModelScope.launch { _effect.send(effect) }
    }
}
