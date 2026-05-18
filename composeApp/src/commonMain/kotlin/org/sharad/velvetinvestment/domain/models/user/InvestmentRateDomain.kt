package org.sharad.velvetinvestment.domain.models.user

data class InvestmentRateDomain(
    val currentSavingPercentage: Double,
    val previousMonthSavingPercentage: Double,
    val percentDelta: Double,
    val savingDelta: Long,
    val trends: List<SavingTrendsDomain>,
    val spendingCategories: SpendingCategoriesDomain,
)

data class SavingTrendsDomain(
    val monthText: String,
    val savings: Long,
    val investments: Long,
)

data class SpendingCategoriesDomain(
    val savings: SpendingChartData,
    val investments: InvestmentChartData,
    val essentials: EssentialsChartData,
)

data class SpendingChartData(
    val amount: Long,
    val percent: Double,
)

data class InvestmentChartData(
    val amount: Long,
    val percent: Double,
    val breakdown: InvestmentBreakdownDomain,
)

data class InvestmentBreakdownDomain(
    val mutualFunds: SpendingChartData,
    val fixedDeposits: SpendingChartData,
)

data class EssentialsChartData(
    val amount: Long,
    val percent: Double,
    val breakdown: EssentialsBreakdownDomain,
)

data class EssentialsBreakdownDomain(
    val house: SpendingChartData,
    val food: SpendingChartData,
    val transportation: SpendingChartData,
    val others: SpendingChartData,
)