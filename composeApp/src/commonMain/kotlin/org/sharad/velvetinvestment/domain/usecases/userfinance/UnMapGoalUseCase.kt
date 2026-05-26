package org.sharad.velvetinvestment.domain.usecases.userfinance

import org.sharad.velvetinvestment.domain.repository.UserFinance
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse

class UnMapGoalUseCase(
    private val repository: UserFinance
) {
    suspend operator fun invoke(goalId: Int): NetworkResponse<Unit, ErrorDomain> {
        return repository.unMapGoal(goalId)
    }
}
