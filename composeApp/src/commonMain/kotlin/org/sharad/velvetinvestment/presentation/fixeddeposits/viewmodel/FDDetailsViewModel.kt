package org.sharad.velvetinvestment.presentation.fixeddeposits.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.sharad.velvetinvestment.domain.models.fd.FDDetailsDomain
import org.sharad.velvetinvestment.domain.usecases.fixeddepositusecases.GetFDDetailsUseCase
import org.sharad.velvetinvestment.utils.UiState
import org.sharad.velvetinvestment.utils.networking.onError
import org.sharad.velvetinvestment.utils.networking.onSuccess
import kotlin.math.pow

class FDDetailsViewModel(
    private val id: String,
    private val getFDDetailsUseCase: GetFDDetailsUseCase
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<UiState<FDDetailsDomain>>(value = UiState.Loading)

    val uiState: StateFlow<UiState<FDDetailsDomain>> =
        _uiState.asStateFlow()

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

            getFDDetailsUseCase(id)
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

    fun updateInterestPayout(payout: String) {
        _uiState.update { state ->
            if (state is UiState.Success) {
                state.copy(
                    data = state.data.copy(
                        interestPayout = payout
                    )
                )
            } else state
        }

        closeSheet()
    }

    fun updateApplicable(applicable: String) {
        _uiState.update { state ->
            if (state is UiState.Success) {
                state.copy(
                    data = state.data.copy(
                        applicable = applicable
                    )
                )
            } else state
        }

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
    days: Int
): Long {
    val years = days / 365.0
    val n = 4 // quarterly compounding

    val maturity = principal * (
        (1 + (rate / 100) / n).pow(n * years)
    )

    return maturity.toLong()
}