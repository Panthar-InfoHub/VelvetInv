package org.sharad.velvetinvestment.data.remote.model.portfolio

import kotlinx.serialization.Serializable

@Serializable
data class FdTransaction(
    val id: String,
    val title: String,
    val category: String,
    val amount: Double,
    val start_date: String,
    val `return`: Double,
    val roi: Double,
    val tenure_days: Int,
    val status: String,
    val maturity_amount: Double,
    val issuer_logo: String
)