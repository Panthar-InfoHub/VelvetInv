package org.sharad.velvetinvestment.data.remote.model.cartpurchase

import kotlinx.serialization.Serializable

@Serializable
data class CartPurchaseDto(
    val `data`: String,
    val message: String,
    val success: Boolean
)