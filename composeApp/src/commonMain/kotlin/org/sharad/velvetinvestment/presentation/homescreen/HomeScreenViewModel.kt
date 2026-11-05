package org.sharad.velvetinvestment.presentation.homescreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.sharad.velvetinvestment.domain.models.home.UserWorthCardDomain
import org.sharad.velvetinvestment.features.cart.domain.usecases.GetUserCartUseCase
import org.sharad.velvetinvestment.domain.usecases.user.GetUnreadStatusUseCase
import org.sharad.velvetinvestment.features.fireonboarding.domain.models.FireStage
import org.sharad.velvetinvestment.features.fireonboarding.domain.models.isPending
import org.sharad.velvetinvestment.utils.SnackBarController
import org.sharad.velvetinvestment.presentation.homescreen.uimodels.HomeScreenUiData
import org.sharad.velvetinvestment.shared.domain.models.UserDataDomain
import org.sharad.velvetinvestment.shared.domain.usecase.GetUserDataUseCase
import org.sharad.velvetinvestment.utils.storage.AuthPrefs
import org.sharad.velvetinvestment.utils.UiState
import org.sharad.velvetinvestment.utils.networking.onError
import org.sharad.velvetinvestment.utils.networking.onSuccess
import org.sharad.velvetinvestment.utils.trimDoubleTo

class HomeScreenViewModel(
    private val getUserDataUseCase: GetUserDataUseCase,
    private val loadCartUseCase: GetUserCartUseCase,
    private val getUnreadStatusUseCase: GetUnreadStatusUseCase,
    private val authPrefs: AuthPrefs
) : ViewModel() {

    private val _homeState =
        MutableStateFlow<UiState<HomeScreenUiData>>(UiState.Loading)
    val homeState = _homeState.asStateFlow()

    init {
        loadHomeData()
    }

    fun loadHomeData(){
        loadHome()
        loadCart()
        loadUnreadStatus()
    }

    fun loadHome() {
        viewModelScope.launch {

            _homeState.value = UiState.Loading

            getUserDataUseCase()
                .onSuccess { user ->
                    // The app lock reads these on the next cold start (JN).
                    authPrefs.setMpinEnabled(user.mpinEnabled)
                    authPrefs.setMpinSetup(user.mpinIsSetup)
                    _homeState.value = UiState.Success(user.toHomeScreenUiData())
                    loadUnreadStatus()
                }
                .onError {
                    _homeState.value = UiState.Error(it.message)
                }

        }
    }

    /**
     * Reloads the user without the full-screen loader — the KYC-in-progress card's refresh (JN
     * `SilentRefresh`). A failure only shows a snackbar over what is already there.
     */
    fun silentRefresh() {
        val current = (_homeState.value as? UiState.Success)?.data ?: return
        if (current.isKycRefreshing) return
        _homeState.value = UiState.Success(current.copy(isKycRefreshing = true))
        viewModelScope.launch {
            getUserDataUseCase()
                .onSuccess { user ->
                    authPrefs.setMpinEnabled(user.mpinEnabled)
                    authPrefs.setMpinSetup(user.mpinIsSetup)
                    _homeState.update { state ->
                        val old = (state as? UiState.Success)?.data
                        UiState.Success(
                            user.toHomeScreenUiData().copy(
                                hidden = old?.hidden ?: true,
                                hasUnreadNotifications = old?.hasUnreadNotifications ?: false
                            )
                        )
                    }
                }
                .onError { error ->
                    _homeState.update { state ->
                        if (state is UiState.Success) UiState.Success(state.data.copy(isKycRefreshing = false))
                        else state
                    }
                    SnackBarController.showError(error.message)
                }
        }
    }

    fun loadUnreadStatus() {
        viewModelScope.launch {
            getUnreadStatusUseCase()
                .onSuccess { hasUnread ->
                    _homeState.update { state ->
                        if (state is UiState.Success) {
                            UiState.Success(state.data.copy(hasUnreadNotifications = hasUnread))
                        } else state
                    }
                }
        }
    }

    fun toggleHidden() {
        _homeState.update {
            if (it is UiState.Success) {
                UiState.Success(it.data.copy(hidden = !it.data.hidden))
            } else it
        }
    }

    fun removeNotificationMark(){
        _homeState.update {
            if (it is UiState.Success) {
                UiState.Success(it.data.copy(hasUnreadNotifications = false))
            } else it
        }
    }

    fun addNotificationMark(){
        _homeState.update {
            if (it is UiState.Success) {
                UiState.Success(it.data.copy(hasUnreadNotifications = true))
            } else it
        }
    }

    fun loadCart(){
        viewModelScope.launch {
            loadCartUseCase()
        }
    }

}

/** FIRE steps in `fire_stages` that are verified or skipped. */
private val UserDataDomain.fireStepsDone: Int
    get() = FireStage.entries.count { !onboarding.isPending(it) }

private fun UserDataDomain.toHomeScreenUiData(): HomeScreenUiData = HomeScreenUiData(
    name = name,
    userWorth = UserWorthCardDomain(
        totalNetWorth = netWorth,
        absGrowth = 0L,
        CARGGrowth = 0.0,
        investingRate = 0.0,
        mutualFunds = dashboard.mutualFunds.toLong(),
        fixedDeposits = dashboard.fixedDeposits.toLong()
    ),
    // `user_home_data.fire_percentage`, EMIs included as the card has always shown it.
    fireReport = homeData.firePercentage.includeEmi.trimDoubleTo(2),
    goals = goals.take(5),
    kycCompletion = kycVerified,
    // `GET /user/` no longer reports a trading account (dropped from onboarding), so its
    // "finish setting up" card is hidden rather than shown forever.
    tradingAccountCompletion = true,
    onboardingPrompt = onboardingPrompt,
    kycFailureReason = onboarding.kycFailureReason,
    fireStepsDone = fireStepsDone,
    fireStepsTotal = FireStage.total,
)
