package org.sharad.velvetinvestment.presentation.portfolio.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.sharad.velvetinvestment.domain.models.portfolio.PortfolioDomain
import org.sharad.velvetinvestment.domain.usecases.fdportfoliousecases.GetFDListUseCase
import org.sharad.velvetinvestment.domain.usecases.fundusecases.GetMutualFundDashboardUseCase
import org.sharad.velvetinvestment.domain.usecases.fundusecases.GetPortfolioMutualFundsUseCase
import org.sharad.velvetinvestment.domain.usecases.userfinance.GetPortfolioUseCase
import org.sharad.velvetinvestment.presentation.portfolio.models.FDCardPortfolioData
import org.sharad.velvetinvestment.presentation.portfolio.models.FundListCardData
import org.sharad.velvetinvestment.presentation.portfolio.models.MutualFundDashBoardData
import org.sharad.velvetinvestment.presentation.portfolio.models.SelectedPortfolio
import org.sharad.velvetinvestment.utils.LoadingState
import org.sharad.velvetinvestment.utils.UiState
import org.sharad.velvetinvestment.utils.networking.onError
import org.sharad.velvetinvestment.utils.networking.onSuccess

class PortfolioScreenViewModel(
    private val getPortfolioUseCase: GetPortfolioUseCase
) : ViewModel() {



    private val _selectedTab =
        MutableStateFlow<SelectedPortfolio>(SelectedPortfolio.MutualFunds)
    val selectedTab = _selectedTab.asStateFlow()

    private val _loadingState =
        MutableStateFlow<UiState<PortfolioDomain>>(UiState.Loading)
    val uiState = _loadingState.asStateFlow()

    init {
        loadPortfolio()
    }


     fun loadPortfolio() {
        viewModelScope.launch {
            _loadingState.value= UiState.Loading
            getPortfolioUseCase()
                .onSuccess {
                    _loadingState.value= UiState.Success(it)
                }
                .onError {
                    _loadingState.value= UiState.Error(it.message)
                }
        }
    }

    fun onTabSelected(tab: SelectedPortfolio) {
        _selectedTab.value = tab
    }
}

