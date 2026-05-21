package org.sharad.velvetinvestment.presentation.explorefunds.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.sharad.velvetinvestment.domain.models.fixeddeposits.FixedDepositDomain
import org.sharad.velvetinvestment.domain.models.mutualfunds.MutualFundDomain
import org.sharad.velvetinvestment.domain.usecases.fixeddepositusecases.GetTopPickFDUseCase
import org.sharad.velvetinvestment.domain.usecases.fundusecases.GetMutualFundTopPicksUseCase
import org.sharad.velvetinvestment.utils.UiState
import org.sharad.velvetinvestment.utils.networking.onError
import org.sharad.velvetinvestment.utils.networking.onSuccess

data class TopPickCombinedUiModel(
    val funds: List<MutualFundDomain> = emptyList(),
    val fixedDeposits: List<FixedDepositDomain> = emptyList()
)
class ExploreFundScreenViewModel(
    private val getMutualFundTopPicksUseCase: GetMutualFundTopPicksUseCase,
    private val getFixedDepositTopPicksUseCase: GetTopPickFDUseCase
) : ViewModel() {

    private val _loadingState = MutableStateFlow<UiState<TopPickCombinedUiModel>>(UiState.Loading)
    val uiState = _loadingState.asStateFlow()

    init {
        loadExploreData()
    }

    fun loadExploreData() {

        _loadingState.value = UiState.Loading

        viewModelScope.launch {

            val mutualResponse = async {
                getMutualFundTopPicksUseCase()
            }

            val fdResponse = async {
                getFixedDepositTopPicksUseCase()
            }

            val mutualResult = mutualResponse.await()
            val fdResult = fdResponse.await()

            var mutualFunds: List<MutualFundDomain> = emptyList()
            var fixedDeposits: List<FixedDepositDomain> = emptyList()

            var hasAnySuccess = false
            var errorMessage = "Something went wrong"

            mutualResult
                .onSuccess { funds ->
                    hasAnySuccess = true
                    mutualFunds = funds
                }
                .onError { error ->
                    errorMessage = error.message
                }

            fdResult
                .onSuccess { fds ->
                    hasAnySuccess = true
                    fixedDeposits = fds
                }
                .onError { error ->
                    errorMessage = error.message
                }

            if (hasAnySuccess) {

                _loadingState.value = UiState.Success(
                    TopPickCombinedUiModel(
                        funds = mutualFunds,
                        fixedDeposits = fixedDeposits
                    )
                )

            } else {

                _loadingState.value = UiState.Error(
                    errorMessage
                )
            }
        }
    }
}
