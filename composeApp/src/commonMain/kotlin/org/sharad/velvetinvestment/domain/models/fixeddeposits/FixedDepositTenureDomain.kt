package org.sharad.velvetinvestment.domain.models.fixeddeposits

import org.sharad.velvetinvestment.data.remote.mapper.TenureRangeList

data class FixedDepositTenureDomain(
    val tenure: TenureRangeList,
    val interestRate: Double,
    val receiveMin: Long,
    val receiveMax: Long
)