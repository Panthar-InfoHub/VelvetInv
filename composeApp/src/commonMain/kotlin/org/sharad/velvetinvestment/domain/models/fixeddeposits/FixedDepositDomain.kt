package org.sharad.velvetinvestment.domain.models.fixeddeposits

data class FixedDepositDomain(
    val id: String,
    val bankName: String,
    val bankLogoUrl: String,
    val riskLevel: RiskLevel,
    val baseInterest: Double,
    val tenures: List<FixedDepositTenureDomain>,
    val bankTag:String
)

