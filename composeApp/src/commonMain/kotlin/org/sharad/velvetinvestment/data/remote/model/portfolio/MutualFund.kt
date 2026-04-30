package org.sharad.velvetinvestment.data.remote.model.portfolio

import kotlinx.serialization.Serializable

@Serializable
data class MutualFund(
    val amount: Double,
    val avg_nav: Double,
    val balance_units: Double,
    val category: String,
    val current_nav: Double,
    val folio: String,
    val id: Int,
    val is_sip: Boolean,
    val `return`: Int,
    val return_percentage: String,
    val start_date: String,
    val title: String,
    val xirr: String
)