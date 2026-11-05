package org.sharad.velvetinvestment.presentation.loans.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.sharad.velvetinvestment.domain.usecases.loan.DeleteLoanUseCase
import org.sharad.velvetinvestment.shared.domain.models.UserLoanDomain
import org.sharad.velvetinvestment.shared.domain.usecase.GetUserDataUseCase
import org.sharad.velvetinvestment.utils.AppEventsController
import org.sharad.velvetinvestment.utils.SnackBarController
import org.sharad.velvetinvestment.utils.UiState
import org.sharad.velvetinvestment.utils.networking.onError
import org.sharad.velvetinvestment.utils.networking.onSuccess

/**
 * The user's loans, read from `GET /user/` (`user_loan`) — the same call home uses — rather than
 * the paged `/user-loan` list. Delete still goes through `/user-loan/{id}`.
 */
class LoanInfoViewModel(
    private val getUserDataUseCase: GetUserDataUseCase,
    private val deleteLoanUseCase: DeleteLoanUseCase
): ViewModel() {

    private val _loans = MutableStateFlow<UiState<List<UserLoanDomain>>>(UiState.Loading)
    val loans = _loans.asStateFlow()

    init {
        loadLoans()
    }

    fun loadLoans() {
        viewModelScope.launch {
            _loans.value = UiState.Loading
            getUserDataUseCase()
                .onSuccess { user -> _loans.value = UiState.Success(user.loans) }
                .onError { _loans.value = UiState.Error(it.message) }
        }
    }

    fun deleteLoan(loanId: String) {
        viewModelScope.launch {
            deleteLoanUseCase(loanId)
                .onSuccess {
                    SnackBarController.showSuccess("Loan deleted successfully")
                    AppEventsController.sendLoanRefreshEvent()
                }
                .onError {
                    SnackBarController.showError(it.message)
                }
        }
    }
}
