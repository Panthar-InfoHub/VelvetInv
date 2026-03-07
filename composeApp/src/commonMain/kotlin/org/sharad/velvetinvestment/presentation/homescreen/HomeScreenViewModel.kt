package org.sharad.velvetinvestment.presentation.homescreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.sharad.velvetinvestment.domain.models.home.FireReportSummaryDomain
import org.sharad.velvetinvestment.domain.models.home.GoalsSummaryDomain
import org.sharad.velvetinvestment.domain.models.home.KYCCompletion
import org.sharad.velvetinvestment.domain.models.home.UserWorthCardDomain
import org.sharad.velvetinvestment.domain.usecases.home.HomeScreenUseCases
import org.sharad.velvetinvestment.presentation.homescreen.uimodels.HomeScreenUiData
import org.sharad.velvetinvestment.utils.LoadingState
import org.sharad.velvetinvestment.utils.UiState
import org.sharad.velvetinvestment.utils.networking.NetworkResponse
import org.sharad.velvetinvestment.utils.networking.onError
import org.sharad.velvetinvestment.utils.networking.toMessage

class HomeScreenViewModel(
    private val useCases: HomeScreenUseCases
) : ViewModel() {

    private val _homeState =
        MutableStateFlow<UiState<HomeScreenUiData>>(UiState.Loading)
    val homeState = _homeState.asStateFlow()

    init {
        loadHome()
    }

    fun loadHome() {
        viewModelScope.launch {

            _homeState.value = UiState.Loading

            // Run in parallel
            val userWorthDeferred = async { useCases.getUserWorthCard() }
            val fireDeferred = async { useCases.getFireReport() }
            val goalsDeferred = async { useCases.getGoalsSummary() }
            val kycDeferred = async { useCases.getKycStatus() }

            val userWorthResult = userWorthDeferred.await()
            val fireResult = fireDeferred.await()
            val goalsResult = goalsDeferred.await()
            val kycResult = kycDeferred.await()

            userWorthResult
                .onError {
                    _homeState.value = UiState.Error(it.toMessage())
                    return@launch
                }

            fireResult
                .onError {
                    _homeState.value = UiState.Error(it.toMessage())
                    return@launch
                }

            goalsResult
                .onError {
                    _homeState.value = UiState.Error(it.toMessage())
                    return@launch
                }

            kycResult
                .onError {
                    _homeState.value = UiState.Error(it.toMessage())
                    return@launch
                }

            val uiData = HomeScreenUiData(
                userWorth = (userWorthResult as NetworkResponse.Success).data,
                fireReport = (fireResult as NetworkResponse.Success).data,
                goals = (goalsResult as NetworkResponse.Success).data,
                kyc = (kycResult as NetworkResponse.Success).data
            )

            _homeState.value = UiState.Success(uiData)
        }
    }
}

