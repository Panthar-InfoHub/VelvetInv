package org.sharad.velvetinvestment.features.onboarding.domain.usecases

import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse
import org.sharad.velvetinvestment.features.onboarding.domain.model.PrefilledBankDetails
import org.sharad.velvetinvestment.features.onboarding.domain.model.ReversePennyDropLinks
import org.sharad.velvetinvestment.features.onboarding.domain.model.ReversePennyDropStatus
import org.sharad.velvetinvestment.features.onboarding.domain.repository.OnboardingRepo

class GetPrefilledBankDetailsUseCase(
    private val onboardingRepo: OnboardingRepo
) {
    suspend operator fun invoke(): NetworkResponse<PrefilledBankDetails?, ErrorDomain> {
        return onboardingRepo.getPrefilledBankDetails()
    }
}

class InitiateReversePennyDropUseCase(
    private val onboardingRepo: OnboardingRepo
) {
    suspend operator fun invoke(): NetworkResponse<ReversePennyDropLinks, ErrorDomain> {
        return onboardingRepo.initiateReversePennyDrop()
    }
}

class GetReversePennyDropStatusUseCase(
    private val onboardingRepo: OnboardingRepo
) {
    suspend operator fun invoke(): NetworkResponse<ReversePennyDropStatus, ErrorDomain> {
        return onboardingRepo.getReversePennyDropStatus()
    }
}
