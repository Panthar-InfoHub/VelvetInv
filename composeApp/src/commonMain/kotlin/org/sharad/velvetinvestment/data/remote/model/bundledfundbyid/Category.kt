package org.sharad.velvetinvestment.data.remote.model.bundledfundbyid

import kotlinx.serialization.Serializable

@Serializable
data class Category(
    val bundle_id: String,
    val category_name: String,
    val display_name: String,
    val funds: List<Fund>,
    val id: String,
    val slots: List<Slot>,
    val total_percentage: Double
)