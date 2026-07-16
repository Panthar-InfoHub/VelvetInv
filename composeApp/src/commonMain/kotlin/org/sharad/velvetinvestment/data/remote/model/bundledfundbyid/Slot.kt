package org.sharad.velvetinvestment.data.remote.model.bundledfundbyid

import kotlinx.serialization.Serializable

@Serializable
data class Slot(
    val allocation_percentage: Double,
    val bundle_category_id: String,
    val default_rank: Int,
    val id: String
)