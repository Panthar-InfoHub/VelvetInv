package org.sharad.velvetinvestment.features.mutualfund.data.remote.model.mfdetails

import kotlinx.serialization.Serializable

@Serializable
data class MutualFundsDetailDto(
    val `data`: Data = Data(),
    val message: String = "",
    val success: Boolean = false
)
