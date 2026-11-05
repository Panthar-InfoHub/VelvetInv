package org.sharad.velvetinvestment.features.fireonboarding.data.remote.model

import kotlinx.serialization.Serializable
import org.sharad.velvetinvestment.shared.data.remote.model.userdata.OnboardingStatusDto

/**
 * What every FIRE step answers with. `data` (the saved record, or null when skipped) differs per
 * step and nothing reads it, so it is not modelled; `onboarding` carries the updated `fire_stages`.
 */
@Serializable
data class FireStepResponseDto(
    val success: Boolean = false,
    val message: String? = null,
    val onboarding: OnboardingStatusDto? = null
)
