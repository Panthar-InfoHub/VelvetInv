package org.sharad.velvetinvestment.data.remote.model.useedata

import kotlinx.serialization.Serializable

@Serializable
data class MetaData(
    val onboarding_stage: Int,
    val is_onboarding_completed: Boolean
)