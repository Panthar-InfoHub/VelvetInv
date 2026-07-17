package org.sharad.velvetinvestment.data.remote.model.mutualfundcombined

import kotlinx.serialization.Serializable

@Serializable
data class Item(
    val id: String,
    val bundle_name: String,
    val bundle_description: String,
    val equity_percentage: Int,
    val commodity_percentage: Int,
    val debt_percentage: Int,
    val hybrid_percentage: Int,
    val meta_data: BundleMetaData,
    val img_url: String? = null
)
