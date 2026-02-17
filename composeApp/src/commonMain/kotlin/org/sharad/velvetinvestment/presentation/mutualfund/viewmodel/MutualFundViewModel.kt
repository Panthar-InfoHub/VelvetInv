package org.sharad.velvetinvestment.presentation.mutualfund.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sharad.surakshakawachneo.utils.Networking.NetworkResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.sharad.velvetinvestment.domain.usecases.fundusecases.GetCategoryMutualFundsUseCase
import org.sharad.velvetinvestment.presentation.mutualfund.CategoryMutualFundUI
import org.sharad.velvetinvestment.presentation.mutualfund.toUI
import org.sharad.velvetinvestment.utils.UIState
import org.sharad.velvetinvestment.utils.networking.onError
import org.sharad.velvetinvestment.utils.networking.onSuccess
import org.sharad.velvetinvestment.utils.networking.toMessage

class MutualFundViewModel(
    private val getCategoryMutualFundsUseCase: GetCategoryMutualFundsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UIState>(UIState.Loading)
    val uiState: StateFlow<UIState> = _uiState.asStateFlow()

    private val _mutualFunds =
        MutableStateFlow<List<CategoryMutualFundUI>>(emptyList())
    val mutualFunds: StateFlow<List<CategoryMutualFundUI>> =
        _mutualFunds.asStateFlow()

    private val _searchText = MutableStateFlow("")
    val searchText: StateFlow<String> = _searchText


    init {
        loadMutualFunds()
    }

    fun loadMutualFunds() {
        viewModelScope.launch {
            _uiState.value = UIState.Loading
            getCategoryMutualFundsUseCase()
                .onSuccess { data ->
                    _mutualFunds.value =
                        data.map { it.toUI() }
                    _uiState.value = UIState.Success
                }
                .onError { error ->
                    _uiState.value =
                        UIState.Error(error.toMessage())
                }
        }
    }

    fun onSearchTextChange(newText: String) {
        _searchText.value = newText
    }

}
