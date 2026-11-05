package org.sharad.velvetinvestment.features.onboarding.data.model

import kotlinx.serialization.Serializable
import org.sharad.velvetinvestment.features.onboarding.domain.model.InvestorProfileSubmission

/** `POST /onboarding/investor-profile/`. */
@Serializable
data class InvestorProfileResponseDto(
    val success: Boolean = false,
    val message: String? = null,
    val data: InvestorProfileDataDto? = null
)

@Serializable
data class InvestorProfileDataDto(
    val kyc_under_review: Boolean = false,
    val can_navigate_home: Boolean = false,
    val kyc_form: InvestorProfileKycFormDto? = null,
    val onboarding: OnboardingDto? = null
)

/** Present when the profile leaves a KYC form to sign, e.g. `status: awaiting_esign`. */
@Serializable
data class InvestorProfileKycFormDto(
    val status: String? = null,
    val fields_needed: List<String> = emptyList(),
    val esign_url: String? = null
)

fun InvestorProfileResponseDto.toDomain() = InvestorProfileSubmission(
    esignUrl = data?.kyc_form?.esign_url,
    hasKycForm = data?.kyc_form != null,
    kycFormStatus = data?.kyc_form?.status,
    kycUnderReview = data?.kyc_under_review ?: false,
    canNavigateHome = data?.can_navigate_home ?: false
)
