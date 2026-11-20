package org.sharad.velvetinvestment.features.goals.domain.models

/**
 * `POST /user-goal/calculate`: what a goal would cost and require, for inputs the user is still
 * editing. The create screen shows this so the figures it previews are the same ones the goal
 * will be stored with.
 */
data class GoalCalculationDomain(
    val goalTypeId: Int,
    val yearsRemaining: Int,
    val currentCost: Double?,
    val targetAmount: Double?,
    val currentSavings: Double,
    /** The rates the server actually applied — the type's defaults unless overridden. */
    val inflationRateUsed: Double?,
    val expectedReturnRateUsed: Double?,
    val futureTargetAmount: Double,
    val fvCurrentSavings: Double,
    val netRequiredCorpus: Double,
    val requiredMonthlySip: Double,
    val requiredLumpsumToday: Double,
    /** Set only for a retirement goal, whose projection the shared fields above cannot describe. */
    val retirement: RetirementProjection? = null
)

/** `POST /user-goal/calculate` for retirement (type 7). */
data class RetirementProjection(
    val currentAge: Int,
    val retirementAge: Int,
    val planUntilAge: Int,
    val yearsToRetirement: Int,
    /** Today's monthly lifestyle cost, inflated to the retirement date. */
    val monthlyCostAtRetirement: Double,
    /** The corpus to retire with, buffer included. */
    val targetCorpus: Double,
    /** What existing savings grow to by retirement. */
    val fvExistingSavings: Double,
    val remainingCorpusToBuild: Double,
    val totalMonthlySip: Double,
    val additionalLumpsumToday: Double
)
