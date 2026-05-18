package org.sharad.velvetinvestment.data.remote.model.fundredeem.response

import kotlinx.serialization.Serializable

@Serializable
data class FundRedeemDto(
    val `data`: Data,
    val message: String,
    val success: Boolean
)