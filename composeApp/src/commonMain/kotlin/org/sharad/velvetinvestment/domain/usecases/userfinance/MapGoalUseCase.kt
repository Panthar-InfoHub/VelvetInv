package org.sharad.velvetinvestment.domain.usecases.userfinance

import org.sharad.velvetinvestment.data.remote.model.goalmapping.GoalMapBodyDto
import org.sharad.velvetinvestment.domain.repository.UserFinance
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse

class MapGoalUseCase(
    private val repository: UserFinance
) {
    suspend operator fun invoke(body: GoalMapBodyDto): NetworkResponse<Unit, ErrorDomain> {
        return repository.mapGoal(body)
    }
}
