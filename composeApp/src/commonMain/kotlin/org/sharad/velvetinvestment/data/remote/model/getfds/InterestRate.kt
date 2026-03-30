package org.sharad.velvetinvestment.data.remote.model.getfds

import kotlinx.serialization.Serializable

@Serializable
data class InterestRate(
    val annualized_yield: String,
    val customer_type: String,
    val interest_rate: String,
    val is_default_selection: Boolean,
    val payout_frequency: String,
    val tenure_days: Int,
    val tenure_label: String
)