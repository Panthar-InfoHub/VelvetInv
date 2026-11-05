package org.sharad.velvetinvestment.features.onboarding.domain.usecases

import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse
import org.sharad.velvetinvestment.features.onboarding.domain.model.EmailVerification
import org.sharad.velvetinvestment.features.onboarding.domain.model.OnboardingStatus
import org.sharad.velvetinvestment.features.onboarding.domain.repository.OnboardingRepo

/** Sends the code, and re-sends it when the user asks for another one from the OTP screen. */
class RequestEmailOtpUseCase(
    private val onboardingRepo: OnboardingRepo
) {
    suspend operator fun invoke(
        email: String
    ): NetworkResponse<OnboardingStatus, ErrorDomain> {
        return onboardingRepo.requestEmailOtp(email.trim())
    }
}

class VerifyEmailOtpUseCase(
    private val onboardingRepo: OnboardingRepo
) {
    suspend operator fun invoke(
        otp: String
    ): NetworkResponse<EmailVerification, ErrorDomain> {
        return onboardingRepo.verifyEmailOtp(otp)
    }
}
