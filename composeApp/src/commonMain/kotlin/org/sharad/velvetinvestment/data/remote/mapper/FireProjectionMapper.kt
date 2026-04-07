package org.sharad.velvetinvestment.data.remote.mapper

import org.sharad.velvetinvestment.data.remote.model.firereport.FireReportDto
import org.sharad.velvetinvestment.domain.models.fire.EmiValueDomain
import org.sharad.velvetinvestment.domain.models.fire.FirePercentagePointDomain
import org.sharad.velvetinvestment.domain.models.fire.FireProjectionRowDomain
import org.sharad.velvetinvestment.domain.models.fire.FireReportDomain
import org.sharad.velvetinvestment.domain.models.fire.PortfolioProjectionPointDomain

fun FireReportDto.toDomain(): FireReportDomain {

    val portfolioChart = mutableListOf<PortfolioProjectionPointDomain>()
    val firePercentageChart = mutableListOf<FirePercentagePointDomain>()
    val projectionRows = mutableListOf<FireProjectionRowDomain>()

    data.projection.forEach { projection ->

        portfolioChart.add(
            PortfolioProjectionPointDomain(
                year = projection.year,
                portfolioValue = EmiValueDomain(
                    includeEmi = projection.portfolio_value.emi_include,
                    excludeEmi = projection.portfolio_value.emi_exclude
                )
            )
        )

        firePercentageChart.add(
            FirePercentagePointDomain(
                year = projection.year,
                percentage = EmiValueDomain(
                    includeEmi = projection.fire_percentage.emi_include,
                    excludeEmi = projection.fire_percentage.emi_exclude
                )
            )
        )

        projectionRows.add(
            FireProjectionRowDomain(
                year = projection.year,
                income = projection.income,

                expenses = EmiValueDomain(
                    includeEmi = projection.total_expenses.emi_include,
                    excludeEmi = projection.total_expenses.emi_exclude
                ),

                yearlyGoalSip = projection.goal_commitment_annual,

                surplusMoney = EmiValueDomain(
                    includeEmi = projection.savings.emi_include,
                    excludeEmi = projection.savings.emi_exclude
                ),

                goalsFutureValue = projection.goals_payout,

                portfolioValue = EmiValueDomain(
                    includeEmi = projection.portfolio_value.emi_include,
                    excludeEmi = projection.portfolio_value.emi_exclude
                ),

                fireNumber = EmiValueDomain(
                    includeEmi = projection.fire_number.emi_include.toLong(),
                    excludeEmi = projection.fire_number.emi_exclude.toLong()
                ),

                firePercentage = EmiValueDomain(
                    includeEmi = projection.fire_percentage.emi_include,
                    excludeEmi = projection.fire_percentage.emi_exclude
                ),
            )
        )
    }

    return FireReportDomain(
        startYear = data.projection.first().year,
        endYear = data.projection.last().year,
        portfolioChart = portfolioChart,
        firePercentageChart = firePercentageChart,
        projectionRows = projectionRows
    )
}