package org.sharad.velvetinvestment.shared.data.remote.model.userdata

import kotlinx.serialization.Serializable

/** One row of `user_loan` in `GET /user/`. `loan_type` is e.g. `HOME_LOAN`, `CAR_LOAN`. */
@Serializable
data class UserLoan(
    val id: String,
    val user_id: String? = null,
    val loan_type: String? = null,
    val loan_name: String? = null,
    val outstanding_amount: String? = null,
    val monthly_emi: String? = null,
    val tenure_months: Int? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null
)
