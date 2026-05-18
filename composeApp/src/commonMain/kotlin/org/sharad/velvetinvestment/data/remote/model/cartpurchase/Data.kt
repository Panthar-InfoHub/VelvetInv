package org.sharad.velvetinvestment.data.remote.model.cartpurchase

import kotlinx.serialization.Serializable

@Serializable
data class Data(
    val order_id: String,
    val status: String,
    val xsip_short_url: String
)