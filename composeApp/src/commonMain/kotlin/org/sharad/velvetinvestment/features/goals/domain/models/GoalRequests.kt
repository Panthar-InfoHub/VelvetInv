package org.sharad.velvetinvestment.features.goals.domain.models

import org.sharad.velvetinvestment.shared.domain.GoalType

/**
 * The inputs a goal is created from. One shape covers all six types: which fields the server
 * requires is a function of [goalType], and [org.sharad.velvetinvestment.features.goals.data.mapper.toCreateBody]
 * sends only the ones that type uses.
 */
data class CreateGoalRequest(
    val goalType: GoalType,
    val goalName: String? = null,
    val childName: String? = null,
    val childAge: Int? = null,
    val assetSubtype: String? = null,
    /** Not sent for retirement, which is sized from [retirementAge] and [planUntilAge] instead. */
    val yearsRemaining: Int,
    /** Present-day cost, for types 1, 2, 3, 4 and 6. */
    val currentCost: Double? = null,
    /** The corpus asked for, for type 5. */
    val targetAmount: Double? = null,
    val currentSavings: Double = 0.0,
    /** Fractions (0.06 == 6%). Null leaves the type's configured default in place. */
    val inflationRate: Double? = null,
    val expectedReturnRate: Double? = null,
    /** Retirement (type 7) only. */
    val retirementAge: Int? = null,
    /** Retirement only: the life expectancy the plan has to last until. */
    val planUntilAge: Int? = null,
    /** Retirement only: today's monthly cost of the lifestyle to retire into. */
    val monthlyLifestyleCost: Long? = null
)

/**
 * The inputs a projection is asked for. The same fields as [CreateGoalRequest] minus everything
 * that only labels the goal — the server sizes a goal from its money and its horizon alone.
 */
data class GoalCalculationRequest(
    val goalTypeId: Int,
    val yearsRemaining: Int,
    val currentCost: Double? = null,
    val targetAmount: Double? = null,
    val currentSavings: Double = 0.0,
    val inflationRate: Double? = null,
    val expectedReturnRate: Double? = null,
    /** Retirement only — sent in retirement's own calculate body. */
    val retirementAge: Int? = null,
    val planUntilAge: Int? = null,
    val monthlyLifestyleCost: Long? = null
) {
    val isRetirement: Boolean get() = retirementAge != null
}

fun CreateGoalRequest.toCalculationRequest(): GoalCalculationRequest? =
    if (goalType.isRetirement) GoalCalculationRequest(
        goalTypeId = goalType.id,
        yearsRemaining = 0,
        retirementAge = retirementAge ?: return null,
        planUntilAge = planUntilAge ?: return null,
        monthlyLifestyleCost = monthlyLifestyleCost ?: return null
    ) else GoalCalculationRequest(
    goalTypeId = goalType.id,
    yearsRemaining = yearsRemaining,
    currentCost = currentCost,
    targetAmount = targetAmount,
    currentSavings = currentSavings,
    inflationRate = inflationRate,
    expectedReturnRate = expectedReturnRate
)
