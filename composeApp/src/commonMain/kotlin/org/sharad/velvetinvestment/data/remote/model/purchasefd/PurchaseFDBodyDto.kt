package org.sharad.velvetinvestment.data.remote.model.purchasefd

import kotlinx.serialization.Serializable

@Serializable
data class PurchaseFDBodyDto(
    val investment_amount: Long,
    val investment_period: Int,
    val payout_frequency: String,
    val product_id: String,
    val tenure: Int
)