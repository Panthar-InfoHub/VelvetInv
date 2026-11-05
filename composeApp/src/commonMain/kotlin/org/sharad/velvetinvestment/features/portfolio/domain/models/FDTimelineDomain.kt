package org.sharad.velvetinvestment.features.portfolio.domain.models

data class FDTimelineDomain(
    val startDate: String,
    val maturityDate: String,
    val daysRemaining: Int
)
