package org.sharad.velvetinvestment.data.remote.model.firereport

import kotlinx.serialization.Serializable

@Serializable
data class Projection(
    val fire_number: FireNumber,
    val fire_percentage: FirePercentage,
    val goal_commitment_annual: Long,
    val goal_hits: List<GoalHit>,
    val goals_payout: Long,
    val income: Long,
    val portfolio_value: PortfolioValue,
    val savings: Savings,
    val total_expenses: TotalExpenses,
    val year: Int
)