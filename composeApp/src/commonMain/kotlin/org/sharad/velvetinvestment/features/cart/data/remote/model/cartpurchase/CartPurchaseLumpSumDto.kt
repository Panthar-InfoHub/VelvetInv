package org.sharad.velvetinvestment.features.cart.data.remote.model.cartpurchase

import kotlinx.serialization.Serializable

@Serializable
data class CartPurchaseLumpSumDto(
    val `data`: String,
    val message: String,
    val success: Boolean
)