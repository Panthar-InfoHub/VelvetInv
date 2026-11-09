package org.sharad.velvetinvestment.features.loans.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.sharad.velvetinvestment.features.loans.data.remote.model.UpdateLoanRequest
import org.sharad.velvetinvestment.domain.LoanTypes
import org.sharad.velvetinvestment.features.loans.domain.usecase.UpdateSingleLoanUseCase
import org.sharad.velvetinvestment.features.fireonboarding.data.mapper.apiCode
import org.sharad.velvetinvestment.features.fireonboarding.data.mapper.loanTypeFromServer
import org.sharad.velvetinvestment.features.fireonboarding.data.mapper.toLoanInfo
import org.sharad.velvetinvestment.features.fireonboarding.data.mapper.toStepBody
import org.sharad.velvetinvestment.features.fireonboarding.domain.repository.FireOnboardingRepository
import org.sharad.velvetinvestment.features.fireonboarding.presentation.onboarding.models.LoanInfo
import org.sharad.velvetinvestment.shared.domain.usecase.GetUserDataUseCase
import org.sharad.velvetinvestment.utils.AppEventsController
import org.sharad.velvetinvestment.utils.SnackBarController
import org.sharad.velvetinvestment.utils.networking.NetworkResponse
import org.sharad.velvetinvestment.utils.networking.onError
import org.sharad.velvetinvestment.utils.networking.onSuccess

/**
 * Add or edit one loan.
 * - Add: `POST /onboarding/loans` (the FIRE onboarding loan step). That endpoint takes the whole
 *   loan list, so the loans already on `GET /user/` are sent along with the new one.
 * - Edit ([loanId] set): unchanged, `PATCH /user-loan/{id}`; the loan is read from `GET /user/`.
 */
class AddLoanViewModel(
    private val loanId: String?,
    private val getUserDataUseCase: GetUserDataUseCase,
    private val fireOnboardingRepository: FireOnboardingRepository,
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
            getUserDataUseCase()
                .onSuccess { user ->
                    val loan = user.loans.firstOrNull { it.id == id }
                    if (loan == null) {
                        SnackBarController.showError("Loan not found")
                    } else {
                        _loanType.value = loanTypeFromServer(loan.loanType)
                        _outstandingAmount.value = loan.outstandingAmount
                        _monthlyEmi.value = loan.monthlyEmi
                        _tenure.value = loan.tenureMonths
                    }
                }
                .onError { SnackBarController.showError(it.message) }
            _loading.value = false
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
        if (_loading.value) return
        val type = _loanType.value ?: return
        val outstanding = _outstandingAmount.value ?: return
        val emi = _monthlyEmi.value ?: return
        val tenureMonths = _tenure.value ?: return

        viewModelScope.launch {
            _loading.value = true
            if (loanId != null) {
                val request = UpdateLoanRequest(
                    loan_type = type.apiCode,
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
                    .onError { SnackBarController.showError(it.message) }
            } else {
                addLoan(LoanInfo(type, outstanding, emi, tenureMonths), onSuccess)
            }
            _loading.value = false
        }
    }

    private suspend fun addLoan(loan: LoanInfo, onSuccess: () -> Unit) {
        val existing = when (val user = getUserDataUseCase()) {
            is NetworkResponse.Success -> user.data.loans.map { it.toLoanInfo() }
            is NetworkResponse.Error -> {
                SnackBarController.showError(user.error.message)
                return
            }
        }
        fireOnboardingRepository.submitLoans((existing + loan).toStepBody())
            .onSuccess {
                SnackBarController.showSuccess("Loan added successfully")
                AppEventsController.sendLoanRefreshEvent()
                onSuccess()
            }
            .onError { SnackBarController.showError(it.message) }
    }

    fun clearLoan() {
        _loanType.value = null
        _outstandingAmount.value = null
        _monthlyEmi.value = null
        _tenure.value = null
    }
}
