package org.sharad.velvetinvestment.data.remote.model.portfolio

import kotlinx.serialization.Serializable

@Serializable
data class InvestmentData(
    val current_value: Double,
    val invested_amount: Double,
    val return_percent: Double?,
    val total_returns: Double
)