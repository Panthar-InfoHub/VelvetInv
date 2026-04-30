package org.sharad.velvetinvestment.domain.usecases.user

import org.sharad.velvetinvestment.data.remote.model.useedata.UserDataDto
import org.sharad.velvetinvestment.domain.repository.UserAuth
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse

class GetUserDataUseCase(
    private val userAuth: UserAuth
) {
    suspend operator fun invoke(): NetworkResponse<UserDataDto, ErrorDomain> {
        return userAuth.getUserData()
    }
}