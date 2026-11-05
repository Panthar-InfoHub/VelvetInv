package org.sharad.velvetinvestment.features.onboarding.domain.usecases

import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse
import org.sharad.velvetinvestment.features.onboarding.domain.model.Mandate
import org.sharad.velvetinvestment.features.onboarding.domain.model.MandateStatus
import org.sharad.velvetinvestment.features.onboarding.domain.repository.OnboardingRepo

class CreateMandateUseCase(
    private val onboardingRepo: OnboardingRepo
) {
    suspend operator fun invoke(
        mandateLimit: Long,
        validFrom: String,
        paymentPostbackUrl: String
    ): NetworkResponse<Mandate, ErrorDomain> {
        return onboardingRepo.createMandate(mandateLimit, validFrom, paymentPostbackUrl)
    }
}

class ConfirmMandateUseCase(
    private val onboardingRepo: OnboardingRepo
) {
    suspend operator fun invoke(mandateId: Int): NetworkResponse<MandateStatus, ErrorDomain> {
        return onboardingRepo.confirmMandate(mandateId)
    }
}
