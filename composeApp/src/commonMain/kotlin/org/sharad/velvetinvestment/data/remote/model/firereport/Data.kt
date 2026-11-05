package org.sharad.velvetinvestment.data.remote.model.firereport

import kotlinx.serialization.Serializable

/** One scenario of `GET /fire-report` (`actual` or `projected`). */
@Serializable
data class Data(
    val user_profile: UserProfile = UserProfile(),
    val computed_metrics: ComputedMetrics = ComputedMetrics(),
    val goals: List<Goal> = emptyList(),
    val projection: List<Projection> = emptyList(),
    val assets_breakdown: AssetsBreakdown? = null,
    val liabilities: List<Liability> = emptyList(),
    val expense_breakdown: ExpenseBreakdown? = null,
    val insurance_summary: InsuranceSummary? = null,
    val quarterly_simulation: List<QuarterlySimulation> = emptyList(),
    val yearly_goal_requirements: List<YearlyGoalRequirement> = emptyList()
)
