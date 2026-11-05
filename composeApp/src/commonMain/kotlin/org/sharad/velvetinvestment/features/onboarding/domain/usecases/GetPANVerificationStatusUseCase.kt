package org.sharad.velvetinvestment.features.onboarding.domain.usecases

import org.sharad.velvetinvestment.utils.networking.NetworkResponse
import org.sharad.velvetinvestment.features.onboarding.domain.model.PANVerificationError
import org.sharad.velvetinvestment.features.onboarding.domain.repository.OnboardingRepo

class GetPANVerificationStatusUseCase(
    private val onboardingRepo: OnboardingRepo
) {
    suspend operator fun invoke(): NetworkResponse<Unit, PANVerificationError> {
        return onboardingRepo.getPANVerificationStatus()
    }
}
