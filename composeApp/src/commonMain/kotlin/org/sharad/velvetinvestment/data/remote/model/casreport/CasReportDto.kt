package org.sharad.velvetinvestment.data.remote.model.casreport

import kotlinx.serialization.Serializable

@Serializable
data class CasReportDto(
    val code: Int,
    val message: String,
    val data: CASReport
)

@Serializable
data class CASReport(
    val portfolioSummary: PortfolioSummary,
    val mutualFunds: MutualFundsWrapper,
    val demat: DematWrapper
)

// 📊 Portfolio
@Serializable
data class PortfolioSummary(
    val totalCost: Double,
    val totalMarket: Double,
)

@Serializable
data class MutualFundsWrapper(
    val summary: MutualFundSummary
)

@Serializable
data class MutualFundSummary(
    val totalValuation: Double,
    val totalSchemes: Int,
    val totalCostValue: Double
)


@Serializable
data class DematWrapper(
    val summary: DematSummary
)


@Serializable
data class DematSummary(
    val totalValue: Double,
    val totalAccounts: Int,
    val totalHoldings: Int
)