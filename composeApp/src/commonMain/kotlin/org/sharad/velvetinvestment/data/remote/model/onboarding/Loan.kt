package org.sharad.velvetinvestment.data.remote.model.onboarding

import kotlinx.serialization.Serializable

@Serializable
data class Loan(
    val loan_name: String,
    val loan_type: String,
    val monthly_emi: Long,
    val outstanding_amount: Long,
    val tenure_months: Int
)