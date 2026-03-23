package org.sharad.velvetinvestment.data.remote.model.useedata

import kotlinx.serialization.Serializable

@Serializable
data class UserGoal(
    val child_age: String?,
    val child_name:  String?,
    val createdAt: String,
    val current_age: Int?,
    val current_goal_cost: String,
    val current_monthly_expense: Double?,
    val current_saved_amount: String,
    val goal_id: Int,
    val goal_item_id: Int,
    val goal_item_name: String,
    val goal_name: String,
    val goal_type_id: Int,
    val id: String,
    val inflation_rate: Int,
    val life_expectancy: Int?,
    val post_retirement_return: Double?,
    val retirement_age: Int?,
    val return_rate: Int,
    val updatedAt: String,
    val user_id: String,
    val years_left: Int
)