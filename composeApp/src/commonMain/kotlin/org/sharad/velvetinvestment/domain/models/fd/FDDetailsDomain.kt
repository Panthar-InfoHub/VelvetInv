package org.sharad.velvetinvestment.domain.models.fd

import org.sharad.velvetinvestment.data.remote.mapper.PayoutType
import org.sharad.velvetinvestment.domain.models.fixeddeposits.RiskLevel

data class FDDetailsDomain(
    val id: String,

    val invest: Long =1000,
    val selectedPayout: PayoutType?,
    val applicable: String ="",

    val bankName: String,
    val bankLogo: String,
    val rating: String,
    val maxInterestRate: Double,
    val riskLabel: RiskLevel,

    val minDeposit: Long,

    val payoutOptions: List<PayoutType>,
    val applicableFor: List<String>,

    val interestRates: List<FDTenureDomain>,

    val lockInDays: Int,
    val prematurePenalty: Double,
    val insuranceAmount: String,

    val about: String,
    val faqs: List<FDFaqDomain>
)

data class FDTenureDomain(
    val id: String,
    val tenureLabel: String,
    val tenureDays: Int,
    val interestRate: Double,
    val annualYield: Double,
    val isDefault: Boolean,
    val payoutFrequency: PayoutType,
)
data class FDFaqDomain(
    val question: String,
    val answer: String
)