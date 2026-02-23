package org.sharad.velvetinvestment.domain.models.mutualfunds

data class MutualFundDetailsDomain(
    val id: String,
    val name: String,
    val icon: String,
    val category: String,
    val amount: String,
    val risk: String?,
    val rating: Int?,
    val returnYear: Int,
    val type: String,
    val percentage: Double,
    val oneDayPercentage: Double,
    val today: String,
    val todayNav: Double,
    val minAmount: Long,
    val fundSize: Long,
    val assets: AssetsAllocationDomain,
    val topFunds: List<MutualFundDomain>,
)

data class AssetsAllocationDomain(
    val equity: Double,
    val debt: Double,
    val cash: Double
)

