package org.sharad.velvetinvestment.shared.data.remote.model.userdata

import kotlinx.serialization.Serializable

@Serializable
data class MetaData(
    val is_onboarding_completed: Boolean = false,
    val onboarding_stage: Int? = null
)
