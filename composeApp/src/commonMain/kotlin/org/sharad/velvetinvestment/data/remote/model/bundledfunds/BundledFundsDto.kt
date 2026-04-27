package org.sharad.velvetinvestment.data.remote.model.bundledfunds

import kotlinx.serialization.Serializable

@Serializable
data class BundledFundsDto(
    val `data`: Data,
    val message: String,
    val success: Boolean
)