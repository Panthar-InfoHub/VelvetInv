package org.sharad.velvetinvestment.features.onboarding.domain.usecases

import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse
import org.sharad.velvetinvestment.features.onboarding.domain.model.KYCError
import org.sharad.velvetinvestment.features.onboarding.domain.model.KycFormInitiation
import org.sharad.velvetinvestment.features.onboarding.domain.model.KycFormStatus
import org.sharad.velvetinvestment.features.onboarding.domain.repository.OnboardingRepo

class InitiateKycFormUseCase(
    private val onboardingRepo: OnboardingRepo
) {
    suspend operator fun invoke(): NetworkResponse<KycFormInitiation, ErrorDomain> {
        return onboardingRepo.initiateKycForm()
    }
}

class GetKycFormStatusUseCase(
    private val onboardingRepo: OnboardingRepo
) {
    suspend operator fun invoke(): NetworkResponse<KycFormStatus, KYCError> {
        return onboardingRepo.getKycFormStatus()
    }
}

class UploadKycFormSignatureUseCase(
    private val onboardingRepo: OnboardingRepo
) {
    suspend operator fun invoke(
        imageBytes: ByteArray,
        mimeType: String
    ): NetworkResponse<Unit, ErrorDomain> {
        return onboardingRepo.uploadKycFormSignature(imageBytes, mimeType)
    }
}

