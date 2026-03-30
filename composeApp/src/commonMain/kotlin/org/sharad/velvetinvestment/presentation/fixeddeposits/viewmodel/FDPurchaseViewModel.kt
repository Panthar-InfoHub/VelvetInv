package org.sharad.velvetinvestment.presentation.fixeddeposits.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.sharad.velvetinvestment.data.remote.model.purchasefd.PurchaseFDBodyDto
import org.sharad.velvetinvestment.domain.models.fd.FDTenureDomain
import org.sharad.velvetinvestment.domain.usecases.LaunchBrowserUseCase
import org.sharad.velvetinvestment.domain.usecases.fixeddepositusecases.GetFDDetailsUseCase
import org.sharad.velvetinvestment.domain.usecases.fixeddepositusecases.PurchaseFDUseCase
import org.sharad.velvetinvestment.presentation.fixeddeposits.uimodels.FDPurchaseUiModel
import org.sharad.velvetinvestment.utils.SnackBarController
import org.sharad.velvetinvestment.utils.SnackBarType
import org.sharad.velvetinvestment.utils.UiState
import org.sharad.velvetinvestment.utils.networking.onError
import org.sharad.velvetinvestment.utils.networking.onSuccess
import kotlin.math.pow

class FDPurchaseViewModel(
    private val id: String,
    private val getFDDetailsUseCase: GetFDDetailsUseCase,
    private val purchaseFDUseCase: PurchaseFDUseCase,
    private val browserLaunchUseCase: LaunchBrowserUseCase
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<UiState<FDPurchaseUiModel>>(UiState.Loading)

    val uiState: StateFlow<UiState<FDPurchaseUiModel>> =
        _uiState.asStateFlow()

    private var allTenures: List<FDTenureDomain> = emptyList()

    val buttonEnabled= combine(uiState){
        val data=it[0]

        data is UiState.Success && data.data.selectedTenure!=null
                && data.data.amount>=data.data.minAmount
                && data.data.amount.toString().isNotBlank()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)


    init {
        loadFDDetails()
    }

    private fun loadFDDetails() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading

            getFDDetailsUseCase(id)
                .onSuccess { data ->

                    val tenures = data.interestRates

                    allTenures = tenures

                    val frequencies = data.interestRates
                        .map { it.payoutFrequency }
                        .distinct()

                    val defaultTenure = tenures.find { it.isDefault } ?: tenures.firstOrNull()

                    _uiState.value = UiState.Success(
                        FDPurchaseUiModel(
                            tenures = emptyList(),
                            frequencies = frequencies,
                            amount = data.minDeposit,
                            minAmount = data.minDeposit,
                            bankName = data.bankName,
                            bankLogo = data.bankLogo,
                            riskLabel = data.riskLabel,
                            highestInterestRate = data.maxInterestRate,
                        )
                    )
                }
                .onError {
                    _uiState.value = UiState.Error(it.message)
                }
        }
    }

    fun updateFrequency(frequency: String) {
        _uiState.update { state ->
            if (state is UiState.Success) {

                val filtered = allTenures.filter {
                    it.payoutFrequency == frequency
                }


                state.copy(
                    data = state.data.copy(
                        frequencies = state.data.frequencies,
                        selectedFrequency = frequency,
                        tenures = filtered
                    )
                )
            } else state
        }
    }

    fun updateTenure(tenure: FDTenureDomain) {
        _uiState.update { state ->
            if (state is UiState.Success) {
                state.copy(
                    data = state.data.copy(
                        selectedTenure = tenure
                    )
                )
            } else state
        }
    }

    fun updateAmount(amount: Long) {
        _uiState.update { state ->
            if (state is UiState.Success) {
                state.copy(
                    data = state.data.copy(
                        amount = amount
                    )
                )
            } else state
        }
    }

    fun purchaseFD(onSuccess: () -> Unit) {
        viewModelScope.launch {

            val currentState = _uiState.value

            if (currentState !is UiState.Success) return@launch

            val data = currentState.data
            val selectedTenure = data.selectedTenure ?: return@launch
            val selectedFrequency = data.selectedFrequency ?: return@launch

            _uiState.update {
                if (it is UiState.Success) {
                    it.copy(data = it.data.copy(loading = true))
                } else it
            }

            val request = PurchaseFDBodyDto(
                investment_amount = data.amount,
                investment_period = selectedTenure.tenureDays,
                payout_frequency = selectedFrequency,
                product_id = id,
                tenure = selectedTenure.tenureDays
            )

            purchaseFDUseCase(request)
                .onSuccess { response ->

                    _uiState.update {
                        if (it is UiState.Success) {
                            it.copy(data = it.data.copy(loading = false))
                        } else it
                    }
                    browserLaunchUseCase(response)
                    onSuccess()
                }
                .onError { error ->

                    _uiState.update {
                        if (it is UiState.Success) {
                            it.copy(data = it.data.copy(loading = false))
                        } else it
                    }

                    SnackBarController.showSnackBar(SnackBarType.Error(error.message))

                }
        }
    }
}

fun calculateMaturityAmount(
    amount: Long,
    rate: Double,
    days: Int
): Long {
    val years = days / 365.0
    val n = 4 // quarterly compounding

    return (amount * (1 + (rate / 100) / n).pow(n * years)).toLong()
}

fun calculateInterestEarned(
    maturity: Long,
    principal: Long
): Long {
    return maturity - principal
}