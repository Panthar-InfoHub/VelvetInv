package org.sharad.velvetinvestment.data.remote.model.auth.verifyotp

import kotlinx.serialization.Serializable

@Serializable
data class Metadata(
    val is_onboarding_complete: Boolean,
    val onboarding_stage: Int
)