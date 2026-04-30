package org.sharad.velvetinvestment.domain.usecases.user

import org.sharad.velvetinvestment.domain.repository.UserAuth
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse

class LoginWithNumberUseCase(
    private val userAuth: UserAuth
) {
    suspend operator fun invoke(number: String): NetworkResponse<Unit, ErrorDomain> {
        return userAuth.loginWithNumber(number)
    }
}