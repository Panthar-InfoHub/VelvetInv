package org.sharad.velvetinvestment.domain.models.fire

data class FireProjectionRowDomain(
    val year: Int,
    val income: Long,
    val expenses: EmiValueDomain<Long>,
    val yearlyGoalSip: Long,
    val surplusMoney: EmiValueDomain<Long>,
    val goalsFutureValue: Long,
    val portfolioValue: EmiValueDomain<Long>,
    val fireNumber: EmiValueDomain<Long>,
    val firePercentage: EmiValueDomain<Double>,
)