package org.sharad.velvetinvestment.data.remote.model.onboarding.response

import kotlinx.serialization.Serializable

@Serializable
data class Data(
    val current_onboarding_step: Int,
    val is_onboarding_completed: Boolean
)