package org.sharad.velvetinvestment.presentation.fixeddeposits.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.sharad.velvetinvestment.domain.models.fixeddeposits.FixedDepositDomain
import org.sharad.velvetinvestment.domain.usecases.fixeddepositusecases.GetFixedDepositsSearchResultUseCase
import org.sharad.velvetinvestment.utils.FDLabel
import org.sharad.velvetinvestment.utils.LabelFilter
import org.sharad.velvetinvestment.utils.LoadingState
import org.sharad.velvetinvestment.utils.SnackBarController
import org.sharad.velvetinvestment.utils.fundfiltersystem.FDFilterIds
import org.sharad.velvetinvestment.utils.fundfiltersystem.InvestmentFilter
import org.sharad.velvetinvestment.utils.fundfiltersystem.createInitialFDFilters
import org.sharad.velvetinvestment.utils.networking.onError
import org.sharad.velvetinvestment.utils.networking.onSuccess

class FDSearchResultViewModel(
    private val search: String?,
    private val getFDSearchResult: GetFixedDepositsSearchResultUseCase,
) : ViewModel() {

    private val _loadingState=MutableStateFlow<LoadingState>(LoadingState.Loading)
    val loadingState= _loadingState.asStateFlow()

    private val _searchText=MutableStateFlow("")
    val searchText= _searchText.asStateFlow()

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
        loadFunds()
    }

    fun loadFunds() {
        viewModelScope.launch {
            _loadingState.value = LoadingState.Loading

            val (tenure, payout) = getSelectedFilters()

            getFDSearchResult(
                page = 1,
                limit = 30,
                tenure = tenure,
                payoutFrequency = payout,
                search = search
            )
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
            val (tenure, payout) = getSelectedFilters()

            getFDSearchResult(
                page = nextPage,
                limit = 30,
                tenure = tenure ?: "${_selectedYear.value}y",
                payoutFrequency = payout,
                search = search
            )
                .onSuccess { data ->
                    currentPage = data.page
                    hasNextPage = data.hasNextPage

                    _fixedDeposits.value += data.items
                }
                .onError {
                    SnackBarController.showError(it.message)
                }

            _isLoadingNext.value = false
        }
    }
    fun onFilterSelected(filter: LabelFilter) {

        if (_selectedFilter.value == filter) {
            _selectedFilter.value = null
            clearFilter()
        }
        else{
            _selectedFilter.value = filter
            //Remaining Work For Chip Filtering
        }

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
        currentPage = 1
        hasNextPage = true
        _selectedFilter.value= FDLabel.CustomLabel(newFilter.getActiveFilterLabel(),FDFilterIds.CUSTOM)
        loadFunds()
    }

    fun clearFilter() {
        _filterState.value = createInitialFDFilters()
        currentPage = 1
        hasNextPage = true
        loadFunds()
    }
    fun toggleFilterScreen() {
        _showFilterScreen.value = !_showFilterScreen.value
    }

        fun onSearchTextChange(text:String){
        _searchText.value=text
    }

    private fun getSelectedFilters(): Pair<String?, String?> {
        val groups = _filterState.value.groups

        val tenure = groups
            .find { it.id == FDFilterIds.TENURE }
            ?.options
            ?.firstOrNull { it.isSelected }
            ?.id

        val payout = groups
            .find { it.id == FDFilterIds.PAYOUT_FREQUENCY }
            ?.options
            ?.firstOrNull { it.isSelected }
            ?.id

        return tenure to payout
    }

}

fun InvestmentFilter.getActiveFilterLabel(): String {

    val tenure = groups
        .find { it.id == FDFilterIds.TENURE }
        ?.options
        ?.firstOrNull { it.isSelected }

    val payout = groups
        .find { it.id == FDFilterIds.PAYOUT_FREQUENCY }
        ?.options
        ?.firstOrNull { it.isSelected }

    val parts = mutableListOf<String>()

    tenure?.let {
        parts.add(it.id.uppercase()) // "3y" → "3Y"
    }

    payout?.let {
        parts.add(it.title) // already user-friendly
    }

    return if (parts.isEmpty()) {
        "All FDs"
    } else {
        parts.joinToString(" • ")
    }
}