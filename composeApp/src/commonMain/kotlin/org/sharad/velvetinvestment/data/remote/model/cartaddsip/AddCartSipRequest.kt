package org.sharad.velvetinvestment.data.remote.model.cartaddsip

import kotlinx.serialization.Serializable

@Serializable
data class AddCartSipRequest(
    val amount: Long,
    val mf_product_id: String,
    val sip_start_date: String,
    val sip_end_date: String,
    val sip_frequency: String,
    val sip_day: Int,
    val sip_amount: Long
)