package org.sharad.velvetinvestment.domain.usecases.userfinance

import org.sharad.velvetinvestment.domain.models.goals.GoalDomain
import org.sharad.velvetinvestment.domain.repository.UserFinance
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse

class GetGoalByIdUseCase(
    private val repository: UserFinance
) {
    suspend operator fun invoke(id: String): NetworkResponse<GoalDomain, ErrorDomain> {
        return repository.getGoalById(id)
    }
}
