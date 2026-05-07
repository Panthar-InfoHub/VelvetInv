package org.sharad.velvetinvestment.presentation.goals.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.sharad.velvetinvestment.data.remote.model.useedata.UserGoal
import org.sharad.velvetinvestment.domain.models.mutualfunds.BundledMutualFundDomain
import org.sharad.velvetinvestment.domain.repository.UserFinance
import org.sharad.velvetinvestment.domain.usecases.mutualfunds.GetAllBundledFundsUseCase
import org.sharad.velvetinvestment.domain.usecases.user.GetUserDataUseCase
import org.sharad.velvetinvestment.utils.AppEvents
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
    val requiredMonthly: Double
)

class ProjectionImpactViewModel(
    private val getUserDataUseCase: GetUserDataUseCase,
    private val getAllBundledFundsUseCase: GetAllBundledFundsUseCase,
    private val deleteRepo: UserFinance,
    private val goalId: String
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<ProjectionImpactUiData>>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _bundleData =  MutableStateFlow<UiState<List<BundledMutualFundDomain>>>(UiState.Loading)
    val bundleData= _bundleData.asStateFlow()

    init {
        loadGoalDetails()
        loadBundles()
    }

    fun loadGoalDetails() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            getUserDataUseCase()
                .onSuccess { userData ->
                    val userGoal = userData.data.user_goals.find { it.id == goalId }
                    if (userGoal != null) {
                        _uiState.value = UiState.Success(deriveProjectionData(userGoal))
                    } else {
                        _uiState.value = UiState.Error("Goal not found")
                    }
                }
                .onError {
                    _uiState.value = UiState.Error(it.message)
                }
        }
    }

    fun loadBundles() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            getAllBundledFundsUseCase()
                .onSuccess { data ->
                    _bundleData.value = UiState.Success(data)
                }
                .onError { error ->
                    _bundleData.value = UiState.Error(error.message)
                }
        }
    }

    private fun deriveProjectionData(goal: UserGoal): ProjectionImpactUiData {
        val todaysCost = goal.current_goal_cost?.toLongOrNull() ?: 0L
        val yearsLeft = goal.years_left ?: 0
        val inflationRate = goal.inflation_rate / 100.0
        val returnRate = goal.return_rate / 100.0

        val futureValue = todaysCost * (1 + inflationRate).pow(yearsLeft.toDouble())
        
        val currentSaved = goal.current_saved_amount.toLongOrNull() ?: 0L
        val targetAmount = futureValue.toLong()

        val monthlyReturnRate = returnRate / 12
        val totalMonths = yearsLeft * 12
        
        val numerator = futureValue - (currentSaved * (1 + returnRate).pow(yearsLeft.toDouble()))
        val denominator = if (totalMonths > 0) {
            ((1 + monthlyReturnRate).pow(totalMonths.toDouble()) - 1) / monthlyReturnRate
        } else {
            1.0
        }
        
        val monthlySip = if (denominator > 0) numerator / denominator else 0.0

        val progress = if (targetAmount > 0) (currentSaved.toDouble() / targetAmount) else 0.0

        val timeFactor = (yearsLeft.toDouble() / 30.0).coerceIn(0.0, 1.0)
        val feasibilityScore = (progress * 0.7 + timeFactor * 0.3).coerceIn(0.1, 1.0).toFloat()
        
        val targetYear = DateTimeUtils.getCurrentYear() + yearsLeft

        return ProjectionImpactUiData(
            goalName = goal.goal_item_name ?: goal.goal_name ?: "Goal",
            todaysCost = todaysCost,
            futureValue = futureValue,
            targetYear = targetYear,
            monthlySip = monthlySip,
            feasibilityScore = feasibilityScore,
            currentSaved = currentSaved,
            targetAmount = targetAmount,
            increasedBy = futureValue - todaysCost,
            requiredMonthly = monthlySip
        )
    }

    fun deleteGoal(id: String, onSuccess: () -> Unit) {
        val state = uiState.value
        if (state !is UiState.Success) return

        val currentData = _uiState.value as (UiState.Success<ProjectionImpactUiData>)

        viewModelScope.launch {
            _uiState.value = UiState.Loading
            deleteRepo.deleteGoal(id)
                .onSuccess {
                    AppEvents.sendGoalRefreshEvent()
                    AppEvents.sendHomeRefreshEvent()
                    onSuccess()
                }
                .onError {
                    _uiState.value = currentData
                    SnackBarController.showError(it.message)
                    AppEvents.sendGoalRefreshEvent()
                    AppEvents.sendHomeRefreshEvent()
                }
        }

    }

}
