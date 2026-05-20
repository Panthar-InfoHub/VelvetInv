package org.sharad.velvetinvestment.domain.models.loan

import kotlinx.serialization.Serializable

@Serializable
data class LoanDomain(
    val id: String,
    val user_id: String,
    val loan_type: String,
    val loan_name: String,
    val outstanding_amount: String,
    val monthly_emi: String,
    val tenure_months: Int,
    val createdAt: String,
    val updatedAt: String
)
