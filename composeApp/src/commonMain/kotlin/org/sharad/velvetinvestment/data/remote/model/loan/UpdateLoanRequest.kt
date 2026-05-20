package org.sharad.velvetinvestment.data.remote.model.loan

import kotlinx.serialization.Serializable

@Serializable
data class UpdateLoanRequest(
    val loan_type: String? = null,
    val loan_name: String? = null,
    val outstanding_amount: Long? = null,
    val monthly_emi: Long? = null,
    val tenure_months: Int? = null
)
