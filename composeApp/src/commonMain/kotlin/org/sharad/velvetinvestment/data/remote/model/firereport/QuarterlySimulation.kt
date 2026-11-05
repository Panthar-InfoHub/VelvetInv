package org.sharad.velvetinvestment.data.remote.model.firereport

import kotlinx.serialization.Serializable

/** `net_worth` and `fire_number` are in lakhs here, unlike the rest of the report. */
@Serializable
data class QuarterlySimulation(
    val quarter: String = "",
    val net_worth: Double = 0.0,
    val fire_number: Double = 0.0,
    val fire_percentage: Double = 0.0
)
