package org.sharad.velvetinvestment.shared.domain.models

/**
 * What the app asks of a user who is not yet cleared to invest. Worked out in
 * [UserDataDomain.onboardingPrompt].
 */
enum class OnboardingPrompt {
    /** KYC form not filled in yet (`is_kyc_completed` false). Opens onboarding. */
    COMPLETE_KYC,

    /**
     * KYC filled in and being verified (`kyc_under_review`), or filled in but not yet verified.
     * There is nothing for the user to do until it clears, so this is shown but not actionable.
     */
    KYC_IN_PROGRESS,

    /**
     * KYC filled in, not under review, `onboarding.kyc_status` FAILED. The server moves the stage
     * back, so this opens onboarding like the other actionable prompts.
     */
    KYC_FAILED,

    /** KYC verified (`onboarding.kyc_status` VERIFIED); the finalization steps are left. */
    COMPLETE_REMAINING,

    /** Onboarding finished (`onboarding.is_completed`); nothing to show. */
    NONE;

    /** Whether the prompt leads into the onboarding flow. */
    val opensOnboarding: Boolean
        get() = this == COMPLETE_KYC || this == KYC_FAILED || this == COMPLETE_REMAINING
}
