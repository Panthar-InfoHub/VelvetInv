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

    private val _loanType = MutableStateFlow<LoanTypes?>(null)
    val loanType = _loanType.asStateFlow()

    private val _outstandingAmount = MutableStateFlow<Long?>(null)
    val outstandingAmount = _outstandingAmount.asStateFlow()

    private val _monthlyEmi = MutableStateFlow<Long?>(null)
    val monthlyEmi = _monthlyEmi.asStateFlow()

    private val _tenure = MutableStateFlow<Int?>(null)
    val tenure = _tenure.asStateFlow()

    val totalOutstandingDebt= _loanList.map { list->
            list.sumOf {info-> info.outstandingAmount }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = 0L
    )

    val monthlyEMIBurden= _loanList.map { list->
        list.sumOf {info-> info.monthlyEmi  }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = 0L
    )

    val totalTenure= _loanList.map {
        list-> list.sumOf { info-> info.tenure }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = 0
    )

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

    fun addLoan() {

        val type = _loanType.value
        val amount = _outstandingAmount.value
        val emi = _monthlyEmi.value
        val tenureValue = _tenure.value

        if (type != null && amount != null && emi != null && tenureValue != null) {

            val loan = LoanInfo(
                loanType = type,
                outstandingAmount = amount,
                monthlyEmi = emi,
                tenure = tenureValue
            )

            _loanList.value = _loanList.value + loan
            clearLoan()
        }
    }

    fun clearLoan() {
        _loanType.value = null
        _outstandingAmount.value = null
        _monthlyEmi.value = null
        _tenure.value = null
    }

}