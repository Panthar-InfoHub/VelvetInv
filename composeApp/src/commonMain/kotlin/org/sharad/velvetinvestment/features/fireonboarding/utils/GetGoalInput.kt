package org.sharad.velvetinvestment.features.fireonboarding.utils

import org.sharad.velvetinvestment.features.fireonboarding.presentation.onboarding.models.GoalRequest

fun GoalRequest.getGoalInputs(): Triple<Long, Double, Int> {
    return when (this) {
        is GoalRequest.ChildEducation -> Triple(currentGoalCost, inflationRate, yearsToGoal)
        is GoalRequest.ChildMarriage -> Triple(currentGoalCost, inflationRate, yearsToGoal)
        is GoalRequest.WealthBuildingGoal -> Triple(currentGoalCost, inflationRate, yearsToGoal)

        is GoalRequest.Retirement -> {
            val years = yearsToGoal
            val yearlyExpense = currentMonthlyExpense * 12
            Triple(yearlyExpense, inflationRate, years)
        }
    }
}