package org.sharad.velvetinvestment.features.fireonboarding.presentation.onboarding.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.sharad.velvetinvestment.features.fireonboarding.data.mapper.toStepBody
import org.sharad.velvetinvestment.features.fireonboarding.domain.repository.FireOnboardingRepository
import org.sharad.velvetinvestment.features.fireonboarding.presentation.onboarding.models.ExpensePercentages
import org.sharad.velvetinvestment.features.fireonboarding.presentation.onboarding.models.FinancialFlowDetails
import org.sharad.velvetinvestment.features.fireonboarding.presentation.onboarding.models.FinancialSummary
import org.sharad.velvetinvestment.features.fireonboarding.presentation.onboarding.models.expensePercentages
import org.sharad.velvetinvestment.features.fireonboarding.presentation.onboarding.models.summary
import org.sharad.velvetinvestment.utils.SnackBarController
import org.sharad.velvetinvestment.utils.networking.onError
import org.sharad.velvetinvestment.utils.networking.onSuccess
import org.sharad.velvetinvestment.utils.parseSafeLong

data class FinancialFlowUiState(
    val details: FinancialFlowDetails = FinancialFlowDetails(),
    val isSubmitting: Boolean = false
) {
    val expensePercentages: ExpensePercentages get() = details.expensePercentages()
    val summary: FinancialSummary get() = details.summary()

    /** Income is what the step is about; with none, the user should skip instead. */
    val canSubmit: Boolean get() = (details.annualIncome ?: 0L) > 0L
}

sealed interface FinancialFlowEvent {
    data class OnAnnualIncomeChanged(val value: String) : FinancialFlowEvent
    data class OnHouseExpenseChanged(val value: String) : FinancialFlowEvent
    data class OnFoodExpenseChanged(val value: String) : FinancialFlowEvent
    data class OnTransportExpenseChanged(val value: String) : FinancialFlowEvent
    data class OnOtherExpenseChanged(val value: String) : FinancialFlowEvent
    /** `POST /onboarding/finance` with the figures. */
    data object OnSubmitClicked : FinancialFlowEvent
}

class FinancialFlowScreenViewModel(
    private val repository: FireOnboardingRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FinancialFlowUiState())
    val uiState = _uiState.asStateFlow()

    private val _effect = Channel<FireStepEffect>()
    val effect = _effect.receiveAsFlow()

    fun handleEvent(event: FinancialFlowEvent) {
        when (event) {
            is FinancialFlowEvent.OnAnnualIncomeChanged ->
                edit { it.copy(annualIncome = parseSafeLong(event.value, it.annualIncome)) }
            is FinancialFlowEvent.OnHouseExpenseChanged ->
                edit { it.copy(houseExpense = parseSafeLong(event.value, it.houseExpense)) }
            is FinancialFlowEvent.OnFoodExpenseChanged ->
                edit { it.copy(foodExpense = parseSafeLong(event.value, it.foodExpense)) }
            is FinancialFlowEvent.OnTransportExpenseChanged ->
                edit { it.copy(transportExpense = parseSafeLong(event.value, it.transportExpense)) }
            is FinancialFlowEvent.OnOtherExpenseChanged ->
                edit { it.copy(otherExpense = parseSafeLong(event.value, it.otherExpense)) }
            FinancialFlowEvent.OnSubmitClicked -> submit()
        }
    }

    private fun edit(change: (FinancialFlowDetails) -> FinancialFlowDetails) {
        _uiState.update { it.copy(details = change(it.details)) }
    }

    private fun submit() {
        val state = _uiState.value
        if (state.isSubmitting || !state.canSubmit) return
        viewModelScope.launch {
            _uiState.update { it.copy(isSubmitting = true) }
            repository.submitFinance(state.details.toStepBody())
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
