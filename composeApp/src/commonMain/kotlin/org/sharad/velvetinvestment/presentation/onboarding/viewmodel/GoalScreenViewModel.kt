package org.sharad.velvetinvestment.presentation.onboarding.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.sharad.velvetinvestment.domain.GoalTypes
import org.sharad.velvetinvestment.presentation.onboarding.models.GoalInfo
import org.sharad.velvetinvestment.utils.parseSafeLong

class GoalScreenViewModel : ViewModel() {

    private val _goalList = MutableStateFlow<List<GoalInfo>>(emptyList())
    val goalList = _goalList.asStateFlow()

    private val _addGoalDetails = MutableStateFlow(GoalInfo())
    val addGoalDetails = _addGoalDetails.asStateFlow()

    val totalTargetAmount = _goalList.map { list ->
        list.sumOf { it.targetAmount ?: 0L }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = 0L
    )

    fun onGoalNameUpdate(name: String) {
        _addGoalDetails.value = _addGoalDetails.value.copy(
            name = name
        )
    }

    fun onGoalCategoryUpdate(category: GoalTypes) {
        _addGoalDetails.value = _addGoalDetails.value.copy(
            category = category
        )
    }

    fun onTargetAmountUpdate(amount: String) {
        _addGoalDetails.value = _addGoalDetails.value.let { current ->
            current.copy(
                targetAmount = parseSafeLong(amount, current.targetAmount)
            )
        }
    }

    fun onTargetYearUpdate(year: String) {
        _addGoalDetails.value = _addGoalDetails.value.copy(
            targetYear = year.toIntOrNull()
        )
    }

    fun addGoal() {
        _goalList.value = _goalList.value + _addGoalDetails.value
        _addGoalDetails.value = GoalInfo()
    }

    fun clearGoal() {
        _addGoalDetails.value = GoalInfo()
    }


    fun deleteGoal(goal: GoalInfo) {
        _goalList.value = _goalList.value.filterNot { it === goal }
    }


}
