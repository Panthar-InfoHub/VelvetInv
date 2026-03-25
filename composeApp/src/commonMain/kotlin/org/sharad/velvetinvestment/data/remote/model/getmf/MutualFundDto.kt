package org.sharad.velvetinvestment.data.remote.model.getmf

import kotlinx.serialization.Serializable

@Serializable
data class MutualFundDto(
    val `data`: Data,
    val message: String,
    val success: Boolean
)