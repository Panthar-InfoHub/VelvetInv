package org.sharad.velvetinvestment.shared.domain.models

import org.sharad.velvetinvestment.features.bottomNavigation.domain.models.GoalsSummaryDomain

/**
 * Everything `GET /user/` tells the app about the signed-in user: who they are, how far through
 * onboarding they got, and what their portfolio is worth.
 */
data class UserDataDomain(
    val id: String,
    val name: String,
    val email: String,
    val mobile: String,
    val dob: String,
    val city: String?,
    val goals: List<GoalsSummaryDomain>,
    val onboarding: OnboardingStatusDomain,
    /** The user chose to skip onboarding rather than finishing it. */
    val isSkipped: Boolean,
    /** The KYC form has been filled in. Says nothing about whether it has been verified. */
    val isKycCompleted: Boolean,
    /** The filled-in KYC is with the server for verification. */
    val isKycUnderReview: Boolean,
    val dashboard: DashboardSummaryDomain,
    /** Gateway ids, present only once KYC has cleared. */
    val investorProfile: String?,
    val investmentAccount: String?,
    val mpinIsSetup: Boolean,
    val mpinEnabled: Boolean,

    /** FIRE questionnaire answers; null until that step is filled in. */
    val finance: UserFinanceDomain?,
    val assets: UserAssetsDomain?,
    val insurance: UserInsuranceDomain?,
    val loans: List<UserLoanDomain>,
    /** The FIRE questionnaire is finished (top-level `is_fire_completed`). */
    val isFireCompleted: Boolean,
    /** Server-computed net worth and FIRE figures (`user_home_data`). */
    val homeData: UserHomeDataDomain = UserHomeDataDomain(),
) {
    val kycVerified: Boolean
        get() = onboarding.isKycVerified

    val totalLoanOutstanding: Long
        get() = loans.sumOf { it.outstandingAmount }

    val totalMonthlyEmi: Long
        get() = loans.sumOf { it.monthlyEmi }

    /** Assets less outstanding loans, as the server reports it in `user_home_data.net_worth`. */
    val netWorth: Long
        get() = homeData.netWorth

    /**
     * What stands between the user and investing. The home card, the profile KYC row and the KYC
     * popup all use this. Checked in order: onboarding finished → KYC form not filled → KYC under
     * review → KYC verified (finalization left) / rejected / not yet picked up for verification.
     */
    val onboardingPrompt: OnboardingPrompt
        get() = when {
            onboarding.isCompleted -> OnboardingPrompt.NONE
            !isKycCompleted -> OnboardingPrompt.COMPLETE_KYC
            isKycUnderReview -> OnboardingPrompt.KYC_IN_PROGRESS
            onboarding.isKycVerified -> OnboardingPrompt.COMPLETE_REMAINING
            onboarding.isKycFailed -> OnboardingPrompt.KYC_FAILED
            // Filled in, not under review and not yet verified: waiting on the server.
            else -> OnboardingPrompt.KYC_IN_PROGRESS
        }

    /** Cleared to invest: onboarding is finished. */
    val canInvest: Boolean
        get() = onboardingPrompt == OnboardingPrompt.NONE

    /**
     * Not reported by `GET /user/` any more — the trading account was removed from the onboarding
     * flow. Kept so the profile screen still compiles; it always reads as unverified.
     */
}

/**
 * Per-step onboarding progress. Statuses are compared case-insensitively because the server has
 * used both casings across endpoints.
 */
data class OnboardingStatusDomain(
    val currentStage: String,
    val basicDetailsStatus: String,
    val readinessStatus: String,
    val kycStatus: String,
    val pennyDropStatus: String,
    val emailStatus: String,
    val profileStatus: String,
    val nomineeStatus: String,
    val isCompleted: Boolean,
    val completedAt: String?,
    /** Shown when the KYC was rejected: the server's `reason`, or a generic line without one. */
    val kycFailureReason: String = "",
    val financeStatus: String = "",
    val assetsStatus: String = "",
    val loanStatus: String = "",
    val insuranceStatus: String = "",
    val goalsStatus: String = "",
    val isFireCompleted: Boolean = false,
    val fireCompletedAt: String? = null
) {
    val isFinanceDone: Boolean
        get() = financeStatus.isVerified

    val isAssetsDone: Boolean
        get() = assetsStatus.isVerified

    val isLoanDone: Boolean
        get() = loanStatus.isVerified

    val isInsuranceDone: Boolean
        get() = insuranceStatus.isVerified

    val isGoalsDone: Boolean
        get() = goalsStatus.isVerified

    val isKycVerified: Boolean
        get() = kycStatus.isVerified

    val isKycFailed: Boolean
        get() = kycStatus.equals(FAILED, ignoreCase = true)

    val isBankVerified: Boolean
        get() = pennyDropStatus.isVerified

    val isEmailVerified: Boolean
        get() = emailStatus.isVerified

    val isProfileComplete: Boolean
        get() = profileStatus.isVerified

    /** Skipping a nominee is a valid outcome, so it counts as settled rather than as pending. */
    val isNomineeSettled: Boolean
        get() = nomineeStatus.isVerified || nomineeStatus.equals(SKIPPED, ignoreCase = true)

    private val String.isVerified: Boolean
        get() = equals(VERIFIED, ignoreCase = true)

    companion object {
        const val VERIFIED = "VERIFIED"
        const val SKIPPED = "SKIPPED"
        const val PENDING = "PENDING"
        const val IN_PROGRESS = "IN_PROGRESS"
        const val COMPLETED = "COMPLETED"
        const val FAILED = "FAILED"

        val EMPTY = OnboardingStatusDomain(
            currentStage = "",
            basicDetailsStatus = "",
            readinessStatus = "",
            kycStatus = "",
            pennyDropStatus = "",
            emailStatus = "",
            profileStatus = "",
            nomineeStatus = "",
            isCompleted = false,
            completedAt = null
        )
    }
}

/** The portfolio figures the home screen shows, in rupees. */
data class DashboardSummaryDomain(
    val portfolioValue: Double = 0.0,
    val mutualFunds: Double = 0.0,
    val fixedDeposits: Double = 0.0,
    val totalReturns: Double = 0.0,
    val returnPercent: Double = 0.0,
    /** Null until there is a previous month to compare against. */
    val monthChangePercent: Double? = null
)

/** `user_home_data`: the home screen's net worth and FIRE figures, computed by the server. */
data class UserHomeDataDomain(
    val netWorth: Long = 0L,
    val fireNumber: EmiSplitDomain = EmiSplitDomain(),
    val totalExpenses: EmiSplitDomain = EmiSplitDomain(),
    val firePercentage: EmiSplitDomain = EmiSplitDomain()
)

/** A figure with and without loan EMIs counted as expenses. */
data class EmiSplitDomain(
    val includeEmi: Double = 0.0,
    val excludeEmi: Double = 0.0
)
