package org.sharad.velvetinvestment.data.remote.model.firereport

import kotlinx.serialization.Serializable

@Serializable
data class InsuranceSummary(
    val term_life_have: Double = 0.0,
    val term_life_recommended: Double = 0.0,
    val term_life_gap: Double = 0.0,
    val health_have: Double = 0.0,
    val health_recommended: Double = 0.0,
    val health_gap: Double = 0.0
)
