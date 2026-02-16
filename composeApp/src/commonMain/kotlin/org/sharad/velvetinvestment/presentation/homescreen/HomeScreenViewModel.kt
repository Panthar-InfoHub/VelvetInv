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
import org.sharad.velvetinvestment.utils.UIState

class HomeScreenViewModel(
    private val useCases: HomeScreenUseCases
) : ViewModel() {

    private val _name = MutableStateFlow<String>("Pooja")
    val name = _name.asStateFlow()

    private val _netWorthInfo = MutableStateFlow<UserWorthCardDomain?>(null)
    val netWorthInfo = _netWorthInfo.asStateFlow()

    private val _kycProcess = MutableStateFlow<KYCCompletion?>(null)
    val kycProcess = _kycProcess.asStateFlow()

    private val _fireReportInfo = MutableStateFlow<FireReportSummaryDomain?>(null)
    val fireReport = _fireReportInfo.asStateFlow()

    private val _goalsInfo = MutableStateFlow<List<GoalsSummaryDomain>>(emptyList())
    val goalsInfo = _goalsInfo.asStateFlow()

    private val _homeUIState=MutableStateFlow<UIState>(UIState.Loading)
    val homeUIState=_homeUIState.asStateFlow()


    init {
        loadHome()
    }

    private fun loadHome() {

        viewModelScope.launch {
            _homeUIState.value= UIState.Loading
            val netWorth = async {loadUserWorth() }
            val fire = async { loadFireReport() }
            val goals = async { loadGoals() }
            val kyc = async { loadKyc() }

            netWorth.await()
            fire.await()
            goals.await()
            kyc.await()

            _homeUIState.value= UIState.Success
        }
    }

    private suspend fun loadUserWorth() {
        _netWorthInfo.value = useCases.getUserWorthCard()
    }

    private suspend fun loadFireReport() {
        _fireReportInfo.value = useCases.getFireReport()
    }

    private suspend fun loadGoals() {
        _goalsInfo.value = useCases.getGoalsSummary()
    }

    private suspend fun loadKyc() {
        _kycProcess.value = useCases.getKycStatus()
    }
}

