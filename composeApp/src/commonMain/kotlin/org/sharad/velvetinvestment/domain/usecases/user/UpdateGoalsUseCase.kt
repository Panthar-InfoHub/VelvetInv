package org.sharad.velvetinvestment.domain.usecases.user

import org.sharad.velvetinvestment.data.remote.model.updateuserdata.GoalsUpdateDto
import org.sharad.velvetinvestment.domain.repository.UserAuth
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse

class UpdateGoalsUseCase(
    private val userAuth: UserAuth
) {
    suspend operator fun invoke(data: GoalsUpdateDto): NetworkResponse<Unit, ErrorDomain> {
        return userAuth.updateGoals(data)
    }
}