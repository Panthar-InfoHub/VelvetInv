package org.sharad.velvetinvestment.data.remote.model.investmentratedto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Investments(
    val amount: Long,
    val percent: Double,
    val breakdown: InvestmentBreakdown,
)

@Serializable
data class InvestmentBreakdown(
    @SerialName("mutual_funds")
    val mutualFunds: InvestmentItem,

    @SerialName("fixed_deposits")
    val fixedDeposits: InvestmentItem,
)

@Serializable
data class InvestmentItem(
    val amount: Long,
    val percent: Double,
)