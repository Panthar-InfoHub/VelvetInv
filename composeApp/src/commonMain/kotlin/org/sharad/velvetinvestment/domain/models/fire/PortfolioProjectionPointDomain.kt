package org.sharad.velvetinvestment.domain.models.fire

data class PortfolioProjectionPointDomain(
    val year: Int,
    val portfolioValue: EmiValueDomain<Long>
)