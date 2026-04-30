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
import org.sharad.velvetinvestment.data.remote.mapper.PayoutType
import org.sharad.velvetinvestment.data.remote.model.purchasefd.PurchaseFDBodyDto
import org.sharad.velvetinvestment.domain.models.fd.FDTenureDomain
import org.sharad.velvetinvestment.domain.usecases.LaunchBrowserUseCase
import org.sharad.velvetinvestment.domain.usecases.fixeddepositusecases.GetFDDetailsUseCase
import org.sharad.velvetinvestment.domain.usecases.fixeddepositusecases.PurchaseFDUseCase
import org.sharad.velvetinvestment.presentation.fixeddeposits.uimodels.FDPurchaseUiModel
import org.sharad.velvetinvestment.utils.SnackBarController
import org.sharad.velvetinvestment.utils.UiState
import org.sharad.velvetinvestment.utils.networking.onError
import org.sharad.velvetinvestment.utils.networking.onSuccess

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

    val buttonEnabled = combine(uiState) { states ->
        val state = states[0]

        if (state is UiState.Success) {
            val data = state.data

            data.selectedTenure != null &&
                    data.amount != null &&
                    data.amount > 0 &&
                    data.amount >= data.minAmount &&
                    !data.showError
        } else {
            false
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        false
    )

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

                    val frequencies = data.payoutOptions

                    val defaultTenure = tenures.find { it.isDefault } ?: tenures.firstOrNull()

                    _uiState.value = UiState.Success(
                        FDPurchaseUiModel(
                            tenures = emptyList(),
                            frequencies = frequencies,
                            amount = data.minDeposit,
                            amountInput = data.minDeposit.toString(),
                            minAmount = data.minDeposit,
                            bankName = data.bankName,
                            bankLogo = data.bankLogo,
                            riskLabel = data.riskLabel,
                            highestInterestRate = data.maxInterestRate,
                            id=data.id
                        )
                    )
                }
                .onError {
                    _uiState.value = UiState.Error(it.message)
                }
        }
    }

    fun updateFrequency(frequency: PayoutType) {
        _uiState.update { state ->
            if (state is UiState.Success) {

                val filtered = allTenures.filter {
                    it.payoutFrequency == frequency
                }


                state.copy(
                    data = state.data.copy(
                        frequencies = state.data.frequencies,
                        selectedFrequency = frequency,
                        tenures = filtered,
                        selectedTenure = null
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

    fun updateAmount(input: String) {
        _uiState.update { state ->
            if (state is UiState.Success) {

                val parsedAmount = input.toLongOrNull()

                val hasInvalidNumber =
                    input.isNotBlank() && parsedAmount == null

                val isBelowMinAmount =
                    parsedAmount != null &&
                            parsedAmount < state.data.minAmount

                val errorText = when {
                    hasInvalidNumber -> "Please enter a valid amount"
                    isBelowMinAmount -> "Minimum amount is ₹${state.data.minAmount}"
                    else -> ""
                }

                state.copy(
                    data = state.data.copy(
                        amountInput = input,
                        amount = parsedAmount,
                        showError = hasInvalidNumber || isBelowMinAmount,
                        errorText = errorText
                    )
                )
            } else {
                state
            }
        }
    }

    fun purchaseFD(onSuccess: () -> Unit) {
        viewModelScope.launch {

            val currentState = _uiState.value

            if (currentState !is UiState.Success) return@launch

            val data = currentState.data
            val selectedTenure = data.selectedTenure ?: return@launch
            val selectedFrequency = data.selectedFrequency ?: return@launch
            val amount = data.amount ?: return@launch

            _uiState.update {
                if (it is UiState.Success) {
                    it.copy(data = it.data.copy(loading = true))
                } else it
            }

            val request = PurchaseFDBodyDto(
                investment_amount = amount,
                investment_period = selectedTenure.tenureDays,
                payout_frequency = selectedFrequency.id,
                product_id = data.id,
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

                    SnackBarController.showError(error.message)

                }
        }
    }
}