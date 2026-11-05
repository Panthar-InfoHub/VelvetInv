package org.sharad.velvetinvestment.features.fireonboarding.presentation.onboarding.models

data class AssetFlowDetails(
    val mutualFunds: Long? = null,
    val stocksAndShares: Long? = null,
    val fixedDeposits: Long? = null,
    val realEstate: Long? = null,
    val goldAndCommodities: Long? = null,
    val cash: Long? = null
)

/** Every asset class added together; a blank one counts as zero. */
fun AssetFlowDetails.total(): Long =
    listOf(mutualFunds, stocksAndShares, fixedDeposits, realEstate, goldAndCommodities, cash).sumOf { it ?: 0L }
