package org.sharad.velvetinvestment.data.remote.model.mutualfundcombined

import kotlinx.serialization.Serializable

@Serializable
data class BundleMetaData(
    val risk_level: String,
    val investment_time: String,
    val investment_growth: String
)
