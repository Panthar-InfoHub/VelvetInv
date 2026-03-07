package org.sharad.velvetinvestment.presentation.fixeddeposits.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.sharad.velvetinvestment.domain.usecases.fixeddepositusecases.GetFixedDepositsSearchResultUseCase
import org.sharad.velvetinvestment.presentation.fixeddeposits.uimodels.FixedDepositUIModel
import org.sharad.velvetinvestment.presentation.fixeddeposits.uimodels.toUI
import org.sharad.velvetinvestment.utils.LabelFilter
import org.sharad.velvetinvestment.utils.LoadingState
import org.sharad.velvetinvestment.utils.fundfiltersystem.InvestmentFilter
import org.sharad.velvetinvestment.utils.fundfiltersystem.createInitialFDFilters
import org.sharad.velvetinvestment.utils.fundfiltersystem.createInitialInvestmentFilter
import org.sharad.velvetinvestment.utils.networking.onError
import org.sharad.velvetinvestment.utils.networking.onSuccess
import org.sharad.velvetinvestment.utils.networking.toMessage

class FDSearchResultViewModel(
    private val id: String,
    private val getFDSearchResult: GetFixedDepositsSearchResultUseCase,
) : ViewModel() {

    private val _loadingState=MutableStateFlow<LoadingState>(LoadingState.Loading)
    val loadingState= _loadingState.asStateFlow()

    private val _fixedDeposits = MutableStateFlow<List<FixedDepositUIModel>>(emptyList())
    val fixedDeposits: StateFlow<List<FixedDepositUIModel>> = _fixedDeposits.asStateFlow()

    private val _selectedYear = MutableStateFlow<Int>(3)
    val selectedYear: StateFlow<Int> = _selectedYear

    val sortedFD: StateFlow<List<FixedDepositUIModel>> =
        combine(_fixedDeposits, _selectedYear) { funds, year ->

            val selectedDays = year * 365

            funds.mapNotNull { fd ->

                val filteredTenures = fd.tenures.filter {
                    selectedDays in it.minDays..it.maxDays
                }

                if (filteredTenures.isEmpty()) {
                    null
                } else {
                    fd.copy(tenures = filteredTenures)
                }
            }

        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    private val _selectedFilter = MutableStateFlow<LabelFilter?>(null)
    val selectedFilter: StateFlow<LabelFilter?> = _selectedFilter

    private val _showFilterScreen = MutableStateFlow(false)
    val showFilterScreen: StateFlow<Boolean> = _showFilterScreen


    private val _filterState = MutableStateFlow<InvestmentFilter>(createInitialFDFilters())
    val filterState: StateFlow<InvestmentFilter> = _filterState



    init {
        loadFunds()
    }

    private fun loadFunds() {
        viewModelScope.launch {
            _loadingState.value = LoadingState.Loading
            getFDSearchResult(id)
                .onSuccess { data ->
                    _fixedDeposits.value =
                        data.map { it.toUI() }
                    _loadingState.value = LoadingState.Success
                }
                .onError { error ->
                    _loadingState.value =
                        LoadingState.Error(error.toMessage())
                }
        }
    }

    fun onFilterSelected(filter: LabelFilter) {
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