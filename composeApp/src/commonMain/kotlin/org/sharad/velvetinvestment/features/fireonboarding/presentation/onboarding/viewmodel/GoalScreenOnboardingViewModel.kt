package org.sharad.velvetinvestment.features.fireonboarding.presentation.onboarding.viewmodel

import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toUpperCase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.sharad.emify.core.ui.theme.Primary
import org.sharad.emify.core.ui.theme.Secondary
import org.sharad.emify.core.ui.theme.bgColor1
import org.sharad.emify.core.ui.theme.bgColor4
import org.sharad.emify.core.ui.theme.homeGoalColor
import org.sharad.velvetinvestment.features.fireonboarding.presentation.onboarding.models.GoalType
import org.sharad.velvetinvestment.features.fireonboarding.presentation.onboarding.models.GoalRequest
import org.sharad.velvetinvestment.features.fireonboarding.presentation.onboarding.models.GoalOption
import org.sharad.velvetinvestment.utils.DateTimeUtils

/** The add-goal form; fields stay text so a half-typed value shows as typed. */
data class GoalForm(
    val selectedOption: GoalOption? = null,
    val inflationRate: String = "8",
    val returnRate: String = "10",
    val currentSavedAmount: String = "",
    val childName: String = "",
    val childAge: String = "",
    val targetYear: String = "",
    val currentGoalCost: String = "",
    /** Set from the date of birth, not typed, so clearing the form keeps it. */
    val currentAge: String = "",
    val retirementAge: String = "",
    val lifeExpectancy: String = "",
    val currentMonthlyExpense: String = "",
    val postRetirementReturn: String = "",
    val goalName: String = "",
    val goalItemName: String = ""
) {
    val isValid: Boolean
        get() {
            val option = selectedOption ?: return false
            if (inflationRate.toDoubleOrNull() == null || returnRate.toDoubleOrNull() == null) return false
            val currentYear = DateTimeUtils.getCurrentYear()
            return when (option.type) {
                GoalType.ChildEducation, GoalType.ChildMarriage -> {
                    val age = childAge.toIntOrNull()
                    val year = targetYear.toIntOrNull()
                    childName.isNotBlank() &&
                            age != null && age in 1..99 &&
                            year != null && year >= currentYear &&
                            currentGoalCost.toLongOrNull() != null
                }
                GoalType.Retirement -> {
                    val age = currentAge.toIntOrNull()
                    val retireAt = retirementAge.toIntOrNull()
                    val expectancy = lifeExpectancy.toIntOrNull()
                    age != null && retireAt != null && expectancy != null &&
                            currentMonthlyExpense.toLongOrNull() != null &&
                            postRetirementReturn.toDoubleOrNull() != null &&
                            retireAt > age && expectancy > retireAt
                }
                GoalType.WealthBuilding -> {
                    val year = targetYear.toIntOrNull()
                    year != null && currentGoalCost.toLongOrNull() != null && year >= currentYear
                }
            }
        }

    /** The form reset for the next goal; the age derived from the date of birth survives. */
    fun cleared(): GoalForm = GoalForm(currentAge = currentAge)
}

data class GoalOnboardingUiState(
    val goals: List<GoalRequest> = emptyList(),
    val form: GoalForm = GoalForm()
) {
    val totalTargetAmount: Long
        get() = goals.sumOf {
            when (it) {
                is GoalRequest.ChildEducation -> it.currentGoalCost
                is GoalRequest.ChildMarriage -> it.currentGoalCost
                is GoalRequest.WealthBuildingGoal -> it.currentGoalCost
                is GoalRequest.Retirement -> it.currentMonthlyExpense * 12L
            }
        }
}

sealed interface GoalOnboardingEvent {
    data class OnGoalOptionSelected(val option: GoalOption) : GoalOnboardingEvent
    data class OnGoalNameChanged(val value: String) : GoalOnboardingEvent
    data class OnGoalItemNameChanged(val value: String) : GoalOnboardingEvent
    data class OnInflationChanged(val value: String) : GoalOnboardingEvent
    data class OnReturnChanged(val value: String) : GoalOnboardingEvent
    data class OnSavedAmountChanged(val value: String) : GoalOnboardingEvent
    data class OnChildNameChanged(val value: String) : GoalOnboardingEvent
    data class OnChildAgeChanged(val value: String) : GoalOnboardingEvent
    data class OnTargetYearChanged(val value: String) : GoalOnboardingEvent
    data class OnGoalCostChanged(val value: String) : GoalOnboardingEvent
    data class OnCurrentAgeChanged(val value: String) : GoalOnboardingEvent
    data class OnRetirementAgeChanged(val value: String) : GoalOnboardingEvent
    data class OnLifeExpectancyChanged(val value: String) : GoalOnboardingEvent
    data class OnMonthlyExpenseChanged(val value: String) : GoalOnboardingEvent
    data class OnPostReturnChanged(val value: String) : GoalOnboardingEvent
    data object OnAddGoalOpened : GoalOnboardingEvent
    data object OnAddGoalClicked : GoalOnboardingEvent
    data class OnDeleteGoalClicked(val goal: GoalRequest) : GoalOnboardingEvent
}

sealed interface GoalOnboardingEffect {
    /** The goal was added; the add-goal screen closes. */
    data object GoalAdded : GoalOnboardingEffect
}

class GoalScreenOnboardingViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(GoalOnboardingUiState())
    val uiState = _uiState.asStateFlow()

    private val _effect = Channel<GoalOnboardingEffect>()
    val effect = _effect.receiveAsFlow()

    fun handleEvent(event: GoalOnboardingEvent) {
        when (event) {
            is GoalOnboardingEvent.OnGoalOptionSelected -> form {
                it.copy(
                    selectedOption = event.option,
                    goalName = event.option.title,
                    goalItemName = event.option.goalItemName ?: event.option.title
                )
            }
            is GoalOnboardingEvent.OnGoalNameChanged -> form { it.copy(goalName = event.value.toUpperCase(Locale.current)) }
            is GoalOnboardingEvent.OnGoalItemNameChanged -> form { it.copy(goalItemName = event.value.toUpperCase(Locale.current)) }
            is GoalOnboardingEvent.OnInflationChanged -> form { it.copy(inflationRate = event.value) }
            is GoalOnboardingEvent.OnReturnChanged -> form { it.copy(returnRate = event.value) }
            is GoalOnboardingEvent.OnSavedAmountChanged -> form { it.copy(currentSavedAmount = event.value) }
            is GoalOnboardingEvent.OnChildNameChanged -> form { it.copy(childName = event.value.toUpperCase(Locale.current)) }
            is GoalOnboardingEvent.OnChildAgeChanged -> form { it.copy(childAge = event.value) }
            is GoalOnboardingEvent.OnTargetYearChanged -> form { it.copy(targetYear = event.value) }
            is GoalOnboardingEvent.OnGoalCostChanged -> form { it.copy(currentGoalCost = event.value) }
            is GoalOnboardingEvent.OnCurrentAgeChanged -> form { it.copy(currentAge = event.value) }
            is GoalOnboardingEvent.OnRetirementAgeChanged -> form { it.copy(retirementAge = event.value) }
            is GoalOnboardingEvent.OnLifeExpectancyChanged -> form { it.copy(lifeExpectancy = event.value) }
            is GoalOnboardingEvent.OnMonthlyExpenseChanged -> form { it.copy(currentMonthlyExpense = event.value) }
            is GoalOnboardingEvent.OnPostReturnChanged -> form { it.copy(postRetirementReturn = event.value) }
            GoalOnboardingEvent.OnAddGoalOpened -> form { it.cleared() }
            GoalOnboardingEvent.OnAddGoalClicked -> addGoal()
            is GoalOnboardingEvent.OnDeleteGoalClicked -> _uiState.update { state ->
                val index = state.goals.indexOfFirst { it == event.goal }
                if (index == -1) state else state.copy(goals = state.goals.toMutableList().apply { removeAt(index) })
            }
        }
    }

    private fun form(change: (GoalForm) -> GoalForm) {
        _uiState.update { it.copy(form = change(it.form)) }
    }

    private fun addGoal() {
        val form = _uiState.value.form
        val goal = form.toGoalRequest() ?: return
        _uiState.update { it.copy(goals = it.goals + goal, form = it.form.cleared()) }
        viewModelScope.launch { _effect.send(GoalOnboardingEffect.GoalAdded) }
    }
}

/** The goal [GoalForm] describes, or null while something it needs is missing or unreadable. */
private fun GoalForm.toGoalRequest(): GoalRequest? {
    val option = selectedOption ?: return null
    val inflation = inflationRate.toDoubleOrNull() ?: return null
    val returns = returnRate.toDoubleOrNull() ?: return null
    val saved = currentSavedAmount.toLongOrNull() ?: 0L
    val title = option.title
    val yearsToGoal = targetYear.toIntOrNull()?.minus(DateTimeUtils.getCurrentYear())
    val cost = currentGoalCost.toLongOrNull()

    return when (option.type) {
        GoalType.ChildEducation, GoalType.ChildMarriage -> {
            val age = childAge.toIntOrNull()
            if (childName.isBlank() || age == null || yearsToGoal == null || cost == null) return null
            if (option.type == GoalType.ChildEducation) {
                GoalRequest.ChildEducation(
                    childName = childName, childAge = age, yearsToGoal = yearsToGoal,
                    currentGoalCost = cost, inflationRate = inflation, returnRate = returns,
                    currentSavedAmount = saved, title = title
                )
            } else {
                GoalRequest.ChildMarriage(
                    childName = childName, childAge = age, yearsToGoal = yearsToGoal,
                    currentGoalCost = cost, inflationRate = inflation, returnRate = returns,
                    currentSavedAmount = saved, title = title
                )
            }
        }
        GoalType.Retirement -> {
            val age = currentAge.toIntOrNull() ?: return null
            val retireAt = retirementAge.toIntOrNull() ?: return null
            GoalRequest.Retirement(
                currentAge = age,
                retirementAge = retireAt,
                lifeExpectancy = lifeExpectancy.toIntOrNull() ?: return null,
                currentMonthlyExpense = currentMonthlyExpense.toLongOrNull() ?: return null,
                postRetirementReturn = postRetirementReturn.toDoubleOrNull() ?: return null,
                inflationRate = inflation,
                returnRate = returns,
                currentSavedAmount = saved,
                yearsToGoal = retireAt - age,
                title = title
            )
        }
        GoalType.WealthBuilding -> {
            if (yearsToGoal == null || cost == null) return null
            GoalRequest.WealthBuildingGoal(
                goalName = goalName.ifBlank { option.title },
                goalItemId = option.goalItemId ?: 1,
                goalItemName = goalItemName.ifBlank { option.goalItemName ?: option.title },
                yearsToGoal = yearsToGoal,
                currentGoalCost = cost,
                inflationRate = inflation,
                returnRate = returns,
                currentSavedAmount = saved,
                title = title
            )
        }
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