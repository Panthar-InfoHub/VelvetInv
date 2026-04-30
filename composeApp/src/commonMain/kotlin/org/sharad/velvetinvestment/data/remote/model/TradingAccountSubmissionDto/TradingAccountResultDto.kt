package org.sharad.velvetinvestment.data.remote.model.TradingAccountSubmissionDto

import kotlinx.serialization.Serializable

@Serializable
data class TradingAccountResultDto(
    val `data`: Data,
    val message: String,
    val success: Boolean
)