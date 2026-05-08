package org.sharad.velvetinvestment.data.remote.model.investmentratedto

import kotlinx.serialization.Serializable

@Serializable
data class InvestingTrend(
    val investments: Long,
    val month: String,
    val savings: Long
)