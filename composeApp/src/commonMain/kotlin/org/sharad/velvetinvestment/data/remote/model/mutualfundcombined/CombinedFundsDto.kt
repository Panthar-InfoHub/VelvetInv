package org.sharad.velvetinvestment.data.remote.model.mutualfundcombined

import kotlinx.serialization.Serializable

@Serializable
data class CombinedFundsDto(
    val `data`: Data,
    val message: String,
    val success: Boolean
)