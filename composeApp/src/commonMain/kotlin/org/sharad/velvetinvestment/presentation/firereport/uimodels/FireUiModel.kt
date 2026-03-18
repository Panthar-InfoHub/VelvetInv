package org.sharad.velvetinvestment.presentation.firereport.uimodels

data class FireReportUiModel(
    val startYear: Int,
    val endYear: Int,
    val portfolioChart: List<PortfolioProjectionPointUiModel>,
    val firePercentageChart: List<FirePercentagePointUiModel>,
    val projectionRows: List<FireProjectionRowUiModel>
)
data class PortfolioProjectionPointUiModel(
    val year: Int,
    val portfolioValue: Long
)

data class FirePercentagePointUiModel(
    val year: Int,
    val percentage: Double
)
data class FireProjectionRowUiModel(
    val year: Int,
    val income: Long,
    val expenses: Long,
    val yearlyGoalSip: Long,
    val surplusMoney: Long,
    val goalsFutureValue: Long,
    val portfolioValue: Long,
    val fireNumber: Long,
    val firePercentage: Double,
)