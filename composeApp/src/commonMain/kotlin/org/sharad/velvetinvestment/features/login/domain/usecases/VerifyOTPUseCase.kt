package org.sharad.velvetinvestment.features.login.domain.usecases

import org.sharad.velvetinvestment.features.login.domain.repository.UserAuth

class VerifyOTPUseCase(private val repository: UserAuth) {
    suspend operator fun invoke(number: String, otp: String) = repository.verifyOTP(number, otp)
}
