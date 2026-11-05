package org.sharad.velvetinvestment.shared.data.remote.model.userdata

import kotlinx.serialization.Serializable

/**
 * `GET /user/`.
 *
 * Almost everything is optional: the profile fills in as the user moves through onboarding, and
 * the finance, assets and insurance blocks come back null until those sections are completed.
 * The hashes and the refresh token are echoed by the server but are not modelled here — the app
 * has no use for them, and unknown keys are ignored by the shared Json config.
 */
@Serializable
data class Data(
    val id: String,
    val full_name: String? = null,
    val email: String? = null,
    val phone_no: String? = null,
    val city: String? = null,
    val dob: String? = null,
    val fcm_token: String? = null,
    val meta_data: MetaData? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null,

    /** Gateway identifiers created once KYC clears; null before that. */
    val investor_profile: String? = null,
    val investment_account: String? = null,
    val investment_account_old_id: Int? = null,

    val mpin_is_setup: Boolean = false,
    val mpin_enabled: Boolean = false,

    val user_finance: UserFinance? = null,
    val user_assets: UserAssets? = null,
    val user_insurance: UserInsurance? = null,
    val user_loan: List<UserLoan> = emptyList(),
    val user_goals: List<UserGoal> = emptyList(),
    val user_bank_details: List<UserBankDetail> = emptyList(),

    val onboarding: OnboardingStatusDto? = null,
    /** True when the user chose to skip onboarding and go straight into the app. */
    val is_skip: Boolean = false,
    /**
     * True once the KYC form has been filled in, whether or not it has been verified yet. This
     * top-level flag is the one the app reads, not the copy inside `onboarding`.
     */
    val is_kyc_completed: Boolean = false,
    /** True while a filled-in KYC is with the server for verification. Read from the top level. */
    val kyc_under_review: Boolean = false,
    /** The server's call on whether the user belongs in the main app. Not used yet. */
    val can_navigate_home: Boolean = false,
    /** KYC onboarding finished. Mirrors `onboarding.is_completed`. */
    val is_completed: Boolean = false,
    /** The FIRE questionnaire (finance, assets, loans, insurance) is finished. */
    val is_fire_completed: Boolean = false,
    val dashboard: DashboardDto? = null,
    /** Net worth and FIRE figures, computed by the server. Null until the FIRE answers exist. */
    val user_home_data: UserHomeDataDto? = null
)

/**
 * Per-step onboarding progress, as both `GET /user/` and `POST /auth/validate-otp` report it.
 * Statuses seen: `PENDING` / `IN_PROGRESS` / `VERIFIED` / `FAILED` / `SKIPPED`.
 */
@Serializable
data class OnboardingStatusDto(
    val current_stage: String? = null,
    val basic_details_status: String? = null,
    val readiness_status: String? = null,
    val kyc_status: String? = null,
    val reverse_penny_status: String? = null,
    val penny_drop_status: String? = null,
    val email_status: String? = null,
    val profile_status: String? = null,
    val nominee_status: String? = null,

    /** FIRE questionnaire steps, same status values as the KYC steps. */
    val finance_status: String? = null,
    val assets_status: String? = null,
    val loan_status: String? = null,
    val insurance_status: String? = null,
    val goals_status: String? = null,

    val has_navigated_home: Boolean = false,
    val is_completed: Boolean = false,
    val completed_at: String? = null,
    val is_fire_completed: Boolean = false,
    val fire_completed_at: String? = null,
    val is_skip: Boolean = false,

    /** The KYC step statuses again, keyed by step (`basic_details`, `kyc`, `penny_drop`, …). */
    val stages: Map<String, String> = emptyMap(),
    /** The FIRE step statuses keyed by step (`finance`, `assets`, `loan`, `insurance`, `goals`). */
    val fire_stages: Map<String, String> = emptyMap(),
    val is_kyc_completed: Boolean = false,
    val kyc_under_review: Boolean = false,
    /** Whether the user goes to the main app rather than onboarding. Login decides on this alone. */
    val can_navigate_home: Boolean = false,
    /** Why the KYC was rejected, when `kyc_status` is `FAILED`. */
    val reason: String? = null
)

/** The headline figures the home screen's portfolio card shows. */
@Serializable
data class DashboardDto(
    val portfolio_value: Double = 0.0,
    val mutual_funds: Double = 0.0,
    val fixed_deposits: Double = 0.0,
    val total_returns: Double = 0.0,
    val return_percent: Double = 0.0,
    /** Null until there is a previous month to compare against. */
    val month_change_percent: Double? = null
)
