package org.sharad.velvetinvestment.domain.models.user

data class InvestmentRateDomain(
    val currentSavingPercentage: Double,
    val previousMonthSavingPercentage: Double,
    val percentDelta: Double,
    val savingDelta:Long,
    val trends: List<SavingTrendsDomain>,
    val spendingCategories: SpendingCategoriesDomain
)

data class SavingTrendsDomain(
    val monthText: String,
    val savings: Long,
    val investments: Long,
)

data class SpendingCategoriesDomain(
    val savings: SpendingChartData,
    val investments: SpendingChartData,
    val essentials: SpendingChartData
)

data class SpendingChartData(
    val amount: Long,
    val percent: Double
)