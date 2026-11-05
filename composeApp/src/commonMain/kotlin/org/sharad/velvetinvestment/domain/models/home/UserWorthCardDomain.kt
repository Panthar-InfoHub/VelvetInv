package org.sharad.velvetinvestment.domain.models.home

data class UserWorthCardDomain(
    val totalNetWorth:Long,
    val absGrowth:Long,
    val CARGGrowth:Double,
    val investingRate:Double,
    /** Current value of the user's Velvet mutual-fund holdings (`dashboard.mutual_funds`). */
    val mutualFunds: Long = 0L,
    /** Current value of the user's Velvet fixed deposits (`dashboard.fixed_deposits`). */
    val fixedDeposits: Long = 0L,
)
