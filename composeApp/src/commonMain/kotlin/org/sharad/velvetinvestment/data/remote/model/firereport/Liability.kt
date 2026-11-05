package org.sharad.velvetinvestment.data.remote.model.firereport

import kotlinx.serialization.Serializable

@Serializable
data class Liability(
    val loan_type: String = "",
    val outstanding: Double = 0.0,
    val monthly_emi: Double = 0.0,
    val tenure_months: Int = 0
)
