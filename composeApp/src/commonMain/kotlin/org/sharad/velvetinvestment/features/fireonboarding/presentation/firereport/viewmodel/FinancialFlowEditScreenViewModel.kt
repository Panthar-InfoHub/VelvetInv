package org.sharad.velvetinvestment.features.fireonboarding.presentation.firereport.viewmodel

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
import org.sharad.velvetinvestment.shared.domain.usecase.GetUserDataUseCase
import org.sharad.velvetinvestment.shared.domain.models.UserFinanceDomain
import org.sharad.velvetinvestment.utils.AppEventsController
import org.sharad.velvetinvestment.utils.SnackBarController
import org.sharad.velvetinvestment.utils.UiState
import org.sharad.velvetinvestment.utils.networking.onError
import org.sharad.velvetinvestment.utils.networking.onSuccess
import org.sharad.velvetinvestment.utils.parseSafeLong

data class FinancialFlowEditUiState(
    val financialInfo: UiState<FinancialFlowDetails> = UiState.Loading,
    val isSubmitting: Boolean = false
) {
    private val details: FinancialFlowDetails?
        get() = (financialInfo as? UiState.Success)?.data

    val expensePercentages: ExpensePercentages
        get() = details?.expensePercentages() ?: ExpensePercentages()

    val summary: FinancialSummary
        get() = (details ?: FinancialFlowDetails()).summary()
}

sealed interface FinancialFlowEditEvent {
    data object Retry : FinancialFlowEditEvent
    data object OnBackClicked : FinancialFlowEditEvent
    data class OnAnnualIncomeChanged(val value: String) : FinancialFlowEditEvent
    data class OnHouseExpenseChanged(val value: String) : FinancialFlowEditEvent
    data class OnFoodExpenseChanged(val value: String) : FinancialFlowEditEvent
    data class OnTransportExpenseChanged(val value: String) : FinancialFlowEditEvent
    data class OnOtherExpenseChanged(val value: String) : FinancialFlowEditEvent
    data object OnSubmitClicked : FinancialFlowEditEvent
}

sealed interface FinancialFlowEditEffect {
    /** Back out, whether by the back button or after the changes saved. */
    data object NavigateBack : FinancialFlowEditEffect
}

class FinancialFlowEditScreenViewModel(
    private val getUserDataUseCase: GetUserDataUseCase,
    private val repository: FireOnboardingRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FinancialFlowEditUiState())
    val uiState = _uiState.asStateFlow()

    private val _effect = Channel<FinancialFlowEditEffect>()
    val effect = _effect.receiveAsFlow()

    init {
        loadData()
    }

    fun handleEvent(event: FinancialFlowEditEvent) {
        when (event) {
            FinancialFlowEditEvent.Retry -> loadData()
            FinancialFlowEditEvent.OnBackClicked -> sendEffect(FinancialFlowEditEffect.NavigateBack)
            is FinancialFlowEditEvent.OnAnnualIncomeChanged ->
                edit { it.copy(annualIncome = parseSafeLong(event.value, it.annualIncome)) }
            is FinancialFlowEditEvent.OnHouseExpenseChanged ->
                edit { it.copy(houseExpense = parseSafeLong(event.value, it.houseExpense)) }
            is FinancialFlowEditEvent.OnFoodExpenseChanged ->
                edit { it.copy(foodExpense = parseSafeLong(event.value, it.foodExpense)) }
            is FinancialFlowEditEvent.OnTransportExpenseChanged ->
                edit { it.copy(transportExpense = parseSafeLong(event.value, it.transportExpense)) }
            is FinancialFlowEditEvent.OnOtherExpenseChanged ->
                edit { it.copy(otherExpense = parseSafeLong(event.value, it.otherExpense)) }
            FinancialFlowEditEvent.OnSubmitClicked -> submit()
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(financialInfo = UiState.Loading) }
            getUserDataUseCase()
                .onError { error -> _uiState.update { it.copy(financialInfo = UiState.Error(error.message)) } }
                .onSuccess { data ->
                    _uiState.update {
                        it.copy(financialInfo = UiState.Success(data.finance?.toFinancialFlowDetails() ?: FinancialFlowDetails()))
                    }
                }
        }
    }

    /** Edits apply only once the figures have loaded. */
    private fun edit(change: (FinancialFlowDetails) -> FinancialFlowDetails) {
        _uiState.update { state ->
            val current = state.financialInfo as? UiState.Success ?: return@update state
            state.copy(financialInfo = UiState.Success(change(current.data)))
        }
    }

    private fun submit() {
        val state = _uiState.value
        val details = (state.financialInfo as? UiState.Success)?.data ?: return
        if (state.isSubmitting) return
        viewModelScope.launch {
            _uiState.update { it.copy(isSubmitting = true) }
            repository.submitFinance(details.toStepBody())
                .onError {
                    _uiState.update { s -> s.copy(isSubmitting = false) }
                    SnackBarController.showError(it.message)
                }
                .onSuccess {
                    _uiState.update { s -> s.copy(isSubmitting = false) }
                    AppEventsController.sendFireRefreshEvent()
                    _effect.send(FinancialFlowEditEffect.NavigateBack)
                }
        }
    }

    private fun sendEffect(effect: FinancialFlowEditEffect) {
        viewModelScope.launch { _effect.send(effect) }
    }
}

private fun UserFinanceDomain.toFinancialFlowDetails(): FinancialFlowDetails = FinancialFlowDetails(
    annualIncome = annualIncome,
    houseExpense = houseExpense,
    foodExpense = foodExpense,
    transportExpense = transportExpense,
    otherExpense = otherExpense
)
