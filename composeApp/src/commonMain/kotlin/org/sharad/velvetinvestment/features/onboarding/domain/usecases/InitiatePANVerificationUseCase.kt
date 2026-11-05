package org.sharad.velvetinvestment.features.onboarding.domain.usecases

import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse
import org.sharad.velvetinvestment.features.onboarding.domain.repository.OnboardingRepo

class InitiatePANVerificationUseCase(
    private val onboardingRepo: OnboardingRepo
) {
    suspend operator fun invoke(
        pan: String
    ): NetworkResponse<Unit, ErrorDomain> {
        return onboardingRepo.initiatePan(pan = pan)
    }
}
