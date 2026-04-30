package org.sharad.velvetinvestment.domain.usecases.user

import org.sharad.velvetinvestment.domain.repository.UserAuth
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse

class SignOutUseCase(
    private val userAuth: UserAuth
) {
    suspend operator fun invoke(): NetworkResponse<Unit, ErrorDomain> {
        return userAuth.signOut()
    }
}