package org.sharad.velvetinvestment.data.remote.model.allbundles

import kotlinx.serialization.Serializable

@Serializable
data class Data(
    val accumulated_min_amount: Int,
    val bundle_name: String,
    val bundle_products: List<BundleProduct>,
    val id: String
)