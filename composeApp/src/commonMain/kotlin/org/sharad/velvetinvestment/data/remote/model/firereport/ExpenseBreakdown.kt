package org.sharad.velvetinvestment.data.remote.model.firereport

import kotlinx.serialization.Serializable

/** Monthly figures, plus the yearly total. */
@Serializable
data class ExpenseBreakdown(
    val house: Double = 0.0,
    val food: Double = 0.0,
    val transportation: Double = 0.0,
    val others: Double = 0.0,
    val total_monthly: Double = 0.0,
    val total_annual: Double = 0.0
)
