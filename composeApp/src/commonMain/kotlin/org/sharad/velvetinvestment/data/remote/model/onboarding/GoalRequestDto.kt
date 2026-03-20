package org.sharad.velvetinvestment.data.remote.model.onboarding

import kotlinx.serialization.Serializable

// -----------------------------
// Base Goal Interface
// -----------------------------

interface GoalRequestDto {
    val goal_type_id: Int
    val inflation_rate: Int
    val return_rate: Int
    val current_saved_amount: Long
}

// -----------------------------
// Goal Type 1 - Child Education
// -----------------------------
@Serializable
data class ChildEducationGoalRequest(
    override val goal_type_id: Int = 1,
    override val inflation_rate: Int,
    override val return_rate: Int,
    override val current_saved_amount: Long,

    val child_name: String,
    val child_age: Int,
    val years_left: Int,
    val current_goal_cost: Long
) : GoalRequestDto

@Serializable
data class ChildMarriageGoalRequest(
    override val goal_type_id: Int = 2,
    override val inflation_rate: Int,
    override val return_rate: Int,
    override val current_saved_amount: Long,

    val child_name: String,
    val child_age: Int,
    val years_left: Int,
    val current_goal_cost: Long
) : GoalRequestDto

// -----------------------------
// Goal Type 3 - Retirement
// -----------------------------
@Serializable
data class RetirementGoalRequest(
    override val goal_type_id: Int = 3,
    override val inflation_rate: Int,
    override val return_rate: Int,
    override val current_saved_amount: Long,

    val current_age: Int,
    val retirement_age: Int,
    val life_expectancy: Int,
    val current_monthly_expense: Long,
    val post_retirement_return: Int
) : GoalRequestDto


// -----------------------------
// Goal Type 4 - Custom Goal
// -----------------------------
@Serializable
data class CustomGoalRequest(
    override val goal_type_id: Int = 4,
    override val inflation_rate: Int,
    override val return_rate: Int,
    override val current_saved_amount: Long,

    val goal_name: String,
    val goal_item_id: Int,
    val goal_item_name: String,
    val years_left: Int,
    val current_goal_cost: Long
) : GoalRequestDto

