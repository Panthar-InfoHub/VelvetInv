package org.sharad.velvetinvestment.data.remote.model.firereport

import kotlinx.serialization.Serializable

@Serializable
data class AssetsBreakdown(
    val mutual_funds: Double = 0.0,
    val stocks: Double = 0.0,
    val fd: Double = 0.0,
    val gold: Double = 0.0,
    val cash_saving: Double = 0.0,
    val total_liquid: Double = 0.0,
    val total_illiquid: Double = 0.0,
    val total: Double = 0.0
)
