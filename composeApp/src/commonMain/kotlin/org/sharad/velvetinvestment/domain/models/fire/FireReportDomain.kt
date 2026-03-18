package org.sharad.velvetinvestment.domain.models.fire

data class FireReportDomain(
    val startYear: Int,
    val endYear: Int,
    val portfolioChart: List<PortfolioProjectionPointDomain>,
    val firePercentageChart: List<FirePercentagePointDomain>,
    val projectionRows: List<FireProjectionRowDomain>
)