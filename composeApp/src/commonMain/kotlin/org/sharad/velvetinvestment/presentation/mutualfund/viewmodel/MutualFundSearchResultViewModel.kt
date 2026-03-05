package org.sharad.velvetinvestment.presentation.mutualfund.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.sharad.velvetinvestment.domain.usecases.fundusecases.GetMutualFundSearchResultUseCase
import org.sharad.velvetinvestment.presentation.mutualfund.MutualFundUI
import org.sharad.velvetinvestment.presentation.mutualfund.toUI
import org.sharad.velvetinvestment.utils.FundFilter
import org.sharad.velvetinvestment.utils.LoadingState
import org.sharad.velvetinvestment.utils.fundfiltersystem.InvestmentFilter
import org.sharad.velvetinvestment.utils.fundfiltersystem.createInitialInvestmentFilter
import org.sharad.velvetinvestment.utils.networking.onError
import org.sharad.velvetinvestment.utils.networking.onSuccess
import org.sharad.velvetinvestment.utils.networking.toMessage

class MutualFundSearchResultViewModel(
    private val id: String,
    private val getMutualFundSearchResultUseCase: GetMutualFundSearchResultUseCase
) : ViewModel() {

    private val _loadingState = MutableStateFlow<LoadingState>(LoadingState.Loading)
    val loadingState: StateFlow<LoadingState> = _loadingState.asStateFlow()

    private val _mutualFunds = MutableStateFlow<List<MutualFundUI>>(emptyList())
    val mutualFunds: StateFlow<List<MutualFundUI>> = _mutualFunds.asStateFlow()

    private val _selectedYear = MutableStateFlow<Int>(3)
    val selectedYear: StateFlow<Int> = _selectedYear

    val sortedFunds: StateFlow<List<MutualFundUI>> =
        combine(_mutualFunds, _selectedYear) { funds, year ->

            funds.sortedByDescending {
                it.returnYear == year
            }

        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    private val _selectedFilter = MutableStateFlow<FundFilter?>(null)
    val selectedFilter: StateFlow<FundFilter?> = _selectedFilter

    private val _showFilterScreen = MutableStateFlow(false)
    val showFilterScreen: StateFlow<Boolean> = _showFilterScreen


    private val _filterState = MutableStateFlow<InvestmentFilter>(createInitialInvestmentFilter())
    val filterState: StateFlow<InvestmentFilter> = _filterState



    init {
        loadFunds()
    }

    private fun loadFunds() {
        viewModelScope.launch {
            _loadingState.value = LoadingState.Loading
            getMutualFundSearchResultUseCase(id)
                .onSuccess { data ->
                    _mutualFunds.value =
                        data.map { it.toUI() }
                    _loadingState.value = LoadingState.Success
                }
                .onError { error ->
                    _loadingState.value =
                        LoadingState.Error(error.toMessage())
                }
        }
    }

    fun onFilterSelected(filter: FundFilter) {
        _selectedFilter.value =
            if (_selectedFilter.value == filter) null
            else filter
    }

    fun incrementYear(){
        if (_selectedYear.value==5) return
        _selectedYear.value=_selectedYear.value.plus(1)
    }

    fun decrementYear(){
        if (_selectedYear.value==1) return
        _selectedYear.value=_selectedYear.value.minus(1)
    }

    fun applyFilter(newFilter: InvestmentFilter) {
        _filterState.value = newFilter
    }

    fun clearFilter() {
        _filterState.value = createInitialInvestmentFilter()
    }

    fun toggleFilterScreen() {
        _showFilterScreen.value = !_showFilterScreen.value
    }

}


val defaultFilters = listOf(
    FundFilter.IndexOnly,
    FundFilter.FlexiCap,
    FundFilter.Sectoral,
    FundFilter.LargeCap
)
