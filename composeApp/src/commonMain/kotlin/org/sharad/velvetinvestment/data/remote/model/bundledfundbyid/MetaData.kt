package org.sharad.velvetinvestment.data.remote.model.bundledfundbyid

import kotlinx.serialization.Serializable

@Serializable
data class MetaData(
    val investment_growth: String,
    val investment_time: String,
    val risk_level: String
)