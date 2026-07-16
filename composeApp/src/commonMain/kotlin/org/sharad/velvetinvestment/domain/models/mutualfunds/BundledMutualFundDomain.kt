package org.sharad.velvetinvestment.domain.models.mutualfunds

data class BundledMutualFundDomain(
    val id: String,
    val bundleName: String,
    val bundleDescription: String,

    val equityPercentage: Int,
    val commodityPercentage: Int,
    val debtPercentage: Int,
    val hybridPercentage: Int,

    val riskLevel: String,
    val investmentTime: String,
    val investmentGrowth: String
)