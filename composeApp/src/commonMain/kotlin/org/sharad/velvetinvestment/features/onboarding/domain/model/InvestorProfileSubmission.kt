package org.sharad.velvetinvestment.features.onboarding.domain.model

/** What `POST /onboarding/investor-profile/` reports back once the profile is saved. */
data class InvestorProfileSubmission(
    /** The KYC form's eSign link; null or blank when there is nothing to sign. */
    val esignUrl: String?,
    /** False when the response carries no `kyc_form` at all, as on the verified-PAN branch. */
    val hasKycForm: Boolean,
    /** `kyc_form.status`, e.g. `awaiting_esign` or `submitted`. */
    val kycFormStatus: String?,
    val kycUnderReview: Boolean,
    val canNavigateHome: Boolean
) {
    /** The KYC form has gone through with nothing left to sign. */
    val isKycFormSubmitted: Boolean
        get() = kycFormStatus.equals(KYC_FORM_SUBMITTED, ignoreCase = true)

    companion object {
        const val KYC_FORM_SUBMITTED = "submitted"
    }
}
