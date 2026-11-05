package org.sharad.velvetinvestment.features.cart.data.remote.model.cartpurchase

import kotlinx.serialization.Serializable

@Serializable
data class CartPurchaseSIPDto(
    val `data`: Data,
    val message: String,
    val success: Boolean
)