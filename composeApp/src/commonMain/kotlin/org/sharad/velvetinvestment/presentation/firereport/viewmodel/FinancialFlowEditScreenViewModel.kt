package org.sharad.velvetinvestment.presentation.firereport.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.sharad.velvetinvestment.data.remote.mapper.toFinanceDto
import org.sharad.velvetinvestment.data.remote.mapper.toFinancialFlowDetails
import org.sharad.velvetinvestment.data.remote.model.updateuserdata.FinanceUpdateDto
import org.sharad.velvetinvestment.domain.repository.UserAuth
import org.sharad.velvetinvestment.presentation.onboarding.models.ExpensePercentages
import org.sharad.velvetinvestment.presentation.onboarding.models.FinancialFlowDetails
import org.sharad.velvetinvestment.presentation.onboarding.models.FinancialSummary
import org.sharad.velvetinvestment.utils.SnackBarController
import org.sharad.velvetinvestment.utils.SnackBarType
import org.sharad.velvetinvestment.utils.UiState
import org.sharad.velvetinvestment.utils.formatMoneyWithUnits
import org.sharad.velvetinvestment.utils.networking.onError
import org.sharad.velvetinvestment.utils.networking.onSuccess
import org.sharad.velvetinvestment.utils.parseSafeLong

class FinancialFlowEditScreenViewModel(
    private val userAuth: UserAuth
): ViewModel() {

    private val _financialInfo= MutableStateFlow<UiState<FinancialFlowDetails>>(UiState.Loading)
    val financialInfo= _financialInfo.asStateFlow()

    private val _loading=MutableStateFlow(false)
    val loading= _loading.asStateFlow()

    val expensePercentages: StateFlow<ExpensePercentages> =
        financialInfo
            .map { data ->
                if (data !is UiState.Success)
                    return@map ExpensePercentages()
                val info=data.data
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


    val financialSummary = combine(_financialInfo) { data ->

        val main = data[0]

        if (main !is UiState.Success) {
            return@combine FinancialSummary(
                totalExpense = "₹0",
                monthlySurplus = "₹0",
                savingsRate = "0%"
            )
        }
        val info=main.data
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

    fun loadData() {
        viewModelScope.launch {
            _financialInfo.value = UiState.Loading

            userAuth.getUserData()
                .onError {
                    _financialInfo.value = UiState.Error(it.message)
                }
                .onSuccess {
                    val financialData = it.data.user_finance

                    _financialInfo.value = UiState.Success(
                        financialData.toFinancialFlowDetails()
                    )
                }
        }
    }

    init {
        loadData()
    }


    fun onAnnualIncomeUpdate(income: String) {
        val current = _financialInfo.value
        if (current is UiState.Success) {
            _financialInfo.value = UiState.Success(
                current.data.copy(
                    annualIncome = parseSafeLong(income, current.data.annualIncome)
                )
            )
        }
    }

    fun onHouseExpenseUpdate(expense: String) {
        val current = _financialInfo.value
        if (current is UiState.Success) {
            _financialInfo.value = UiState.Success(
                current.data.copy(
                    houseExpense = parseSafeLong(expense, current.data.houseExpense)
                )
            )
        }
    }

    fun onFoodExpenseUpdate(expense: String) {
        val current = _financialInfo.value
        if (current is UiState.Success) {
            _financialInfo.value = UiState.Success(
                current.data.copy(
                    foodExpense = parseSafeLong(expense, current.data.foodExpense)
                )
            )
        }
    }

    fun onTransportExpenseUpdate(expense: String) {
        val current = _financialInfo.value
        if (current is UiState.Success) {
            _financialInfo.value = UiState.Success(
                current.data.copy(
                    transportExpense = parseSafeLong(expense, current.data.transportExpense)
                )
            )
        }
    }

    fun onOtherExpenseUpdate(expense: String) {
        val current = _financialInfo.value
        if (current is UiState.Success) {
            _financialInfo.value = UiState.Success(
                current.data.copy(
                    otherExpense = parseSafeLong(expense, current.data.otherExpense)
                )
            )
        }
    }

    fun onSubmit(
        onSuccessful: () -> Unit
    ) {
        val current = _financialInfo.value
        if (current !is UiState.Success) return
        val data = current.data
        val dto = FinanceUpdateDto(
            finance = data.toFinanceDto()
        )
        viewModelScope.launch {
            _loading.value=true
            userAuth.updateFinance(dto)
                .onError {
                    _loading.value=false
                    SnackBarController.showSnackBar(SnackBarType.Error(it.message))
                }
                .onSuccess {
                    _loading.value=false
                    onSuccessful()
                }
        }
    }

}