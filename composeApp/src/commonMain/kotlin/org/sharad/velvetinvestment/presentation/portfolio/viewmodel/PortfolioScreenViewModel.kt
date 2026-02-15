package org.sharad.velvetinvestment.presentation.portfolio.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.sharad.velvetinvestment.domain.usecases.fdusecases.GetFDListUseCase
import org.sharad.velvetinvestment.domain.usecases.fundusecases.GetMutualFundDashboardUseCase
import org.sharad.velvetinvestment.domain.usecases.fundusecases.GetMutualFundsUseCase
import org.sharad.velvetinvestment.presentation.portfolio.models.FDCardData
import org.sharad.velvetinvestment.presentation.portfolio.models.FundListCardData
import org.sharad.velvetinvestment.presentation.portfolio.models.MutualFundDashBoardData
import org.sharad.velvetinvestment.presentation.portfolio.models.SelectedPortfolio
import org.sharad.velvetinvestment.utils.UIState
import org.sharad.velvetinvestment.utils.networking.onError
import org.sharad.velvetinvestment.utils.networking.onSuccess

class PortfolioScreenViewModel(
    private val getMutualFundsUseCase: GetMutualFundsUseCase,
    private val getMutualFundDashboardUseCase: GetMutualFundDashboardUseCase,
    private val getFDListUseCase: GetFDListUseCase
) : ViewModel() {



    private val _selectedTab =
        MutableStateFlow<SelectedPortfolio>(SelectedPortfolio.MutualFunds)
    val selectedTab = _selectedTab.asStateFlow()

    private val _uiState =
        MutableStateFlow<UIState>(UIState.Loading)
    val uiState = _uiState.asStateFlow()


    private val _mutualFunds =
        MutableStateFlow<List<FundListCardData>>(emptyList())
    val mutualFunds = _mutualFunds.asStateFlow()

    private val _dashboard =
        MutableStateFlow<MutualFundDashBoardData?>(null)
    val dashboard = _dashboard.asStateFlow()

    private val _fds =
        MutableStateFlow<List<FDCardData>>(emptyList())
    val fds = _fds.asStateFlow()

    init {
        loadPortfolio()
    }


    private fun loadPortfolio() {
        viewModelScope.launch {

            _uiState.value = UIState.Loading

            val mfResult = getMutualFundsUseCase()
            val dashboardResult = getMutualFundDashboardUseCase()
            val fdResult = getFDListUseCase()

            var hasError = false

            mfResult
                .onSuccess {
                    _mutualFunds.value = it
                }
                .onError {
                    hasError = true
                    _uiState.value = UIState.Error(it.name)
                }

            dashboardResult
                .onSuccess { _dashboard.value = it }
                .onError {
                    hasError = true
                    _uiState.value = UIState.Error(it.name)
                }

            fdResult
                .onSuccess { _fds.value = it }
                .onError {
                    hasError = true
                    _uiState.value = UIState.Error(it.name)
                }

            if (!hasError) {
                _uiState.value = UIState.Success
            }
        }
    }

    fun onTabSelected(tab: SelectedPortfolio) {
        _selectedTab.value = tab
    }
}

