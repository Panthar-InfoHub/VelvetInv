package org.sharad.velvetinvestment.domain.models.bundle

data class BundleDomain(
    val name: String,
    val description: String,
    val assetAllocation: AssetAllocationDomain,
    val metaData: BundleMetaDataDomain,
    val categories: List<BundleCategoryDomain>
)

data class AssetAllocationDomain(
    val equity: Double,
    val debt: Double,
    val hybrid: Double,
    val commodity: Double
)

data class BundleMetaDataDomain(
    val riskLevel: String,
    val investmentTime: String,
    val investmentGrowth: String
)

data class BundleCategoryDomain(
    val id: String,
    val categoryName: String,
    val displayName: String,
    val allocationPercentage: Double,
    val funds: List<FundDomain>,
    val slots: List<PortfolioSlotDomain>
)

data class PortfolioSlotDomain(
    val id: String,
    val allocationPercentage: Double,
    val rank: Int,
    val selectedFund: FundDomain?
)

data class FundDomain(
    val id: String,
    val schemeId: String,
    val isin: String,
    val imageUrl: String,

    val mappingCode: String,

    val schemeName: String,
    val displayName1: String,
    val displayName2: String,

    val amcId: String,
    val amcCode: String,
    val amcName: String,

    val assetType: String,
    val schemeType: String,
    val structure: String,

    val riskName: String,
    val riskLevel: Int,

    val latestNav: String,
    val latestNavDate: String,

    val purchaseAllowed: Boolean,
    val sipAllowed: Boolean,
    val redemptionAllowed: Boolean,
    val switchAllowed: Boolean,

    val maturityDate: String?,
    val nfoEndDate: String?,

    val createdAt: String,
    val updatedAt: String,

    val nseSchemeCode: String,
    val platformCode: String,

    val metrics: FundMetricsDomain,
    val transactionRules: TransactionRulesDomain
)

data class FundMetricsDomain(
    val return1Y: Double,
    val return3Y: Double,
    val return5Y: Double,
    val return6M: Double,
    val return90D: Double
)

data class TransactionRulesDomain(
    val id: String,
    val mfProductId: String,

    val minSipAmount: Long,
    val minLumpSumAmount: Long,
    val minInvestmentAmount: Long,

    val minLumpsumAddOnAmount: Long,
    val minRedemptionQty: Int,
    val minRedemptionAmount: Long,

    val minDailySipAmount: Long,
    val minWeeklySipAmount: Long,
    val minFortnightlySipAmount: Long,
    val minMonthlySipAmount: Long,
    val minQuarterlySipAmount: Long,
    val minSemiAnnualSipAmount: Long,
    val minAnnualSipAmount: Long,

    val sipAllowedDates: List<Int>,
    val sipFrequencies: List<String>,

    val createdAt: String,
    val updatedAt: String
)