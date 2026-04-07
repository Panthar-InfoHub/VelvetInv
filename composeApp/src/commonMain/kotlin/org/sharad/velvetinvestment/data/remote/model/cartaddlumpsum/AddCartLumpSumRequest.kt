package org.sharad.velvetinvestment.data.remote.model.cartaddlumpsum

import kotlinx.serialization.Serializable

@Serializable
data class AddCartLumpSumRequest(
    val amount: Long,
    val mfProductId: String
)