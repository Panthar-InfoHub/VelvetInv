package org.sharad.velvetinvestment.features.portfolio.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.sharad.velvetinvestment.utils.networking.onError
import org.sharad.velvetinvestment.utils.networking.onSuccess
import org.sharad.velvetinvestment.utils.SnackBarController
import org.sharad.velvetinvestment.utils.UiState
import org.sharad.velvetinvestment.features.portfolio.domain.models.FixedDepositTransactionDomain
import org.sharad.velvetinvestment.features.portfolio.domain.usecases.GetFDPortfolioByIdUseCase
import org.sharad.velvetinvestment.features.portfolio.domain.usecases.GetFDRedirectUrlUseCase

sealed interface FDPortfolioSideEffect {
    data class OpenWebView(val url: String) : FDPortfolioSideEffect
}

class FDPortFolioDetailsViewModel(
    private val getFDPortfolioByIdUseCase: GetFDPortfolioByIdUseCase,
    private val getFDRedirectUrlUseCase: GetFDRedirectUrlUseCase,
    private val fdId: String
) : ViewModel() {

    private val _loadingState = MutableStateFlow<UiState<FixedDepositTransactionDomain>>(UiState.Loading)
    val loadingState = _loadingState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<FDPortfolioSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    init {
        loadFDDetails()
    }
    fun loadFDDetails() {
        viewModelScope.launch {
            _loadingState.value = UiState.Loading
            getFDPortfolioByIdUseCase(fdId)
                .onSuccess { domain ->
                    _loadingState.value = UiState.Success(domain)
                }
                .onError { error ->
                    _loadingState.value = UiState.Error(error.message)
                }
        }
    }

    fun onClick(){
        val data=loadingState.value
        if (_loadingState.value !is UiState.Success) return
        viewModelScope.launch {
            val currentData = (_loadingState.value as UiState.Success).data
            _loadingState.value = UiState.Loading
            getFDRedirectUrlUseCase(id=currentData.id, event = currentData.pendingAction.name)
                .onSuccess { url ->
                    _loadingState.value = data
                    _sideEffect.emit(FDPortfolioSideEffect.OpenWebView(url))
                }
                .onError { error ->
                    _loadingState.value = data
                    SnackBarController.showError(error.message)
                }
        }
    }

}
