package org.sharad.velvetinvestment.features.goals.domain.repository

import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse
import org.sharad.velvetinvestment.features.goals.domain.models.CreateGoalRequest
import org.sharad.velvetinvestment.features.goals.domain.models.GoalCalculationDomain
import org.sharad.velvetinvestment.features.goals.domain.models.GoalCalculationRequest
import org.sharad.velvetinvestment.features.goals.domain.models.GoalConfigDomain
import org.sharad.velvetinvestment.features.goals.domain.models.GoalDomain

interface GoalsRepository {

    /** The goal types on offer, and the rates, tenures and costs each is sized with. */
    suspend fun getGoalConfig(): NetworkResponse<List<GoalConfigDomain>, ErrorDomain>

    /** What a goal would require, for inputs the user has not saved yet. */
    suspend fun calculateGoal(
        request: GoalCalculationRequest
    ): NetworkResponse<GoalCalculationDomain, ErrorDomain>

    suspend fun createGoal(request: CreateGoalRequest): NetworkResponse<Unit, ErrorDomain>

    suspend fun getAllGoals(): NetworkResponse<List<GoalDomain>, ErrorDomain>

    suspend fun getGoalById(id: String): NetworkResponse<GoalDomain, ErrorDomain>

    suspend fun deleteGoal(goalId: String): NetworkResponse<Unit, ErrorDomain>

    /** Maps holdings the user already owns to a goal. */
    suspend fun mapHoldings(
        goalId: String,
        holdingIds: List<String>
    ): NetworkResponse<Unit, ErrorDomain>

    /** Removes one mapped holding from a goal. */
    suspend fun removeHolding(
        goalId: String,
        holdingId: String
    ): NetworkResponse<Unit, ErrorDomain>
}
