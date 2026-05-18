package org.sharad.velvetinvestment.presentation.investmentrate.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.sharad.velvetinvestment.domain.models.user.InvestmentRateDomain
import org.sharad.velvetinvestment.domain.usecases.userfinance.GetInvestmentRateDataUseCase
import org.sharad.velvetinvestment.utils.UiState
import org.sharad.velvetinvestment.utils.networking.onError
import org.sharad.velvetinvestment.utils.networking.onSuccess

class InvestmentRateScreenViewModel(
    private val getInvestmentRateDataUseCase: GetInvestmentRateDataUseCase
) : ViewModel() {

    private val _investmentRateState =
        MutableStateFlow<UiState<InvestmentRateDomain>>(value = UiState.Loading)

    private val _investmentBreakdownExpanded = MutableStateFlow(false)
    val investmentBreakdownExpanded = _investmentBreakdownExpanded.asStateFlow()
    private val _essentialsBreakdownExpanded = MutableStateFlow(false)
    val essentialsBreakdownExpanded = _essentialsBreakdownExpanded.asStateFlow()
    val investmentRateState= _investmentRateState.asStateFlow()

    init {
        loadInvestmentRate()
    }

    fun loadInvestmentRate(){
        viewModelScope.launch {
            _investmentRateState.value= UiState.Loading
            getInvestmentRateDataUseCase()
                .onSuccess {
                    _investmentRateState.value= UiState.Success(it)
                }
                .onError {
                    _investmentRateState.value= UiState.Error(it.message)
                }
        }
    }

    fun toggleInvestmentBreakdown(){
        _investmentBreakdownExpanded.value= !_investmentBreakdownExpanded.value
    }

    fun toggleEssentialsBreakdown(){
        _essentialsBreakdownExpanded.value= !_essentialsBreakdownExpanded.value
    }

}