package org.sharad.velvetinvestment.features.goals.data.repository

import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse
import org.sharad.velvetinvestment.utils.networking.getUrl
import org.sharad.velvetinvestment.utils.networking.safeRequest
import org.sharad.velvetinvestment.utils.networking.safeUnitRequest
import org.sharad.velvetinvestment.features.goals.data.mapper.toCalculateBody
import org.sharad.velvetinvestment.features.goals.data.mapper.toCreateBody
import org.sharad.velvetinvestment.features.goals.data.mapper.toDomain
import org.sharad.velvetinvestment.features.goals.data.remote.model.calculate.GoalCalculationResponseDto
import org.sharad.velvetinvestment.features.goals.data.remote.model.config.GoalConfigResponseDto
import org.sharad.velvetinvestment.features.goals.data.remote.model.goalmapping.GoalMapBodyDto
import org.sharad.velvetinvestment.features.goals.data.remote.model.goalmapping.GoalUnMapBodyDto
import org.sharad.velvetinvestment.features.goals.data.remote.model.usergoal.UserGoalDto
import org.sharad.velvetinvestment.features.goals.data.remote.model.usergoal.UserGoalsListDto
import org.sharad.velvetinvestment.features.goals.domain.models.CreateGoalRequest
import org.sharad.velvetinvestment.features.goals.domain.models.GoalCalculationDomain
import org.sharad.velvetinvestment.features.goals.domain.models.GoalCalculationRequest
import org.sharad.velvetinvestment.features.goals.domain.models.GoalConfigDomain
import org.sharad.velvetinvestment.features.goals.domain.models.GoalDomain
import org.sharad.velvetinvestment.features.goals.domain.repository.GoalsRepository

/**
 * The `v2.0` goal endpoints, all under `/user-goal`. The server owns the goal arithmetic: this
 * class only carries inputs out and projections back.
 */
class GoalsRepositoryImpl(
    private val client: HttpClient
) : GoalsRepository {

    override suspend fun getGoalConfig(): NetworkResponse<List<GoalConfigDomain>, ErrorDomain> {
        val response = safeRequest<GoalConfigResponseDto> {
            client.get(getUrl("/user-goal/config"))
        }
        return when (response) {
            is NetworkResponse.Error -> NetworkResponse.Error(response.error)
            is NetworkResponse.Success -> NetworkResponse.Success(
                response.data.data.map { it.toDomain() }
            )
        }
    }

    override suspend fun calculateGoal(
        request: GoalCalculationRequest
    ): NetworkResponse<GoalCalculationDomain, ErrorDomain> {
        val response = safeRequest<GoalCalculationResponseDto> {
            client.post(getUrl("/user-goal/calculate")) {
                setBody(request.toCalculateBody())
            }
        }
        return when (response) {
            is NetworkResponse.Error -> NetworkResponse.Error(response.error)
            is NetworkResponse.Success -> NetworkResponse.Success(response.data.data.toDomain())
        }
    }

    override suspend fun createGoal(request: CreateGoalRequest): NetworkResponse<Unit, ErrorDomain> {
        return safeUnitRequest {
            client.post(getUrl("/user-goal/")) {
                setBody(request.toCreateBody())
            }
        }
    }

    override suspend fun getAllGoals(): NetworkResponse<List<GoalDomain>, ErrorDomain> {
        val response = safeRequest<UserGoalsListDto> {
            client.get(getUrl("/user-goal/"))
        }
        return when (response) {
            is NetworkResponse.Error -> NetworkResponse.Error(response.error)
            is NetworkResponse.Success -> NetworkResponse.Success(
                response.data.data.map { it.toDomain() }
            )
        }
    }

    override suspend fun getGoalById(id: String): NetworkResponse<GoalDomain, ErrorDomain> {
        val response = safeRequest<UserGoalDto> {
            client.get(getUrl("/user-goal/$id"))
        }
        return when (response) {
            is NetworkResponse.Error -> NetworkResponse.Error(response.error)
            is NetworkResponse.Success -> NetworkResponse.Success(response.data.data.toDomain())
        }
    }

    override suspend fun deleteGoal(goalId: String): NetworkResponse<Unit, ErrorDomain> {
        return safeUnitRequest {
            client.delete(getUrl("/user-goal/$goalId"))
        }
    }

    override suspend fun mapHoldings(
        goalId: String,
        holdingIds: List<String>
    ): NetworkResponse<Unit, ErrorDomain> {
        return safeUnitRequest {
            client.post(getUrl("/user-goal/map")) {
                setBody(GoalMapBodyDto(goal_id = goalId, holding_ids = holdingIds))
            }
        }
    }

    override suspend fun removeHolding(
        goalId: String,
        holdingId: String
    ): NetworkResponse<Unit, ErrorDomain> {
        return safeUnitRequest {
            client.post(getUrl("/user-goal/remove")) {
                setBody(GoalUnMapBodyDto(goal_id = goalId, holding_id = holdingId))
            }
        }
    }
}
