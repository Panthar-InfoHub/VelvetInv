package org.sharad.velvetinvestment.presentation.mutualfund.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.sharad.velvetinvestment.domain.models.mutualfunds.CombinedFundsDomain
import org.sharad.velvetinvestment.features.bundles.domain.models.BundleSummaryDomain
import org.sharad.velvetinvestment.features.bundles.domain.usecases.GetAllBundlesUseCase
import org.sharad.velvetinvestment.domain.usecases.fundusecases.GetCombinedCategoryMutualFundsUseCase
import org.sharad.velvetinvestment.utils.LoadingState
import org.sharad.velvetinvestment.utils.networking.onError
import org.sharad.velvetinvestment.utils.networking.onSuccess

class CategoryMutualFundViewModel(
    private val getCategoryMutualFundsUseCase: GetCombinedCategoryMutualFundsUseCase,
    private val getAllBundlesUseCase: GetAllBundlesUseCase
) : ViewModel() {

    private val _loadingState = MutableStateFlow<LoadingState>(LoadingState.Loading)
    val loadingState: StateFlow<LoadingState> = _loadingState.asStateFlow()

    private val _mutualFunds =
        MutableStateFlow<CombinedFundsDomain>(CombinedFundsDomain())
    val mutualFunds = _mutualFunds.asStateFlow()

    /** The curated bundles shown above the categories; empty hides the section. */
    private val _bundles = MutableStateFlow<List<BundleSummaryDomain>>(emptyList())
    val bundles = _bundles.asStateFlow()

    private val _searchText = MutableStateFlow("")
    val searchText: StateFlow<String> = _searchText


    init {
        loadMutualFunds()
    }

    fun loadMutualFunds() {
        loadBundles()
        viewModelScope.launch {
            _loadingState.value = LoadingState.Loading
            getCategoryMutualFundsUseCase()
                .onSuccess { data ->
                    _loadingState.value = LoadingState.Success
                    _mutualFunds.value = data
                }
                .onError { error ->
                    _loadingState.value =
                        LoadingState.Error(error.message)
                }
        }
    }

    /** Loaded on its own: the bundles are extra, so failing to get them never blocks the funds. */
    private fun loadBundles() {
        viewModelScope.launch {
            getAllBundlesUseCase()
                .onSuccess { _bundles.value = it }
        }
    }

    fun onSearchTextChange(newText: String) {
        _searchText.value = newText
    }

}
