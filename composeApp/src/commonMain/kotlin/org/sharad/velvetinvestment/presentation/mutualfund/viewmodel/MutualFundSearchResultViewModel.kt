package org.sharad.velvetinvestment.presentation.mutualfund.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.sharad.velvetinvestment.domain.models.mutualfunds.MutualFundDomain
import org.sharad.velvetinvestment.domain.usecases.fundusecases.GetMutualFundSearchResultUseCase
import org.sharad.velvetinvestment.utils.LabelFilter
import org.sharad.velvetinvestment.utils.LoadingState
import org.sharad.velvetinvestment.utils.MutualFundLabel
import org.sharad.velvetinvestment.utils.fundfiltersystem.InvestmentFilter
import org.sharad.velvetinvestment.utils.fundfiltersystem.createInitialInvestmentFilter
import org.sharad.velvetinvestment.utils.networking.onError
import org.sharad.velvetinvestment.utils.networking.onSuccess

class MutualFundSearchResultViewModel(
    private val search: String?,
    private val fundCategory: String?,
    private val getMutualFundSearchResultUseCase: GetMutualFundSearchResultUseCase
) : ViewModel() {

    private val _loadingState = MutableStateFlow<LoadingState>(LoadingState.Loading)
    val loadingState: StateFlow<LoadingState> = _loadingState.asStateFlow()

    private val _mutualFunds = MutableStateFlow<List<MutualFundDomain>>(emptyList())
    val mutualFunds = _mutualFunds.asStateFlow()

    private val _selectedYear = MutableStateFlow<SelectedReturnRatePeriod>(SelectedReturnRatePeriod.ONE_YEAR)
    val selectedYear = _selectedYear.asStateFlow()

    val sortedFunds: StateFlow<List<MutualFundDomain>> = _mutualFunds
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    private val _selectedFilter = MutableStateFlow<LabelFilter?>(null)
    val selectedFilter: StateFlow<LabelFilter?> = _selectedFilter

    private val _showFilterScreen = MutableStateFlow(false)
    val showFilterScreen: StateFlow<Boolean> = _showFilterScreen

    private val _filterState = MutableStateFlow<InvestmentFilter>(createInitialInvestmentFilter())
    val filterState: StateFlow<InvestmentFilter> = _filterState

    private val _searchText = MutableStateFlow("")
    val searchText: StateFlow<String> = _searchText

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
            getMutualFundSearchResultUseCase(
                search = search,
                fundCategory = fundCategory,
                page = 1,
                limit = 20
            )
                .onSuccess { data ->
                    currentPage = data.page
                    hasNextPage = data.hasNextPage
                    _mutualFunds.value = data.items
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
            getMutualFundSearchResultUseCase(
                search = search,
                page = nextPage,
                limit = 20
            )
                .onSuccess { data ->
                    currentPage = data.page
                    hasNextPage = data.hasNextPage
                    _mutualFunds.value += data.items
                }
                .onError {
                    // We don't change loading state to error for next page failure
                    // just show a snackbar or log it
                }

            _isLoadingNext.value = false
        }
    }

    fun onFilterSelected(filter: LabelFilter) {
        _selectedFilter.value =
            if (_selectedFilter.value == filter) null
            else filter
    }

//    fun incrementYear(){
//        if (_selectedYear.value==5) return
//        _selectedYear.value=_selectedYear.value.plus(1)
//    }
//
//    fun decrementYear(){
//        if (_selectedYear.value==1) return
//        _selectedYear.value=_selectedYear.value.minus(1)
//    }

    fun cycleReturnRatePeriod(){
        when(_selectedYear.value){
            SelectedReturnRatePeriod.THREE_MONTH -> _selectedYear.value=SelectedReturnRatePeriod.SIX_MONTH
            SelectedReturnRatePeriod.SIX_MONTH -> _selectedYear.value=SelectedReturnRatePeriod.ONE_YEAR
            SelectedReturnRatePeriod.ONE_YEAR -> _selectedYear.value=SelectedReturnRatePeriod.THREE_YEAR
            SelectedReturnRatePeriod.THREE_YEAR -> _selectedYear.value=SelectedReturnRatePeriod.THREE_MONTH
        }
    }

    fun applyFilter(newFilter: InvestmentFilter) {
        _filterState.value = newFilter
        currentPage = 1
        hasNextPage = true
        loadFunds()
    }

    fun clearFilter() {
        _filterState.value = createInitialInvestmentFilter()
        currentPage = 1
        hasNextPage = true
        loadFunds()
    }

    fun toggleFilterScreen() {
        _showFilterScreen.value = !_showFilterScreen.value
    }

    fun onSearchTextChange(newText: String) {
        _searchText.value = newText
    }

}


val defaultFilters: List<LabelFilter> = listOf(
    MutualFundLabel.IndexOnly,
    MutualFundLabel.FlexiCap,
    MutualFundLabel.Sectoral,
    MutualFundLabel.LargeCap
)


enum class SelectedReturnRatePeriod(val displayText: String){
    THREE_MONTH("3M"),
    SIX_MONTH("6M"),
    ONE_YEAR("1Y"),
    THREE_YEAR("3Y")
}