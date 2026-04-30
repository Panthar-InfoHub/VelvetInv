package org.sharad.velvetinvestment.domain.usecases.user

import org.sharad.velvetinvestment.domain.models.user.PANVerifyDomain
import org.sharad.velvetinvestment.domain.repository.UserAuth
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse

class VerifyPANUseCase(
    private val userAuth: UserAuth
) {
    suspend operator fun invoke(pan: String): NetworkResponse<PANVerifyDomain, ErrorDomain> {
        return userAuth.verifyPAN(pan)
    }
}