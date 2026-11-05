package org.sharad.velvetinvestment.features.login.data.models.auth.verifyotp

import kotlinx.serialization.Serializable
import org.sharad.velvetinvestment.shared.data.remote.model.userdata.OnboardingStatusDto

/** `POST /auth/validate-otp` → `data`. */
@Serializable
data class Data(
    val user: User,
    /** The same flat block `GET /user/` returns under `onboarding`. */
    val onboarding: OnboardingStatusDto,
    /** True when the user chose to skip onboarding at PAN verification. */
    val is_skip: Boolean = false,
    /** Top-level copies of the `onboarding` flags; login reads the ones inside `onboarding`. */
    val is_kyc_completed: Boolean = false,
    val kyc_under_review: Boolean = false,
    val can_navigate_home: Boolean = false,
    val is_completed: Boolean = false,
    /** FIRE questionnaire finished. An app feature, never a login gate. */
    val is_fire_completed: Boolean = false,
    val token: String,
    val refresh_token: String
)
