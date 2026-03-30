package org.sharad.velvetinvestment.data.remote.model.useedata

import kotlinx.serialization.Serializable

@Serializable
data class MetaData(
    val is_onboarding_completed: Boolean,
    val onboarding_stage: Int
)