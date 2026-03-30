package org.sharad.velvetinvestment.data.remote.model.getfds

import kotlinx.serialization.Serializable

@Serializable
data class FdProduct(
    val id: String,
    val interest_rates: List<InterestRate>,
    val issuer: Issuer,
    val issuer_id: String,
    val lock_in_period_days: Int,
    val max_deposit: String,
    val max_tenure_days: Int,
    val min_deposit: String,
    val min_tenure_days: Int,
    val premature_penalty_percent: Int,
    val tags: List<Tag>,
    val type: String,
    val withdrawal_message: String
)