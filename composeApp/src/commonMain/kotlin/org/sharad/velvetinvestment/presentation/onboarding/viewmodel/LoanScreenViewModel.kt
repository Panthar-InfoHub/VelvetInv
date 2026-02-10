package org.sharad.velvetinvestment.presentation.onboarding.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.sharad.velvetinvestment.domain.LoanTypes
import org.sharad.velvetinvestment.presentation.onboarding.models.LoanInfo
import org.sharad.velvetinvestment.utils.parseSafeLong

class LoanScreenViewModel: ViewModel() {

    private val _loanList= MutableStateFlow<List<LoanInfo>>(emptyList())
    val loanList=_loanList.asStateFlow()

    private val _addLoanDetails= MutableStateFlow<LoanInfo>(LoanInfo())
    val addLoanDetails=_addLoanDetails.asStateFlow()

    val totalOutstandingDebt= _loanList.map { list->
            list.sumOf {info-> info.outstandingAmount?:0L  }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = 0L
    )

    val monthlyEMIBurden= _loanList.map { list->
        list.sumOf {info-> info.monthlyEmi?:0L  }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = 0L
    )

    val totalTenure= _loanList.map {
        list-> list.sumOf { info-> info.tenure?:0 }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = 0
    )

    fun onLoanTypeUpdate(type: LoanTypes) {
        _addLoanDetails.value = _addLoanDetails.value.copy(
            loanType = type
        )
    }

    fun onOutstandingAmountUpdate(amount: String) {
        _addLoanDetails.value = _addLoanDetails.value.let { current ->
            current.copy(
                outstandingAmount = parseSafeLong(amount, current.outstandingAmount)
            )
        }
    }

    fun onMonthlyEmiUpdate(emi: String) {
        _addLoanDetails.value = _addLoanDetails.value.let { current ->
            current.copy(
                monthlyEmi = parseSafeLong(emi, current.monthlyEmi)
            )
        }
    }

    fun onTenureUpdate(tenure: String) {
        _addLoanDetails.value = _addLoanDetails.value.copy(
            tenure = tenure.toIntOrNull()
        )
    }

    fun addLoan() {
        _loanList.value = _loanList.value + _addLoanDetails.value
        _addLoanDetails.value = LoanInfo()
    }

    fun clearLoan() {
        _addLoanDetails.value = LoanInfo()

    }

}