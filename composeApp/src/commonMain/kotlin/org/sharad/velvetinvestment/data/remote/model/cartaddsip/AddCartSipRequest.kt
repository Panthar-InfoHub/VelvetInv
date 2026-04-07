package org.sharad.velvetinvestment.data.remote.model.cartaddsip

import kotlinx.serialization.Serializable

@Serializable
data class AddCartSipRequest(
    val amount: Long,
    val mfProductId: String,
    val sipStartDate: String,
    val sipEndDate: String,
    val sipFrequency: String,
    val sipDay: Int,
    val sipAmount: Long
)