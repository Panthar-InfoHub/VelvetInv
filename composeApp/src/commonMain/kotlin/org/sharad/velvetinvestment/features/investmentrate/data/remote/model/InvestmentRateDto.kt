package org.sharad.velvetinvestment.features.investmentrate.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class InvestmentRateDto(
    val code: Int,
    val `data`: Data,
    val message: String
)