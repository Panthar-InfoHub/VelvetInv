package org.sharad.velvetinvestment.data.remote.model.firereport

import kotlinx.serialization.Serializable

@Serializable
data class Goal(
    val category: String,
    val current_monthly_exp: Int?,
    val goal_type_id: Int,
    val id: String,
    val life_expectancy: Int?,
    val name: String,
    val required_monthly_sip: Int,
    val target_amount: Int,
    val target_year: Int
)