package org.sharad.velvetinvestment.domain.models.fire

data class FireReportDomain(
    val actual: FireScenarioDomain,
    val projected: FireScenarioDomain,
    val userName: String
)

data class FireScenarioDomain(
    val startYear: Int,
    val endYear: Int,
    val portfolioChart: List<PortfolioProjectionPointDomain>,
    val firePercentageChart: List<FirePercentagePointDomain>,
    val projectionRows: List<FireProjectionRowDomain>
)