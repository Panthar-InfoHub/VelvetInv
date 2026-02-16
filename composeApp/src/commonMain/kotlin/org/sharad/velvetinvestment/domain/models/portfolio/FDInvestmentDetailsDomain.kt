package org.sharad.velvetinvestment.domain.models.portfolio

data class FDInvestmentDetailsDomain(
    val principalAmount: Long,
    val interestRate: Double,
    val tenureMonths: Int,
    val maturityAmount: Long,
    val interestEarnedTillDate: Long
)
