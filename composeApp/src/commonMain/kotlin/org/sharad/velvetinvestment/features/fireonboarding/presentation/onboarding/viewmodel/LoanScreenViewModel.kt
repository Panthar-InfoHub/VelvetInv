package org.sharad.velvetinvestment.features.fireonboarding.presentation.onboarding.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.sharad.velvetinvestment.domain.LoanTypes
import org.sharad.velvetinvestment.features.fireonboarding.data.mapper.toStepBody
import org.sharad.velvetinvestment.features.fireonboarding.domain.repository.FireOnboardingRepository
import org.sharad.velvetinvestment.features.fireonboarding.presentation.onboarding.models.LoanInfo
import org.sharad.velvetinvestment.utils.SnackBarController
import org.sharad.velvetinvestment.utils.networking.onError
import org.sharad.velvetinvestment.utils.networking.onSuccess

/** The loan being typed on the add-loan screen; each field is null until it parses. */
data class LoanDraft(
    val loanType: LoanTypes? = null,
    val outstandingAmount: Long? = null,
    val monthlyEmi: Long? = null,
    val tenure: Int? = null
) {
    fun toLoanOrNull(): LoanInfo? {
        return LoanInfo(
            loanType = loanType ?: return null,
            outstandingAmount = outstandingAmount ?: return null,
            monthlyEmi = monthlyEmi ?: return null,
            tenure = tenure ?: return null
        )
    }
}

data class LoanUiState(
    val loans: List<LoanInfo> = emptyList(),
    val draft: LoanDraft = LoanDraft(),
    val isSubmitting: Boolean = false
) {
    /** No loans means nothing to save; the user should skip instead. */
    val canSubmit: Boolean get() = loans.isNotEmpty()

    val totalOutstandingDebt: Long get() = loans.sumOf { it.outstandingAmount }
    val monthlyEmiBurden: Long get() = loans.sumOf { it.monthlyEmi }
    val totalTenure: Int get() = loans.sumOf { it.tenure }
}

sealed interface LoanEvent {
    data class OnLoanTypeSelected(val type: LoanTypes) : LoanEvent
    data class OnOutstandingAmountChanged(val value: String) : LoanEvent
    data class OnMonthlyEmiChanged(val value: String) : LoanEvent
    data class OnTenureChanged(val value: String) : LoanEvent
    /** Adds the draft when every field is filled; an incomplete draft is dropped. */
    data object OnAddLoanClicked : LoanEvent
    /** Opening the add-loan screen starts from an empty draft. */
    data object OnAddLoanOpened : LoanEvent
    data class OnDeleteLoanClicked(val loan: LoanInfo) : LoanEvent
    /** `POST /onboarding/loans` with every loan added. */
    data object OnSubmitClicked : LoanEvent
}

class LoanScreenViewModel(
    private val repository: FireOnboardingRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoanUiState())
    val uiState = _uiState.asStateFlow()

    private val _effect = Channel<FireStepEffect>()
    val effect = _effect.receiveAsFlow()

    fun handleEvent(event: LoanEvent) {
        when (event) {
            is LoanEvent.OnLoanTypeSelected -> editDraft { it.copy(loanType = event.type) }
            is LoanEvent.OnOutstandingAmountChanged -> editDraft { it.copy(outstandingAmount = event.value.toLongOrNull()) }
            is LoanEvent.OnMonthlyEmiChanged -> editDraft { it.copy(monthlyEmi = event.value.toLongOrNull()) }
            is LoanEvent.OnTenureChanged -> editDraft { it.copy(tenure = event.value.toIntOrNull()) }
            LoanEvent.OnAddLoanClicked -> _uiState.update { state ->
                val loan = state.draft.toLoanOrNull() ?: return@update state
                state.copy(loans = state.loans + loan, draft = LoanDraft())
            }
            LoanEvent.OnAddLoanOpened -> editDraft { LoanDraft() }
            is LoanEvent.OnDeleteLoanClicked -> _uiState.update { state ->
                val index = state.loans.indexOf(event.loan)
                if (index == -1) state else state.copy(loans = state.loans.toMutableList().apply { removeAt(index) })
            }
            LoanEvent.OnSubmitClicked -> submit()
        }
    }

    private fun submit() {
        val state = _uiState.value
        if (state.isSubmitting || !state.canSubmit) return
        viewModelScope.launch {
            _uiState.update { it.copy(isSubmitting = true) }
            repository.submitLoans(state.loans.toStepBody())
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

    private fun editDraft(change: (LoanDraft) -> LoanDraft) {
        _uiState.update { it.copy(draft = change(it.draft)) }
    }
}
