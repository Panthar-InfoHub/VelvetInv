package org.sharad.velvetinvestment.presentation.insurance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.sharad.velvetinvestment.domain.repository.UserAuth
import org.sharad.velvetinvestment.presentation.firereport.viewmodel.InsuranceFlowDetails
import org.sharad.velvetinvestment.utils.UiState
import org.sharad.velvetinvestment.utils.networking.onError
import org.sharad.velvetinvestment.utils.networking.onSuccess


class InsuranceScreenViewModel(
    private val userAuth: UserAuth
): ViewModel() {

    val recommendedHealth= 1000000L
    val recommendedLife= 1000000L


    private val _insuranceInfo =
        MutableStateFlow<UiState<InsuranceFlowDetails>>(UiState.Loading)
    val insuranceInfo = _insuranceInfo.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    fun loadData() {
        viewModelScope.launch {
            _insuranceInfo.value = UiState.Loading

            userAuth.getUserData()
                .onError {
                    _insuranceInfo.value = UiState.Error(it.message)
                }
                .onSuccess {
                    val insurance = it.data.user_insurance

                    _insuranceInfo.value = UiState.Success(
                        InsuranceFlowDetails(
                            healthInsurance = insurance.health_insurance.toLong(),
                            lifeInsurance = insurance.life_insurance.toLong()
                        )
                    )
                }
        }
    }

    init {
        loadData()
    }

}