package org.sharad.velvetinvestment.data.remote.model.mutualfundcombined

import kotlinx.serialization.Serializable

@Serializable
data class Item(
    val bundle_name: String,
    val bundle_products: List<BundleProduct>,
    val id: String,
    val accumulated_min_amount: Double
)