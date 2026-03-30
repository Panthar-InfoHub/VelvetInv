package org.sharad.velvetinvestment.data.remote.model.fddetails

import kotlinx.serialization.Serializable

@Serializable
data class InterestRate(
    val annualized_yield: String,
    val customer_type: String,
    val id: String,
    val interest_rate: String,
    val is_default_selection: Boolean,
    val is_tax_saver: Boolean,
    val last_updated_at: String,
    val payout_frequency: String,
    val tenure_days: Int,
    val tenure_label: String
)