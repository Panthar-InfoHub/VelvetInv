package org.sharad.velvetinvestment.data.remote.model.tradingaccount.prefilled

import kotlinx.serialization.Serializable

@Serializable
data class TradingAccountPrefilledResponseDto(
    val success: Boolean,
    val message: String,
    val data: PrefilledDataDto
)