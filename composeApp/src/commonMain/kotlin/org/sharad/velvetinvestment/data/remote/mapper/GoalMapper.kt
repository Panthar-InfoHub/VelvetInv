package org.sharad.velvetinvestment.data.remote.mapper

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.encodeToJsonElement
import org.sharad.velvetinvestment.domain.models.goals.GoalRequest
import org.sharad.velvetinvestment.data.remote.model.onboarding.*

fun GoalRequest.toDto(): GoalRequestDto {
    return when (this) {

        is GoalRequest.ChildEducation -> {
            ChildEducationGoalRequest(
                goal_type_id = goalTypeId,
                inflation_rate = inflationRate,
                return_rate = returnRate,
                current_saved_amount = currentSavedAmount,
                child_name = childName,
                child_age = childAge,
                years_left = yearsToGoal,
                current_goal_cost = currentGoalCost
            )
        }

        is GoalRequest.ChildMarriage -> {
            ChildMarriageGoalRequest(
                goal_type_id = goalTypeId,
                inflation_rate = inflationRate,
                return_rate = returnRate,
                current_saved_amount = currentSavedAmount,
                child_name = childName,
                child_age = childAge,
                years_left = yearsToGoal,
                current_goal_cost = currentGoalCost
            )
        }

        is GoalRequest.Retirement -> {
            RetirementGoalRequest(
                goal_type_id = goalTypeId,
                inflation_rate = inflationRate,
                return_rate = returnRate,
                current_saved_amount = currentSavedAmount,
                current_age = currentAge,
                retirement_age = retirementAge,
                life_expectancy = lifeExpectancy,
                current_monthly_expense = currentMonthlyExpense,
                post_retirement_return = postRetirementReturn
            )
        }

        is GoalRequest.WealthBuildingGoal -> {
            CustomGoalRequest(
                goal_type_id = goalTypeId,
                inflation_rate = inflationRate,
                return_rate = returnRate,
                current_saved_amount = currentSavedAmount,
                goal_name = goalName,
                goal_item_id = goalItemId,
                goal_item_name = goalItemName,
                years_left = yearsToGoal,
                current_goal_cost = currentGoalCost
            )
        }
    }
}



fun GoalRequestDto.toJsonElement(): JsonElement {
    val json = Json{
        encodeDefaults=true
    }
    return when (this) {
        is ChildEducationGoalRequest -> {
            json.encodeToJsonElement(this)
        }
        is ChildMarriageGoalRequest -> json.encodeToJsonElement(this)
        is RetirementGoalRequest -> json.encodeToJsonElement(this)
        is CustomGoalRequest -> json.encodeToJsonElement(this)
        else -> error("Unknown goal type")
    }
}

