package org.sharad.velvetinvestment.data.remote.model.fddetails

import kotlinx.serialization.Serializable

@Serializable
data class Data(
    val faqs: List<Faq>,
    val id: String,
    val interest_rates: List<InterestRate>,
    val is_vkyc_required: Boolean,
    val issuer: Issuer,
    val issuer_id: String,
    val lock_in_period_days: Int,
    val max_deposit: String,
    val max_tenure_days: Int,
    val min_amount_for_vkyc: String,
    val min_deposit: String,
    val min_tenure_days: Int,
    val premature_penalty_percent: Int,
    val tags: List<Tag>,
    val type: String,
    val usps: List<Usp>,
    val withdrawal_message: String
)