package org.sharad.velvetinvestment.presentation.firereport.uimodels

import org.sharad.velvetinvestment.domain.models.fire.FireReportDomain
import org.sharad.velvetinvestment.presentation.firereport.compose.LineChartData
import org.sharad.velvetinvestment.utils.formatMoneyWithUnits
import org.sharad.velvetinvestment.utils.trimTo

fun FireReportDomain.toUiModel(
    includeEmi: Boolean,
    showProjected: Boolean,
    yearLimit: Int
): FireReportUiModel {

    val scenario = if (showProjected) projected else actual

    return FireReportUiModel(
        startYear = scenario.startYear,
        endYear = scenario.endYear,

        portfolioChart = scenario.portfolioChart
            .take(yearLimit)
            .map {
                PortfolioProjectionPointUiModel(
                    year = it.year,
                    portfolioValue =
                        if (includeEmi) it.portfolioValue.includeEmi
                        else it.portfolioValue.excludeEmi
                )
            },

        firePercentageChart = scenario.firePercentageChart
            .take(yearLimit)
            .map {
                FirePercentagePointUiModel(
                    year = it.year,
                    percentage =
                        if (includeEmi) it.percentage.includeEmi
                        else it.percentage.excludeEmi
                )
            },

        projectionRows = scenario.projectionRows
            .take(yearLimit)
            .map { row ->

                FireProjectionRowUiModel(
                    year = row.year,

                    income = row.income,

                    expenses =
                        if (includeEmi) row.expenses.includeEmi
                        else row.expenses.excludeEmi,

                    yearlyGoalSip = row.yearlyGoalSip,

                    surplusMoney =
                        if (includeEmi) row.surplusMoney.includeEmi
                        else row.surplusMoney.excludeEmi,

                    goalsFutureValue = row.goalsFutureValue,

                    portfolioValue =
                        if (includeEmi) row.portfolioValue.includeEmi
                        else row.portfolioValue.excludeEmi,

                    fireNumber =
                        if (includeEmi) row.fireNumber.includeEmi
                        else row.fireNumber.excludeEmi,

                    firePercentage =
                        if (includeEmi) row.firePercentage.includeEmi
                        else row.firePercentage.excludeEmi
                )
            }
    )
}

fun List<PortfolioProjectionPointUiModel>.toMapPoints(): List<LineChartData> {
    return map {
        LineChartData(
            floatingLabel = "₹ ${formatMoneyWithUnits(it.portfolioValue)}",
            value = it.portfolioValue.toDouble(),
            axisLabel = it.year.toString()
        )
    }
}

fun List<FirePercentagePointUiModel>.toFireMapPoints(): List<LineChartData> {
    return map {
        LineChartData(
            floatingLabel = "${it.percentage.trimTo(1)}%",
            value = it.percentage,
            axisLabel = it.year.toString()
        )
    }
}