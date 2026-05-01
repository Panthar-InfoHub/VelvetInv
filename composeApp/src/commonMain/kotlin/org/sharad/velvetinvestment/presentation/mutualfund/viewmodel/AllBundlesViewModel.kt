package org.sharad.velvetinvestment.presentation.mutualfund.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.sharad.velvetinvestment.domain.models.mutualfunds.BundledMutualFundDomain
import org.sharad.velvetinvestment.domain.usecases.mutualfunds.GetAllBundledFundsUseCase
import org.sharad.velvetinvestment.utils.UiState
import org.sharad.velvetinvestment.utils.networking.onError
import org.sharad.velvetinvestment.utils.networking.onSuccess

class AllBundlesViewModel(
    private val getAllBundledFundsUseCase: GetAllBundledFundsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<BundledMutualFundDomain>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<BundledMutualFundDomain>>> = _uiState.asStateFlow()

    init {
        loadBundles()
    }

    fun loadBundles() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            getAllBundledFundsUseCase()
                .onSuccess { data ->
                    _uiState.value = UiState.Success(data)
                }
                .onError { error ->
                    _uiState.value = UiState.Error(error.message)
                }
        }
    }
}