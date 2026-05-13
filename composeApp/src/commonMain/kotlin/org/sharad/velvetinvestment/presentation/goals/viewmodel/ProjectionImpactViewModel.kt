package org.sharad.velvetinvestment.presentation.goals.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.sharad.velvetinvestment.domain.models.goals.GoalDomain
import org.sharad.velvetinvestment.domain.models.goals.GoalSchemeDomain
import org.sharad.velvetinvestment.domain.models.mutualfunds.BundledMutualFundDomain
import org.sharad.velvetinvestment.domain.repository.UserFinance
import org.sharad.velvetinvestment.domain.usecases.mutualfunds.GetAllBundledFundsUseCase
import org.sharad.velvetinvestment.domain.usecases.userfinance.GetGoalByIdUseCase
import org.sharad.velvetinvestment.domain.usecases.userfinance.GetPortfolioUseCase
import org.sharad.velvetinvestment.domain.usecases.userfinance.MapGoalUseCase
import org.sharad.velvetinvestment.presentation.goals.uimodels.toBody
import org.sharad.velvetinvestment.utils.AppEventsController
import org.sharad.velvetinvestment.utils.DateTimeUtils
import org.sharad.velvetinvestment.utils.SnackBarController
import org.sharad.velvetinvestment.utils.UiState
import org.sharad.velvetinvestment.utils.networking.onError
import org.sharad.velvetinvestment.utils.networking.onSuccess
import kotlin.math.pow

data class ProjectionImpactUiData(
    val goalName: String,
    val todaysCost: Long,
    val futureValue: Double,
    val targetYear: Int,
    val monthlySip: Double,
    val feasibilityScore: Float,
    val currentSaved: Long,
    val targetAmount: Long,
    val increasedBy: Double,
    val requiredMonthly: Double,
    val schemes: List<GoalSchemeDomain>,
    val goalId:Int
)

data class SelectableSchemeUiModel(
    val schemeId: String,
    val name: String,
    val units: String,
    val value: Double,
    val isSelected: Boolean,
    val folio: String
)

sealed interface PortfolioSideEffect{
    data object OpenBottomSheet: PortfolioSideEffect
    data object CloseBottomSheet: PortfolioSideEffect
}

class ProjectionImpactViewModel(
    private val getAllBundledFundsUseCase: GetAllBundledFundsUseCase,
    private val deleteRepo: UserFinance,
    private val getGoalByIdUseCase: GetGoalByIdUseCase,
    private val getPortfolioUseCase: GetPortfolioUseCase,
    private val mapGoalUseCase: MapGoalUseCase,
    private val goalId: String
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<UiState<ProjectionImpactUiData>>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _bundleData =
        MutableStateFlow<UiState<List<BundledMutualFundDomain>>>(UiState.Loading)
    val bundleData = _bundleData.asStateFlow()

    private val _projectionSideEffect = MutableSharedFlow<PortfolioSideEffect>()
    val projectionSideEffect = _projectionSideEffect.asSharedFlow()


    init {
        loadData()
    }


    fun loadData(){
        loadGoalDetails()
        loadBundles()
    }

    fun loadGoalDetails() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            getGoalByIdUseCase(goalId)
                .onSuccess { goal ->
                    _uiState.value = UiState.Success(
                        deriveProjectionData(goal)
                    )
                }
                .onError {
                    _uiState.value = UiState.Error(it.message)
                }
        }
    }

    fun loadBundles() {
        viewModelScope.launch {
            _bundleData.value = UiState.Loading

            getAllBundledFundsUseCase()
                .onSuccess { data ->
                    _bundleData.value = UiState.Success(data)
                }
                .onError { error ->
                    _bundleData.value = UiState.Error(error.message)
                }
        }
    }

    private fun deriveProjectionData(
        goal: GoalDomain
    ): ProjectionImpactUiData {

        val todaysCost =
            goal.currentGoalCost?.toLongOrNull() ?: 0L

        val yearsLeft =
            goal.yearsLeft ?: 0

        val inflationRate =
            goal.inflationRate / 100.0

        val returnRate =
            goal.returnRate / 100.0

        val futureValue =
            todaysCost * (1 + inflationRate)
                .pow(yearsLeft.toDouble())

        val currentSaved =
            goal.currentSavedAmount.toLongOrNull() ?: 0L

        val targetAmount =
            futureValue.toLong()

        val monthlyReturnRate =
            returnRate / 12

        val totalMonths =
            yearsLeft * 12

        val numerator =
            futureValue - (
                    currentSaved *
                            (1 + returnRate)
                                .pow(yearsLeft.toDouble())
                    )

        val denominator =
            if (totalMonths > 0) {
                (
                        (1 + monthlyReturnRate)
                            .pow(totalMonths.toDouble()) - 1
                        ) / monthlyReturnRate
            } else {
                1.0
            }

        val monthlySip =
            if (denominator > 0) {
                numerator / denominator
            } else {
                0.0
            }

        val progress =
            if (targetAmount > 0) {
                currentSaved.toDouble() / targetAmount
            } else {
                0.0
            }

        val timeFactor =
            (yearsLeft.toDouble() / 30.0)
                .coerceIn(0.0, 1.0)

        val feasibilityScore =
            (
                    progress * 0.7 +
                            timeFactor * 0.3
                    ).coerceIn(0.1, 1.0)
                .toFloat()

        val targetYear =
            DateTimeUtils.getCurrentYear() + yearsLeft

        return ProjectionImpactUiData(
            goalName = goal.goalItemName
                ?: goal.goalName
                ?: "Goal",
            todaysCost = todaysCost,
            futureValue = futureValue,
            targetYear = targetYear,
            monthlySip = monthlySip,
            feasibilityScore = feasibilityScore,
            currentSaved = currentSaved,
            targetAmount = targetAmount,
            increasedBy = futureValue - todaysCost,
            requiredMonthly = monthlySip,
            schemes = goal.schemes,
            goalId = goal.goalId
        )
    }

    fun deleteGoal(
        id: String,
        onSuccess: () -> Unit
    ) {

        val state = uiState.value

        if (state !is UiState.Success) return

        val currentData =
            _uiState.value as UiState.Success<ProjectionImpactUiData>

        viewModelScope.launch {

            _uiState.value = UiState.Loading

            deleteRepo.deleteGoal(id)
                .onSuccess {

                    AppEventsController.sendGoalRefreshEvent()
                    AppEventsController.sendHomeRefreshEvent()

                    onSuccess()
                }
                .onError {

                    _uiState.value = currentData

                    SnackBarController.showError(it.message)

                    AppEventsController.sendGoalRefreshEvent()
                    AppEventsController.sendHomeRefreshEvent()
                }
        }
    }

    fun openBottomSheet(){
        viewModelScope.launch {
            _projectionSideEffect.emit(PortfolioSideEffect.OpenBottomSheet)
            loadPortfolio()
        }
    }

    fun closeBottomSheet(){
        viewModelScope.launch {
            _projectionSideEffect.emit(PortfolioSideEffect.CloseBottomSheet)
        }
    }

    private val _portfolioData = MutableStateFlow<UiState<List<SelectableSchemeUiModel>>>(UiState.Loading)
    val portfolioData = _portfolioData.asStateFlow()

    fun loadPortfolio() {
        _portfolioData.value = UiState.Loading
        viewModelScope.launch {
            getPortfolioUseCase()
                .onSuccess {
                    _portfolioData.value = UiState.Success(it.mutualFunds.map {
                        SelectableSchemeUiModel(
                            schemeId = it.id.toString(),
                            name = it.title,
                            units = it.balanceUnits.toString(),
                            value = it.amount,
                            folio =it.folio,
                            isSelected = false
                        )
                    })
                }
                .onError {
                    _portfolioData.value = UiState.Error(it.message)
                }
        }
    }

    fun mapGoal() {
        val currentState = _uiState.value
        if (currentState !is UiState.Success) return
        if (_portfolioData.value !is UiState.Success) return
        viewModelScope.launch {
            closeBottomSheet()
            _uiState.value = UiState.Loading
            mapGoalUseCase(
                body = (_portfolioData.value as UiState.Success).data.toBody(currentState.data.goalId)
            ).onSuccess {
                loadGoalDetails()
            }.onError {
                _uiState.value = UiState.Success(currentState.data)
                SnackBarController.showError(it.message)
            }
        }
    }

    fun toggleSelection(id: Int) {
        val currentState = _portfolioData.value
        if (currentState !is UiState.Success) return
        _portfolioData.value = UiState.Success(
            currentState.data.map { scheme ->

                if (scheme.schemeId.toInt() == id) {
                    scheme.copy(
                        isSelected = !scheme.isSelected
                    )
                } else {
                    scheme
                }
            }
        )
    }

}