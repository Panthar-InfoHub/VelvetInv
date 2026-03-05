package org.sharad.velvetinvestment.presentation.explorefunds.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.sharad.velvetinvestment.domain.usecases.fdportfoliousecases.GetFixedDepositTopPicksUseCase
import org.sharad.velvetinvestment.domain.usecases.fundusecases.GetMutualFundTopPicksUseCase
import org.sharad.velvetinvestment.presentation.explorefunds.uimodel.FixedTopPicksUiModel
import org.sharad.velvetinvestment.presentation.explorefunds.uimodel.MutualFundTopPicksUiModel
import org.sharad.velvetinvestment.presentation.explorefunds.uimodel.toUi
import org.sharad.velvetinvestment.utils.LoadingState
import org.sharad.velvetinvestment.utils.networking.onError
import org.sharad.velvetinvestment.utils.networking.onSuccess

class ExploreFundScreenViewModel(
    private val getMutualFundTopPicksUseCase: GetMutualFundTopPicksUseCase,
    private val getFixedDepositTopPicksUseCase: GetFixedDepositTopPicksUseCase
) : ViewModel() {

    private val _loadingState = MutableStateFlow<LoadingState>(LoadingState.Loading)
    val uiState = _loadingState.asStateFlow()

    private val _mutualFunds =
        MutableStateFlow<List<MutualFundTopPicksUiModel>>(emptyList())
    val mutualFunds = _mutualFunds.asStateFlow()

    private val _fixedDeposits =
        MutableStateFlow<List<FixedTopPicksUiModel>>(emptyList())
    val fixedDeposits = _fixedDeposits.asStateFlow()

    init {
        loadExploreData()
    }

    fun loadExploreData() {

        _loadingState.value = LoadingState.Loading

        viewModelScope.launch {

            val mutualResponse = async{ getMutualFundTopPicksUseCase() }
            val fdResponse = async{ getFixedDepositTopPicksUseCase() }

            var hasError = false
            var errorMessage = ""

            mutualResponse.await()
                .onSuccess { list ->
                    _mutualFunds.value = list.map { it.toUi() }
                }
                .onError {
                    hasError = true
                    errorMessage = it.name
                }

            fdResponse.await()
                .onSuccess { list ->
                    _fixedDeposits.value = list.map { it.toUi() }
                }
                .onError {
                    hasError = true
                    errorMessage = it.name
                }

            _loadingState.value =
                if (hasError) LoadingState.Error(errorMessage)
                else LoadingState.Success
        }
    }
}
