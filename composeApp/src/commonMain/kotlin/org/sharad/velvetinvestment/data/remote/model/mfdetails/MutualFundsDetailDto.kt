package org.sharad.velvetinvestment.data.remote.model.mfdetails

import kotlinx.serialization.Serializable

@Serializable
data class MutualFundsDetailDto(
    val `data`: Data,
    val message: String,
    val success: Boolean
)