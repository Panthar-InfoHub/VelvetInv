package org.sharad.velvetinvestment.data.remote.model.firereport

import kotlinx.serialization.Serializable

@Serializable
data class Projection(
    val year: Int = 0,
    val income: Double = 0.0,
    val goal_commitment_annual: Double = 0.0,
    val goals_payout: Double = 0.0,
    val goal_hits: List<GoalHit> = emptyList(),
    val total_expenses: TotalExpenses = TotalExpenses(),
    val savings: Savings = Savings(),
    val portfolio_value: PortfolioValue = PortfolioValue(),
    val fire_number: FireNumber = FireNumber(),
    val fire_percentage: FirePercentage = FirePercentage()
)
