package org.sharad.velvetinvestment.presentation.loans.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.sharad.velvetinvestment.domain.models.loan.LoanDomain
import org.sharad.velvetinvestment.domain.usecases.loan.DeleteLoanUseCase
import org.sharad.velvetinvestment.domain.usecases.loan.GetLoansUseCase
import org.sharad.velvetinvestment.utils.AppEventsController
import org.sharad.velvetinvestment.utils.SnackBarController
import org.sharad.velvetinvestment.utils.UiState
import org.sharad.velvetinvestment.utils.networking.onError
import org.sharad.velvetinvestment.utils.networking.onSuccess

class LoanInfoViewModel(
    private val getLoans: GetLoansUseCase,
    private val deleteLoanUseCase: DeleteLoanUseCase
): ViewModel() {

    private val _loans = MutableStateFlow<UiState<List<LoanDomain>>>(UiState.Loading)
    val loans = _loans.asStateFlow()

    private var currentPage = 1
    private val _hasNextPage = MutableStateFlow(false)
    val hasNextPage = _hasNextPage.asStateFlow()

    private val _isLoadingNext = MutableStateFlow(false)
    val isLoadingNext = _isLoadingNext.asStateFlow()

    init {
        loadLoans()
    }

    fun loadLoans() {
        viewModelScope.launch {
            _loans.value = UiState.Loading
            getLoans(page = 1, limit = 20)
                .onSuccess { res ->
                    currentPage = res.page
                    _hasNextPage.value = res.hasNextPage
                    _loans.value = UiState.Success(res.items)
                }
                .onError {
                    _loans.value = UiState.Error(it.message)
                }
        }
    }

    fun loadNext() {
        if (!_hasNextPage.value || _isLoadingNext.value) return
        val currentList = (_loans.value as? UiState.Success)?.data ?: return

        viewModelScope.launch {
            _isLoadingNext.value = true
            getLoans(page = currentPage + 1, limit = 20)
                .onSuccess { res ->
                    currentPage = res.page
                    _hasNextPage.value = res.hasNextPage
                    _loans.value = UiState.Success(currentList + res.items)
                }
                .onError {
                    SnackBarController.showError(it.message)
                }
            _isLoadingNext.value = false
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
