package org.sharad.velvetinvestment.data.remote.model.onboarding

import kotlinx.serialization.Serializable

@Serializable
data class Insurance(
    val health_insurance: Long,
    val life_insurance: Long
)