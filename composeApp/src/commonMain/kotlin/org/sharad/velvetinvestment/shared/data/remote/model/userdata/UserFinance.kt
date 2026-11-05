package org.sharad.velvetinvestment.shared.data.remote.model.userdata

import kotlinx.serialization.Serializable

/**
 * `user_finance` in `GET /user/`: the FIRE questionnaire's income step. Amounts are Postgres
 * numerics sent as strings; null until the finance step is filled in.
 */
@Serializable
data class UserFinance(
    val id: String? = null,
    val user_id: String? = null,
    val annual_income: String? = null,
    val expense_house: String? = null,
    val expense_food: String? = null,
    val expense_transportation: String? = null,
    val expense_others: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null
)
