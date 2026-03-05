package org.sharad.velvetinvestment.domain.models.fire

data class QuarterlyTrendDomain(
    val quarter: String,
    val netWorth: Long,
    val fireProgress: Double
)
