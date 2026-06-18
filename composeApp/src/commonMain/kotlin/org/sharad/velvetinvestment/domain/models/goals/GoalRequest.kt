package org.sharad.velvetinvestment.domain.models.goals

sealed interface GoalRequest {

    val goalTypeId: Int
    val inflationRate: Double
    val returnRate: Double
    val currentSavedAmount: Long
    val yearsToGoal:Int
    val title: String

    // Child related goals
    data class ChildEducation(
        override val goalTypeId: Int = 1,
        val childName: String,
        val childAge: Int,
        override val yearsToGoal: Int,
        val currentGoalCost: Long,
        override val inflationRate: Double,
        override val returnRate: Double,
        override val currentSavedAmount: Long, override val title: String
    ) : GoalRequest

    data class ChildMarriage(
        override val goalTypeId: Int = 2,
        val childName: String,
        val childAge: Int,
        override val yearsToGoal: Int,
        val currentGoalCost: Long,
        override val inflationRate: Double,
        override val returnRate: Double,
        override val currentSavedAmount: Long, override val title: String
    ) : GoalRequest


    // Retirement goal
    data class Retirement(
        override val goalTypeId: Int = 3,
        val currentAge: Int,
        val retirementAge: Int,
        val lifeExpectancy: Int,
        val currentMonthlyExpense: Long,
        val postRetirementReturn: Double,
        override val inflationRate: Double,
        override val returnRate: Double,
        override val currentSavedAmount: Long,
        override val yearsToGoal: Int, override val title: String
    ) : GoalRequest


    // Item goal
    data class WealthBuildingGoal(
        override val goalTypeId: Int = 4,
        val goalName: String,
        val goalItemId: Int,
        val goalItemName: String,
        override val yearsToGoal: Int,
        val currentGoalCost: Long,
        override val inflationRate: Double,
        override val returnRate: Double,
        override val currentSavedAmount: Long, override val title: String
    ) : GoalRequest
}