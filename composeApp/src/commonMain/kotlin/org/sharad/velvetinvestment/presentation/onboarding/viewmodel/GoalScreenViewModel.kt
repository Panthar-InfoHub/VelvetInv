package org.sharad.velvetinvestment.presentation.onboarding.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.sharad.emify.core.ui.theme.Primary
import org.sharad.emify.core.ui.theme.Secondary
import org.sharad.emify.core.ui.theme.bgColor1
import org.sharad.emify.core.ui.theme.bgColor4
import org.sharad.emify.core.ui.theme.homeGoalColor
import org.sharad.velvetinvestment.domain.GoalType
import org.sharad.velvetinvestment.domain.models.goals.GoalRequest
import org.sharad.velvetinvestment.presentation.goals.uimodels.GoalOption
import org.sharad.velvetinvestment.utils.DateTimeUtils

class GoalScreenViewModel : ViewModel() {

    private val _goalList = MutableStateFlow<List<GoalRequest>>(emptyList())
    val goalList = _goalList.asStateFlow()

    private val _selectedGoalOption = MutableStateFlow<GoalOption?>(null)
    val selectedGoalOption = _selectedGoalOption.asStateFlow()

    private val _inflationRate = MutableStateFlow("8")
    private val _returnRate = MutableStateFlow("10")
    private val _currentSavedAmount = MutableStateFlow("")

    private val _childName = MutableStateFlow("")
    private val _childAge = MutableStateFlow("")
    private val _targetYear = MutableStateFlow("")
    private val _currentGoalCost = MutableStateFlow("")

    private val _currentAge = MutableStateFlow("")
    private val _retirementAge = MutableStateFlow("")
    private val _lifeExpectancy = MutableStateFlow("")
    private val _currentMonthlyExpense = MutableStateFlow("")
    private val _postRetirementReturn = MutableStateFlow("")

    private val _goalName = MutableStateFlow("")
    private val _goalItemName = MutableStateFlow("")

    val inflationRate = _inflationRate.asStateFlow()
    val returnRate = _returnRate.asStateFlow()
    val currentSavedAmount = _currentSavedAmount.asStateFlow()

    val childName = _childName.asStateFlow()
    val childAge = _childAge.asStateFlow()
    val targetYear = _targetYear.asStateFlow()
    val currentGoalCost = _currentGoalCost.asStateFlow()

    val currentAge = _currentAge.asStateFlow()
    val retirementAge = _retirementAge.asStateFlow()
    val lifeExpectancy = _lifeExpectancy.asStateFlow()
    val currentMonthlyExpense = _currentMonthlyExpense.asStateFlow()
    val postRetirementReturn = _postRetirementReturn.asStateFlow()

    val goalName = _goalName.asStateFlow()

    private val _goalItemNameInput = MutableStateFlow("")
    val goalItemNameInput = _goalItemNameInput.asStateFlow()

    val totalTargetAmount = _goalList.map { list ->
        list.sumOf {
            when (it) {
                is GoalRequest.ChildEducation -> it.currentGoalCost
                is GoalRequest.ChildMarriage -> it.currentGoalCost
                is GoalRequest.WealthBuildingGoal -> it.currentGoalCost
                is GoalRequest.Retirement -> it.currentMonthlyExpense * 12L // or better logic later
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = 0L
    )

    val isFormValid = combine(
        _selectedGoalOption,
        _inflationRate,
        _returnRate,
        _currentSavedAmount,

        _childName,
        _childAge,
        _targetYear,
        _currentGoalCost,

        _currentAge,
        _retirementAge,
        _lifeExpectancy,
        _currentMonthlyExpense,
        _postRetirementReturn,

        _goalName,
        _goalItemNameInput
    ) { values ->

        val option = values[0] as GoalOption?

        val inflation = (values[1] as String).toIntOrNull()
        val returns = (values[2] as String).toIntOrNull()

        if (option == null || inflation == null || returns == null) return@combine false

        when (option.type) {

            GoalType.ChildEducation,
            GoalType.ChildMarriage -> {

                val name = values[4] as String
                val age = (values[5] as String).toIntOrNull()
                val year = (values[6] as String).toIntOrNull()
                val cost = (values[7] as String).toLongOrNull()

                name.isNotBlank() &&
                        age != null &&
                        year != null &&
                        cost != null
            }

            GoalType.Retirement -> {

                val currentAge = (values[8] as String).toIntOrNull()
                val retirementAge = (values[9] as String).toIntOrNull()
                val lifeExpectancy = (values[10] as String).toIntOrNull()
                val expense = (values[11] as String).toLongOrNull()
                val postReturn = (values[12] as String).toIntOrNull()

                currentAge != null &&
                        retirementAge != null &&
                        lifeExpectancy != null &&
                        expense != null &&
                        postReturn != null &&
                        retirementAge > currentAge
            }

            GoalType.WealthBuilding -> {

                val years = (values[6] as String).toIntOrNull()
                val cost = (values[7] as String).toLongOrNull()

                years != null && cost != null
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = false
    )

    fun addGoal(onSuccess: () -> Unit) {
        val option = _selectedGoalOption.value ?: return

        val inflation = _inflationRate.value.toIntOrNull() ?: return
        val returns = _returnRate.value.toIntOrNull() ?: return
        val saved = _currentSavedAmount.value.toLongOrNull() ?: 0L
        val title= option.title

        val goal = when (option.type) {

            GoalType.ChildEducation -> createChildEducationGoal(inflation, returns, saved,title)

            GoalType.ChildMarriage -> createChildMarriageGoal(inflation, returns, saved,title)

            GoalType.Retirement -> createRetirementGoal(inflation, returns, saved,title)

            GoalType.WealthBuilding -> createWealthGoal(option, inflation, returns, saved,title)
        } ?: return

        _goalList.value = _goalList.value + goal
        onSuccess()
        clearInputs()
    }

    fun deleteGoal(goal: GoalRequest) {
        val currentList = _goalList.value.toMutableList()
        val index = currentList.indexOfFirst { it == goal }

        if (index != -1) {
            currentList.removeAt(index)
            _goalList.value = currentList
        }
    }

    fun onGoalOptionSelected(option: GoalOption) {
        _selectedGoalOption.value = option
        _goalName.value = option.title
        _goalItemNameInput.value = option.goalItemName ?: option.title
    }

    fun onGoalItemNameChange(value: String) {
        _goalItemNameInput.value = value
    }

    fun onGoalNameChange(value: String) {
        _goalName.value = value
    }

    fun onInflationChange(value: String) {
        _inflationRate.value = value
    }

    fun onReturnChange(value: String) {
        _returnRate.value = value
    }

    fun onSavedAmountChange(value: String) {
        _currentSavedAmount.value = value
    }

    fun onChildNameChange(value: String) {
        _childName.value = value
    }

    fun onChildAgeChange(value: String) {
        _childAge.value = value
    }

    fun onTargetYearChange(value: String) {
        _targetYear.value = value
    }

    fun onGoalCostChange(value: String) {
        _currentGoalCost.value = value
    }

    fun onCurrentAgeChange(value: String) {
        _currentAge.value = value
    }

    fun onRetirementAgeChange(value: String) {
        _retirementAge.value = value
    }

    fun onLifeExpectancyChange(value: String) {
        _lifeExpectancy.value = value
    }

    fun onMonthlyExpenseChange(value: String) {
        _currentMonthlyExpense.value = value
    }

    fun onPostReturnChange(value: String) {
        _postRetirementReturn.value = value
    }

    fun clearInputs() {
        _childName.value = ""
        _childAge.value = ""
        _targetYear.value = ""
        _currentGoalCost.value = ""

        _currentAge.value = ""
        _retirementAge.value = ""
        _lifeExpectancy.value = ""
        _currentMonthlyExpense.value = ""
        _postRetirementReturn.value = ""

        _goalName.value = ""
        _goalItemName.value = ""

        _inflationRate.value = "8"
        _returnRate.value = "10"
        _currentSavedAmount.value = ""
    }


    private fun createChildEducationGoal(
        inflation: Int,
        returns: Int,
        saved: Long,
        title: String
    ): GoalRequest.ChildEducation? {

        val name = _childName.value
        val age = _childAge.value.toIntOrNull()
        val years = _targetYear.value.toIntOrNull()?.minus(DateTimeUtils.getCurrentYear())
        val cost = _currentGoalCost.value.toLongOrNull()

        if (name.isBlank() || age == null || years == null || cost == null) return null

        return GoalRequest.ChildEducation(
            childName = name,
            childAge = age,
            yearsToGoal = years,
            currentGoalCost = cost,
            inflationRate = inflation,
            returnRate = returns,
            currentSavedAmount = saved,
            title = title
        )
    }

    private fun createChildMarriageGoal(
        inflation: Int,
        returns: Int,
        saved: Long,
        title: String
    ): GoalRequest.ChildMarriage? {

        val name = _childName.value
        val age = _childAge.value.toIntOrNull()
        val years = _targetYear.value.toIntOrNull()?.minus(DateTimeUtils.getCurrentYear())
        val cost = _currentGoalCost.value.toLongOrNull()

        if (name.isBlank() || age == null || years == null || cost == null) return null

        return GoalRequest.ChildMarriage(
            childName = name,
            childAge = age,
            yearsToGoal = years,
            currentGoalCost = cost,
            inflationRate = inflation,
            returnRate = returns,
            currentSavedAmount = saved,
            title = title

        )
    }

    private fun createRetirementGoal(
        inflation: Int,
        returns: Int,
        saved: Long,
        title: String
    ): GoalRequest.Retirement? {

        val currentAge = _currentAge.value.toIntOrNull()
        val retirementAge = _retirementAge.value.toIntOrNull()
        val lifeExpectancy = _lifeExpectancy.value.toIntOrNull()
        val expense = _currentMonthlyExpense.value.toLongOrNull()
        val postReturn = _postRetirementReturn.value.toIntOrNull()

        if (currentAge == null || retirementAge == null || lifeExpectancy == null || expense == null || postReturn == null) return null

        return GoalRequest.Retirement(
            currentAge = currentAge,
            retirementAge = retirementAge,
            lifeExpectancy = lifeExpectancy,
            currentMonthlyExpense = expense,
            postRetirementReturn = postReturn,
            inflationRate = inflation,
            returnRate = returns,
            currentSavedAmount = saved,
            yearsToGoal = retirementAge - currentAge,
            title = title
        )
    }

    private fun createWealthGoal(
        option: GoalOption,
        inflation: Int,
        returns: Int,
        saved: Long,
        title: String
    ): GoalRequest.WealthBuildingGoal? {

        val name = _goalName.value.ifBlank { option.title }

        val itemId = option.goalItemId ?: 1

        val itemName = _goalItemNameInput.value
            .ifBlank { option.goalItemName ?: option.title }

        val years = _targetYear.value.toIntOrNull()?.minus(DateTimeUtils.getCurrentYear())
        val cost = _currentGoalCost.value.toLongOrNull()

        if (years == null || cost == null) return null

        return GoalRequest.WealthBuildingGoal(
            goalName = name,
            goalItemId = itemId,
            goalItemName = itemName,
            yearsToGoal = years,
            currentGoalCost = cost,
            inflationRate = inflation,
            returnRate = returns,
            currentSavedAmount = saved,
            title = title
        )
    }
}

val goalOptions = listOf(

    // Child Goals
    GoalOption(
        title = "Child Education",
        color = bgColor1,
        type = GoalType.ChildEducation
    ),
    GoalOption(
        title = "Child Marriage",
        color = Secondary,
        type = GoalType.ChildMarriage
    ),

    // Retirement
    GoalOption(
        title = "Retirement",
        color = bgColor4,
        type = GoalType.Retirement
    ),

    // Wealth Goals (IMPORTANT)
    GoalOption(
        title = "Wealth Building",
        color = homeGoalColor,
        type = GoalType.WealthBuilding,
        goalItemId = 1,
        goalItemName = "General Wealth"
    ),

    GoalOption(
        title = "Custom Goal",
        color = Primary,
        type = GoalType.WealthBuilding,
        goalItemId = 2,
        goalItemName = "Custom"
    )

)