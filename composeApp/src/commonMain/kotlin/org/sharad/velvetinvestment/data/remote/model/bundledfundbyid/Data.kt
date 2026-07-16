package org.sharad.velvetinvestment.data.remote.model.bundledfundbyid

import kotlinx.serialization.Serializable

@Serializable
data class Data(
    val bundle_description: String,
    val bundle_name: String,
    val categories: List<Category>,
    val commodity_percentage: Double,
    val debt_percentage: Double,
    val equity_percentage: Double,
    val hybrid_percentage: Double,
    val meta_data: MetaData
)