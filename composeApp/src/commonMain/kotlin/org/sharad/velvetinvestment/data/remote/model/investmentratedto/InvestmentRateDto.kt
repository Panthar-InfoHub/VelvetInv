package org.sharad.velvetinvestment.data.remote.model.investmentratedto

import kotlinx.serialization.Serializable

@Serializable
data class InvestmentRateDto(
    val code: Int,
    val `data`: Data,
    val message: String
)