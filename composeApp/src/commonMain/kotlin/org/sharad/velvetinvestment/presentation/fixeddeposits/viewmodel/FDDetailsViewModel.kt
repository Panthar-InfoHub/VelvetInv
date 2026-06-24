package org.sharad.velvetinvestment.presentation.fixeddeposits.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.sharad.velvetinvestment.data.remote.mapper.PayoutType
import org.sharad.velvetinvestment.domain.models.fd.FDDetailsDomain
import org.sharad.velvetinvestment.domain.models.fd.FdCustomerType
import org.sharad.velvetinvestment.domain.usecases.fixeddepositusecases.GetFDDetailsUseCase
import org.sharad.velvetinvestment.utils.Log
import org.sharad.velvetinvestment.utils.UiState
import org.sharad.velvetinvestment.utils.networking.onError
import org.sharad.velvetinvestment.utils.networking.onSuccess
import org.sharad.velvetinvestment.utils.trimDoubleTo
import kotlin.math.pow

class FDDetailsViewModel(
    private val id: String,
    private val getFDDetailsUseCase: GetFDDetailsUseCase
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<UiState<FDDetailsDomain>>(value = UiState.Loading)

    val uiState: StateFlow<UiState<FDDetailsDomain>> =
        _uiState.asStateFlow()

    private val _selectedCustomerType = MutableStateFlow<FdCustomerType>(FdCustomerType.STANDARD)
    val selectedCustomerType = _selectedCustomerType.asStateFlow()

    private val _activeSheet = MutableStateFlow<FDModalType?>(null)
    val activeSheet = _activeSheet.asStateFlow()

    fun openSheet(type: FDModalType) {
        _activeSheet.value = type
    }

    fun closeSheet() {
        _activeSheet.value = null
    }

    init {
        loadFDDetails()
    }

    fun loadFDDetails() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading

            getFDDetailsUseCase(id, selectedCustomerType.value.name)
                .onSuccess { data ->
                    _uiState.value = UiState.Success(data)
                }
                .onError { error ->
                    _uiState.value = UiState.Error(error.message)
                }
        }
    }

    fun updateInvest(amount: Long) {
        _uiState.update { state ->
            if (state is UiState.Success) {
                state.copy(
                    data = state.data.copy(
                        invest = amount
                    )
                )
            } else state
        }
        closeSheet()
    }

    fun updateInterestPayout(payout: PayoutType) {
        _uiState.update { state ->
            if (state is UiState.Success) {
                state.copy(
                    data = state.data.copy(
                        selectedPayout = payout
                    )
                )
            } else state
        }

        closeSheet()
    }

    fun updateApplicable(applicable: FdCustomerType) {
        _selectedCustomerType.value = applicable
        loadFDDetails()
        closeSheet()
    }

}

enum class FDModalType {
    PAYOUT,
    APPLICABLE,
    INVEST
}


fun calculateMaturity(
    principal: Long,
    rate: Double,
    days: Int,
    frequency: PayoutType
): Double {

    val years = days / 360.0

    return when (frequency) {

        PayoutType.Cumulative -> {
            val n = 4

            val base = 1 + (rate / 100) / n
            val exponent = n * years

            val maturity = principal * base.pow(exponent)

            Log("FD_CALC", "CUMULATIVE maturity=$maturity")

            maturity.trimDoubleTo(2)
        }

        PayoutType.Monthly,
        PayoutType.Quarterly,
        PayoutType.HalfYearly,
        PayoutType.Yearly,
        is PayoutType.Custom -> {

            val totalInterest = principal * (rate / 100) * years

            val maturity = principal + totalInterest

            Log("FD_CALC", "NON_CUMULATIVE maturity=$maturity")

            maturity.trimDoubleTo(2)
        }
    }
}