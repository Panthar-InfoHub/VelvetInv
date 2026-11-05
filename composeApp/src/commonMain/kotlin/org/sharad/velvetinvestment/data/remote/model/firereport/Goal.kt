package org.sharad.velvetinvestment.data.remote.model.firereport

import kotlinx.serialization.Serializable

@Serializable
data class Goal(
    val id: String = "",
    val name: String = "",
    val category: String = "",
    val goal_type_id: Int = 0,
    val target_year: Int = 0,
    val target_amount: Double = 0.0,
    val life_expectancy: Int? = null,
    val current_monthly_exp: Double? = null,
    val required_monthly_sip: Double = 0.0,
    val future_value: Double = 0.0
)
