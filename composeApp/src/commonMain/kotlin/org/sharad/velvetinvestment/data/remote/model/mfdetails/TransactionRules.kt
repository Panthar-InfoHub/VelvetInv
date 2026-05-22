package org.sharad.velvetinvestment.data.remote.model.mfdetails

import kotlinx.serialization.Serializable

@Serializable
data class TransactionRules(
    val sip_allowed_dates: List<Int>? = emptyList(),
    val sip_frequencies: List<String>? = emptyList(),
    val min_investment_amount: Double= 0.0
)