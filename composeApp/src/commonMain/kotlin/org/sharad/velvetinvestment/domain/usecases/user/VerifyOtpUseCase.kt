package org.sharad.velvetinvestment.domain.usecases.user

import org.sharad.velvetinvestment.domain.models.auth.LoginDomain
import org.sharad.velvetinvestment.domain.repository.UserAuth
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse

class VerifyOtpUseCase(
    private val userAuth: UserAuth
) {
    suspend operator fun invoke(number: String, otp: String): NetworkResponse<LoginDomain, ErrorDomain> {
        return userAuth.verifyOTP(number, otp)
    }
}