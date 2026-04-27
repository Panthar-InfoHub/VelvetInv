package org.sharad.velvetinvestment.presentation.insurance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.sharad.velvetinvestment.domain.repository.UserAuth
import org.sharad.velvetinvestment.presentation.firereport.viewmodel.InsuranceFlowDetails
import org.sharad.velvetinvestment.utils.UiState
import org.sharad.velvetinvestment.utils.calculateRecommendedHealthInsurance
import org.sharad.velvetinvestment.utils.calculateRecommendedTermLifeInsurance
import org.sharad.velvetinvestment.utils.networking.onError
import org.sharad.velvetinvestment.utils.networking.onSuccess


class InsuranceScreenViewModel(
    private val userAuth: UserAuth
): ViewModel() {

    private val _recommendedHealth = MutableStateFlow(0L)
    val recommendedHealth= _recommendedHealth.asStateFlow()

    private val _recommendedLife = MutableStateFlow(0L)
    val recommendedLife= _recommendedLife.asStateFlow()


    private val _insuranceInfo =
        MutableStateFlow<UiState<InsuranceFlowDetails>>(UiState.Loading)
    val insuranceInfo = _insuranceInfo.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    fun loadData() {
        viewModelScope.launch {
            _insuranceInfo.value = UiState.Loading

            userAuth.getUserData()
                .onError { error ->
                    _insuranceInfo.value = UiState.Error(error.message)
                }
                .onSuccess { response ->

                    val data = response.data
                    val insurance = data.user_insurance
                    val finance = data.user_finance

                    val annualIncome = finance.annual_income.toDoubleOrNull() ?: 0.0
                    val monthlyIncome = annualIncome / 12

                    val totalExpenses = listOf(
                        finance.expense_food,
                        finance.expense_others,
                        finance.expense_house,
                        finance.expense_transportation
                    ).sumOf { it.toDoubleOrNull() ?: 0.0 }

                    val totalLiabilities = data.user_loan.sumOf {
                        it.outstanding_amount.toDoubleOrNull() ?: 0.0
                    }

                    _insuranceInfo.value = UiState.Success(
                        InsuranceFlowDetails(
                            healthInsurance = insurance.health_insurance.toLong(),
                            lifeInsurance = insurance.life_insurance.toLong()
                        )
                    )

                    _recommendedHealth.value =
                        calculateRecommendedHealthInsurance(monthlyIncome)

                    _recommendedLife.value =
                        calculateRecommendedTermLifeInsurance(
                            monthlyIncome = monthlyIncome,
                            totalLiabilities = totalLiabilities,
                            totalAnnualExpenses = totalExpenses
                        )
                }
        }
    }

    init {
        loadData()
    }

}