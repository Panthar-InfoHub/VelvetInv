package org.sharad.velvetinvestment.presentation.insurance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.sharad.velvetinvestment.features.fireonboarding.presentation.firereport.viewmodel.InsuranceFlowDetails
import org.sharad.velvetinvestment.shared.domain.usecase.GetUserDataUseCase
import org.sharad.velvetinvestment.utils.UiState
import org.sharad.velvetinvestment.utils.calculateRecommendedHealthInsurance
import org.sharad.velvetinvestment.utils.calculateRecommendedTermLifeInsurance
import org.sharad.velvetinvestment.utils.networking.onError
import org.sharad.velvetinvestment.utils.networking.onSuccess


class InsuranceScreenViewModel(
    private val getUserDataUseCase: GetUserDataUseCase
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

            getUserDataUseCase()
                .onError { error ->
                    _insuranceInfo.value = UiState.Error(error.message)
                }
                .onSuccess { user ->

                    val monthlyIncome = user.finance?.monthlyIncome ?: 0.0
                    // Same sum of the four expense heads as before (it was never multiplied by 12).
                    val totalExpenses = (user.finance?.monthlyExpenses ?: 0L).toDouble()
                    val totalLiabilities = user.totalLoanOutstanding.toDouble()

                    _insuranceInfo.value = UiState.Success(
                        InsuranceFlowDetails(
                            healthInsurance = user.insurance?.healthInsurance,
                            lifeInsurance = user.insurance?.lifeInsurance
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