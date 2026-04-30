package org.sharad.velvetinvestment.data.remote.model.bundledfundbyid

import kotlinx.serialization.Serializable

@Serializable
data class Data(
    val bundle_name: String,
    val bundle_products: List<BundleProduct>,
    val id: String
)