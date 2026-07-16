package org.sharad.velvetinvestment.data.remote.model.bundlecart

import kotlinx.serialization.Serializable

@Serializable
data class AddBundleLumpsumRequest(
    val type: String = "LUMPSUM",
    val bundle_id: String,
    val amount: Long,
    val selections: List<BundleSelection>
)

@Serializable
data class AddBundleSipRequest(
    val type: String = "SIP",
    val bundle_id: String,
    val amount: Long,
    val sip_st_date: String,
    val sip_en_date: String,
    val sip_freq: String,
    val sip_day: Int,
    val sip_amt: Long,
    val selections: List<BundleSelection>
)

@Serializable
data class BundleSelection(
    val mf_product_id: String,
    val allocation_percentage: Int
)
