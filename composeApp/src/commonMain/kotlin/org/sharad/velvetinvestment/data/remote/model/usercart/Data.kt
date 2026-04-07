package org.sharad.velvetinvestment.data.remote.model.usercart

import kotlinx.serialization.Serializable

@Serializable
data class Data(
    val lump_sum_items: List<LumpSumItem>,
    val sip_items: List<SipItem>
)