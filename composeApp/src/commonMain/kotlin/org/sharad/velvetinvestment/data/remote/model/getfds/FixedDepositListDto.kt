package org.sharad.velvetinvestment.data.remote.model.getfds

import kotlinx.serialization.Serializable

@Serializable
data class FixedDepositListDto(
    val `data`: Data,
    val message: String,
    val success: Boolean
)