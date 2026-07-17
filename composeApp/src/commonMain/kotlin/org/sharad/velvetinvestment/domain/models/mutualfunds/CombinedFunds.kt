package org.sharad.velvetinvestment.domain.models.mutualfunds

import org.sharad.velvetinvestment.presentation.mutualfund.CategoryMutualFundDomain

data class CombinedFundsDomain(
    val bundleFunds: List<BundledMutualFundDomain> = emptyList(),
    val categoryMutualFundDomain: List<CategoryMutualFundDomain> = emptyList()
)

data class CuratedBundleDomain(
    val id: String,
    val name: String,
    val description: String,
    val equityPercentage: Int,
    val commodityPercentage: Int,
    val debtPercentage: Int,
    val hybridPercentage: Int,
    val metaData: BundleMetaDataDomain
)

data class BundleMetaDataDomain(
    val riskLevel: String,
    val investmentTime: String,
    val investmentGrowth: String
)

fun CuratedBundleDomain.toBundledMutualFundDomain(): BundledMutualFundDomain {
    return BundledMutualFundDomain(
        id = id,
        bundleName = name,
        bundleDescription = description,

        equityPercentage = equityPercentage,
        commodityPercentage = commodityPercentage,
        debtPercentage = debtPercentage,
        hybridPercentage = hybridPercentage,

        riskLevel = metaData.riskLevel,
        investmentTime = metaData.investmentTime,
        investmentGrowth = metaData.investmentGrowth
    )
}