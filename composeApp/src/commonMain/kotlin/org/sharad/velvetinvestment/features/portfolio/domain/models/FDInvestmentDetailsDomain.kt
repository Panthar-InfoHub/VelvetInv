package org.sharad.velvetinvestment.features.portfolio.domain.models

data class FDInvestmentDetailsDomain(
    val principalAmount: Long,
    val interestRate: Double,
    val tenureMonths: Int,
    val maturityAmount: Long,
    val interestEarnedTillDate: Long
)
