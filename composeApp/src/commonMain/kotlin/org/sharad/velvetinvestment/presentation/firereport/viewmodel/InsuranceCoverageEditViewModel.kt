package org.sharad.velvetinvestment.presentation.firereport.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.sharad.velvetinvestment.data.remote.model.onboarding.Insurance
import org.sharad.velvetinvestment.data.remote.model.updateuserdata.InsuranceUpdateDto
import org.sharad.velvetinvestment.domain.repository.UserAuth
import org.sharad.velvetinvestment.utils.SnackBarController
import org.sharad.velvetinvestment.utils.SnackBarType
import org.sharad.velvetinvestment.utils.UiState
import org.sharad.velvetinvestment.utils.networking.onError
import org.sharad.velvetinvestment.utils.networking.onSuccess
import org.sharad.velvetinvestment.utils.parseSafeLong

data class InsuranceFlowDetails(
    val healthInsurance: Long? = null,
    val lifeInsurance: Long? = null
)

class InsuranceCoverageEditViewModel(
    private val userAuth: UserAuth
) : ViewModel() {

    private val _insuranceInfo =
        MutableStateFlow<UiState<InsuranceFlowDetails>>(UiState.Loading)
    val insuranceInfo = _insuranceInfo.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    val totalInsuranceAmount = insuranceInfo
        .map {
            if (it !is UiState.Success) return@map 0L
            val data = it.data
            (data.healthInsurance ?: 0L) + (data.lifeInsurance ?: 0L)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0L
        )

    // -------------------------------
    // LOAD DATA
    // -------------------------------
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

    // -------------------------------
    // UPDATE FUNCTIONS
    // -------------------------------

    fun onHealthInsuranceAmountChange(amount: String) {
        val current = _insuranceInfo.value
        if (current is UiState.Success) {
            _insuranceInfo.value = UiState.Success(
                current.data.copy(
                    healthInsurance = parseSafeLong(
                        amount,
                        current.data.healthInsurance
                    )
                )
            )
        }
    }

    fun onLifeInsuranceAmountChange(amount: String) {
        val current = _insuranceInfo.value
        if (current is UiState.Success) {
            _insuranceInfo.value = UiState.Success(
                current.data.copy(
                    lifeInsurance = parseSafeLong(
                        amount,
                        current.data.lifeInsurance
                    )
                )
            )
        }
    }

    // -------------------------------
    // SUBMIT
    // -------------------------------

    fun onSubmit(onSuccess: () -> Unit) {
        val current = _insuranceInfo.value
        if (current !is UiState.Success) return

        val data = current.data

        val dto = InsuranceUpdateDto(
            insurance = Insurance(
                health_insurance = data.healthInsurance ?: 0L,
                life_insurance = data.lifeInsurance ?: 0L
            )
        )

        viewModelScope.launch {
            _loading.value = true

            userAuth.updateInsurance(dto)
                .onError {
                    _loading.value = false
                    SnackBarController.showSnackBar(
                        SnackBarType.Error(it.message)
                    )
                }
                .onSuccess {
                    _loading.value = false
                    onSuccess()
                }
        }
    }
}