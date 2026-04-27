package org.sharad.velvetinvestment.data.remote.repository

import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import org.sharad.velvetinvestment.data.remote.mapper.toDomain
import org.sharad.velvetinvestment.data.remote.model.addgoals.ChildGoalBodyDto
import org.sharad.velvetinvestment.data.remote.model.addgoals.RetirementGoalBodyDto
import org.sharad.velvetinvestment.data.remote.model.addgoals.WealthBuildingGoalBodyDto
import org.sharad.velvetinvestment.data.remote.model.firereport.FireReportDto
import org.sharad.velvetinvestment.domain.models.fire.FireReportDomain
import org.sharad.velvetinvestment.domain.models.goals.GoalRequest
import org.sharad.velvetinvestment.domain.repository.UserFinance
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse
import org.sharad.velvetinvestment.utils.networking.getUrl
import org.sharad.velvetinvestment.utils.networking.safeRequest

class UserFinanceRepo(
    private val client: HttpClient
): UserFinance {

    override suspend fun getFireReport(): NetworkResponse<FireReportDomain, ErrorDomain> {
        val response= safeRequest<FireReportDto> {
            client.get(getUrl("/fire-report")) {
                parameter("include_emi", true)
                parameter("projection_years",20)
            }
        }
        return when (response) {
            is NetworkResponse.Success -> {
                NetworkResponse.Success(
                    response.data.toDomain()
                )
            }
            is NetworkResponse.Error -> {
                NetworkResponse.Error(response.error)
            }
        }
    }

    override suspend fun getFirePdf(): NetworkResponse<ByteArray, ErrorDomain> {
        return safeRequest<ByteArray>{
            client.get(getUrl("/fire-report/pdf")) {
            }
        }
    }

    override suspend fun addChildMarriageGoal(goal: GoalRequest.ChildMarriage): NetworkResponse<Unit, ErrorDomain> {
        val body= ChildGoalBodyDto(
            child_age = goal.childAge,
            child_name = goal.childName,
            current_goal_cost = goal.currentGoalCost,
            current_saved_amount = goal.currentSavedAmount,
            goal_type_id = goal.goalTypeId,
            inflation_rate = goal.inflationRate.toDouble(),
            return_rate = goal.returnRate,
            years_left = goal.yearsToGoal
        )
        return safeRequest<Unit> {
            client.post(getUrl("/user-goal")) {
                setBody(body)
            }
        }
    }

    override suspend fun addChildEducationGoal(goal: GoalRequest.ChildEducation): NetworkResponse<Unit, ErrorDomain> {
        val body= ChildGoalBodyDto(
            child_age = goal.childAge,
            child_name = goal.childName,
            current_goal_cost = goal.currentGoalCost,
            current_saved_amount = goal.currentSavedAmount,
            goal_type_id = goal.goalTypeId,
            inflation_rate = goal.inflationRate.toDouble(),
            return_rate = goal.returnRate,
            years_left = goal.yearsToGoal
        )
        return safeRequest<Unit> {
            client.post(getUrl("/user-goal")) {
                setBody(body)
            }
        }
    }

    override suspend fun addRetirementGoal(goal: GoalRequest.Retirement): NetworkResponse<Unit, ErrorDomain> {
        val body= RetirementGoalBodyDto(
            current_age = goal.currentAge,
            current_monthly_expense = goal.currentMonthlyExpense,
            current_saved_amount = goal.currentSavedAmount,
            goal_type_id = goal.goalTypeId,
            inflation_rate = goal.inflationRate.toDouble(),
            life_expectancy = goal.lifeExpectancy,
            post_retirement_return = goal.postRetirementReturn.toDouble(),
            retirement_age = goal.retirementAge,
            return_rate = goal.returnRate.toDouble()
        )
        return safeRequest<Unit> {
            client.post(getUrl("/user-goal")) {
                setBody(body)
            }
        }
    }

    override suspend fun addWealthBuildingGoal(goal: GoalRequest.WealthBuildingGoal): NetworkResponse<Unit, ErrorDomain> {
        val body= WealthBuildingGoalBodyDto(
            current_goal_cost = goal.currentGoalCost,
            current_saved_amount = goal.currentSavedAmount,
            goal_item_id = goal.goalItemId,
            goal_item_name = goal.goalItemName,
            goal_name = goal.goalName,
            goal_type_id = goal.goalTypeId,
            inflation_rate = goal.inflationRate.toDouble(),
            return_rate = goal.returnRate.toDouble(),
            years_left = goal.yearsToGoal
        )
        return safeRequest<Unit> {
            client.post(getUrl("/user-goal")) {
                setBody(body)
            }
        }
    }

    override suspend fun deleteGoal(goalId: String): NetworkResponse<Unit, ErrorDomain> {
        return safeRequest<Unit> {
            client.delete(getUrl("/user-goal/$goalId"))
        }
    }
}