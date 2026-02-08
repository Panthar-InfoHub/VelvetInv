package org.sharad.velvetinvestment.presentation.onboarding.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.sharad.velvetinvestment.presentation.onboarding.models.ExpensePercentages
import org.sharad.velvetinvestment.presentation.onboarding.models.FinancialFlowDetails
import org.sharad.velvetinvestment.presentation.onboarding.models.FinancialSummary
import org.sharad.velvetinvestment.utils.formatMoneyWithUnits
import org.sharad.velvetinvestment.utils.parseSafeLong

class FinancialFlowScreenViewModel: ViewModel() {

    private val _financialInfo= MutableStateFlow<FinancialFlowDetails>(FinancialFlowDetails())
    val financialInfo= _financialInfo.asStateFlow()

    val expensePercentages: StateFlow<ExpensePercentages> =
        financialInfo
            .map { info ->
                val house = info.houseExpense ?: 0L
                val food = info.foodExpense ?: 0L
                val transport = info.transportExpense ?: 0L
                val other = info.otherExpense ?: 0L
                val total = house + food + transport + other
                if (total == 0L) {
                    ExpensePercentages()
                } else {
                    ExpensePercentages(
                        housePercent = house.toFloat() / total * 100f,
                        foodPercent = food.toFloat() / total * 100f,
                        transportPercent = transport.toFloat() / total * 100f,
                        otherPercent = other.toFloat() / total * 100f,
                        totalExpense = total
                    )
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = ExpensePercentages()
            )


    val financialSummary = combine(_financialInfo) { list ->
        val info = list[0]
        val monthlyIncome = (info.annualIncome ?: 0) / 12
        val house = info.houseExpense ?: 0
        val food = info.foodExpense ?: 0
        val transport = info.transportExpense ?: 0
        val other = info.otherExpense ?: 0

        val totalExpense = house + food + transport + other
        val surplus = monthlyIncome - totalExpense

        val savingsRate = if (monthlyIncome > 0) {
            ((surplus * 100) / monthlyIncome)
        } else {
            0
        }

        FinancialSummary(
            totalExpense = "₹${formatMoneyWithUnits(totalExpense)}",
            monthlySurplus = "₹$surplus",
            savingsRate = "$savingsRate%"
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = FinancialSummary(
            totalExpense = "₹0",
            monthlySurplus ="₹0",
            savingsRate = "0%"
        )
    )


    fun onAnnualIncomeUpdate(income: String) {
        _financialInfo.value = _financialInfo.value.let { current ->
            current.copy(
                annualIncome = parseSafeLong(income, current.annualIncome)
            )
        }
    }

    fun onHouseExpenseUpdate(expense: String) {
        _financialInfo.value = _financialInfo.value.let { current ->
            current.copy(
                houseExpense = parseSafeLong(expense, current.houseExpense)
            )
        }
    }

    fun onFoodExpenseUpdate(expense: String) {
        _financialInfo.value = _financialInfo.value.let { current ->
            current.copy(
                foodExpense = parseSafeLong(expense, current.foodExpense)
            )
        }
    }

    fun onTransportExpenseUpdate(expense: String) {
        _financialInfo.value = _financialInfo.value.let { current ->
            current.copy(
                transportExpense = parseSafeLong(expense, current.transportExpense)
            )
        }
    }

    fun onOtherExpenseUpdate(expense: String) {
        _financialInfo.value = _financialInfo.value.let { current ->
            current.copy(
                otherExpense = parseSafeLong(expense, current.otherExpense)
            )
        }
    }


}