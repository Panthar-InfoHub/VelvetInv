package org.sharad.velvetinvestment.presentation.loans.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.sharad.velvetinvestment.data.remote.model.loan.UpdateLoanRequest
import org.sharad.velvetinvestment.data.remote.model.onboarding.Loan
import org.sharad.velvetinvestment.domain.LoanTypes
import org.sharad.velvetinvestment.domain.usecases.loan.AddSingleLoanUseCase
import org.sharad.velvetinvestment.domain.usecases.loan.GetLoanByIdUseCase
import org.sharad.velvetinvestment.domain.usecases.loan.UpdateSingleLoanUseCase
import org.sharad.velvetinvestment.utils.AppEventsController
import org.sharad.velvetinvestment.utils.SnackBarController
import org.sharad.velvetinvestment.utils.networking.onError
import org.sharad.velvetinvestment.utils.networking.onSuccess

class AddLoanViewModel(
    private val loanId: String?,
    private val getLoanByIdUseCase: GetLoanByIdUseCase,
    private val addSingleLoanUseCase: AddSingleLoanUseCase,
    private val updateSingleLoanUseCase: UpdateSingleLoanUseCase
) : ViewModel() {

    private val _loanType = MutableStateFlow<LoanTypes?>(null)
    val loanType = _loanType.asStateFlow()

    private val _outstandingAmount = MutableStateFlow<Long?>(null)
    val outstandingAmount = _outstandingAmount.asStateFlow()

    private val _monthlyEmi = MutableStateFlow<Long?>(null)
    val monthlyEmi = _monthlyEmi.asStateFlow()

    private val _tenure = MutableStateFlow<Int?>(null)
    val tenure = _tenure.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    fun loadLoan(id: String) {
        viewModelScope.launch {
            _loading.value = true
            getLoanByIdUseCase(id).onSuccess { loan ->
                _loanType.value =
                    LoanTypes.entries.find { it.displayName == loan.loan_type } ?: LoanTypes.OTHER
                _outstandingAmount.value = loan.outstanding_amount.toLongOrNull()
                _monthlyEmi.value = loan.monthly_emi.toLongOrNull()
                _tenure.value = loan.tenure_months
                _loading.value = false
            }.onError {
                _loading.value = false
            }
        }
    }

    fun onLoanTypeUpdate(type: LoanTypes) {
        _loanType.value = type
    }

    fun onOutstandingAmountUpdate(amount: String) {
        _outstandingAmount.value = amount.toLongOrNull()
    }

    fun onMonthlyEmiUpdate(emi: String) {
        _monthlyEmi.value = emi.toLongOrNull()
    }

    fun onTenureUpdate(tenure: String) {
        _tenure.value = tenure.toIntOrNull()
    }

    fun submit(onSuccess: () -> Unit) {
        val type = _loanType.value ?: return
        val outstanding = _outstandingAmount.value ?: return
        val emi = _monthlyEmi.value ?: return
        val tenureMonths = _tenure.value ?: return

        viewModelScope.launch {
            _loading.value = true

            if (loanId != null) {
                // Update existing loan
                val request = UpdateLoanRequest(
                    loan_type = type.displayName,
                    loan_name = type.displayName,
                    outstanding_amount = outstanding,
                    monthly_emi = emi,
                    tenure_months = tenureMonths
                )
                updateSingleLoanUseCase(loanId, request)
                    .onSuccess {
                        SnackBarController.showSuccess("Loan updated successfully")
                        AppEventsController.sendLoanRefreshEvent()
                        onSuccess()
                    }
                    .onError {
                        SnackBarController.showError(it.message)
                    }
                _loading.value = false
            } else {
                // Add new loan using specialized POST endpoint
                val currentLoan = Loan(
                    loan_name = type.displayName,
                    loan_type = type.displayName,
                    monthly_emi = emi,
                    outstanding_amount = outstanding,
                    tenure_months = tenureMonths
                )

                addSingleLoanUseCase(currentLoan)
                    .onSuccess {
                        SnackBarController.showSuccess("Loan added successfully")
                        AppEventsController.sendLoanRefreshEvent()
                        onSuccess()
                    }
                    .onError {
                        SnackBarController.showError(it.message)
                    }
                _loading.value = false
            }
        }
    }

    fun clearLoan() {
        _loanType.value = null
        _outstandingAmount.value = null
        _monthlyEmi.value = null
        _tenure.value = null
    }
}
