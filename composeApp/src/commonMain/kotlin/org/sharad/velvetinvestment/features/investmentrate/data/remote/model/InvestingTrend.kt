package org.sharad.velvetinvestment.features.investmentrate.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class InvestingTrend(
    val investments: Long,
    val month: String,
    val savings: Long
)