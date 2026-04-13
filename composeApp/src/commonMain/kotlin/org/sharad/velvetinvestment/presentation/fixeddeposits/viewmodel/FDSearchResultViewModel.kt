package org.sharad.velvetinvestment.presentation.fixeddeposits.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.sharad.velvetinvestment.domain.models.fixeddeposits.FixedDepositDomain
import org.sharad.velvetinvestment.domain.usecases.fixeddepositusecases.GetFixedDepositsSearchResultUseCase
import org.sharad.velvetinvestment.utils.LabelFilter
import org.sharad.velvetinvestment.utils.LoadingState
import org.sharad.velvetinvestment.utils.SnackBarController
import org.sharad.velvetinvestment.utils.SnackBarType
import org.sharad.velvetinvestment.utils.fundfiltersystem.InvestmentFilter
import org.sharad.velvetinvestment.utils.fundfiltersystem.createInitialFDFilters
import org.sharad.velvetinvestment.utils.fundfiltersystem.createInitialInvestmentFilter
import org.sharad.velvetinvestment.utils.networking.onError
import org.sharad.velvetinvestment.utils.networking.onSuccess

class FDSearchResultViewModel(
    private val getFDSearchResult: GetFixedDepositsSearchResultUseCase,
) : ViewModel() {

    private val _loadingState=MutableStateFlow<LoadingState>(LoadingState.Loading)
    val loadingState= _loadingState.asStateFlow()

    private val _fixedDeposits = MutableStateFlow<List<FixedDepositDomain>>(emptyList())
    val fixedDeposits = _fixedDeposits.asStateFlow()

    private val _selectedYear = MutableStateFlow<Int>(3)
    val selectedYear: StateFlow<Int> = _selectedYear

    private val _selectedFilter = MutableStateFlow<LabelFilter?>(null)
    val selectedFilter: StateFlow<LabelFilter?> = _selectedFilter

    private val _showFilterScreen = MutableStateFlow(false)
    val showFilterScreen: StateFlow<Boolean> = _showFilterScreen


    private val _filterState = MutableStateFlow<InvestmentFilter>(createInitialFDFilters())
    val filterState: StateFlow<InvestmentFilter> = _filterState

    private var currentPage = 1
    private var hasNextPage = true
    private val _isLoadingNext = MutableStateFlow(false)
    val isLoadingNext = _isLoadingNext.asStateFlow()




    init {
        loadFunds(page=1,limit=30,tenure="3y")
    }

    fun loadFunds(page: Int, limit: Int, tenure: String) {
        viewModelScope.launch {
            _loadingState.value = LoadingState.Loading

            getFDSearchResult(page, limit, tenure)
                .onSuccess { data ->
                    currentPage = data.page
                    hasNextPage = data.hasNextPage

                    _fixedDeposits.value = data.items
                    _loadingState.value = LoadingState.Success
                }
                .onError { error ->
                    _loadingState.value = LoadingState.Error(error.message)
                }
        }
    }

    fun loadNext() {
        if (!hasNextPage || _isLoadingNext.value) return

        viewModelScope.launch {
            _isLoadingNext.value = true

            val nextPage = currentPage + 1

            getFDSearchResult(
                page = nextPage,
                limit = 30,
                tenure = "${_selectedYear.value}y"
            )
                .onSuccess { data ->
                    currentPage = data.page
                    hasNextPage = data.hasNextPage

                    _fixedDeposits.value =
                        _fixedDeposits.value + data.items
                }
                .onError {
                    SnackBarController.showSnackBar(SnackBarType.Error(it.message))
                }

            _isLoadingNext.value = false
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