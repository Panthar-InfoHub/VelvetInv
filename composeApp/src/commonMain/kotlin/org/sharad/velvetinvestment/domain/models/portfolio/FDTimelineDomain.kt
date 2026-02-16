package org.sharad.velvetinvestment.domain.models.portfolio

data class FDTimelineDomain(
    val startDate: String,
    val maturityDate: String,
    val daysRemaining: Int
)
