package org.sharad.velvetinvestment.utils.GoalUtils

import org.sharad.velvetinvestment.domain.models.goals.GoalRequest

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