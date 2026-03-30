package org.sharad.velvetinvestment.data.remote.model.purchasefd

import kotlinx.serialization.Serializable

@Serializable
data class PurchaseFDDto(
    val `data`: String,
    val message: String,
    val success: Boolean
)