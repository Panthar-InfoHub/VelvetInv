package org.sharad.velvetinvestment.features.mutualfund.data.remote.model.fundredeem.response

import kotlinx.serialization.Serializable

@Serializable
data class FundRedeemDto(
    val `data`: Data,
    val message: String,
    val success: Boolean
)