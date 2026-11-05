package org.sharad.velvetinvestment.features.onboarding.domain.usecases

import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse
import org.sharad.velvetinvestment.features.onboarding.domain.model.OnboardingStatus
import org.sharad.velvetinvestment.features.onboarding.domain.repository.OnboardingRepo

class SkipPANVerificationUseCase(
    private val onboardingRepo: OnboardingRepo
) {
    suspend operator fun invoke(): NetworkResponse<OnboardingStatus, ErrorDomain> {
        return onboardingRepo.skipPan()
    }
}
