package org.sharad.velvetinvestment.domain.models.portfolio

data class MutualFundPortfolioDomain(
    val id: Int,
    val title: String,
    val category: String,
    val amount: Double,
    val isSip: Boolean,
    val startDate: String,
    val returnPercentage: String,
    val returnAmount: Int,
    val xirr: String,
    val currentNav: Double,
    val avgNav: Double,
    val folio: String,
    val balanceUnits: Double,
    val icon: String
)

data class FixedDepositPortfolioDomain(
    val id: String,
    val amount: String,
    val roiAtBooking: String,
    val tenureAtBooking: Int,
    val fdIssuedAt: String?,
    val status: String,
    val maturityAmount: String?,
    val userId: String,
    val userFullName: String,
    val userEmail: String,
    val issuerLogoUrl: String,
    val issuerDisplayName: String,
    val maturityDate: String?
)

data class PortfolioDashboardDomain(
    val currentValue: Double,
    val investedAmount: Double,
    val totalReturns: Int,
    val returnPercent: Double
)

data class PortfolioAllocationDomain(
    val mutualFunds: PortfolioAllocationItemDomain,
    val fixedDeposits: PortfolioAllocationItemDomain
)

data class PortfolioAllocationItemDomain(
    val value: Double,
    val percent: Double
)

data class TotalInvestmentsDomain(
    val currentValue: Double,
    val totalReturns: Double,
    val returnPercent: Double,
    val allocation: PortfolioAllocationDomain
)

data class InvestedAmountBreakdownDomain(
    val investedAmount: Double,
    val investedItemsCount: Int,
    val returnsAmount: Double,
    val returnsPercent: Double
)

data class PortfolioDomain(
    val dashboard: PortfolioDashboardDomain,
    val totalInvestments: TotalInvestmentsDomain,
    val investedAmountBreakdown: InvestedAmountBreakdownDomain,
    val mutualFunds: List<MutualFundPortfolioDomain>,
    val fixedDeposits: List<FixedDepositPortfolioDomain>
)