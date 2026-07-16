package org.sharad.velvetinvestment.data.remote.model.bundledfundbyid

import kotlinx.serialization.Serializable

@Serializable
data class BundleByIdDto(
    val `data`: Data,
    val message: String,
    val success: Boolean
)