package org.sharad.velvetinvestment.domain.models.fixeddeposits

data class FixedDepositTenureDomain(
    val tenure: TenureRange,
    val interestRate: Double,
    val receiveMin: Long,
    val receiveMax: Long
)