package org.sharad.velvetinvestment.features.fd.domain.model

data class FDDetailsDomain(
    val id: String,
    val invest: Long = 1000,
    val selectedPayout: PayoutType?,
    val applicable: String = "",
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
    val keyFeatures: List<KeyFeatureDomain>,
    val faqs: List<FDFaqDomain>,
    val tags: List<String> = emptyList()
)

data class KeyFeatureDomain(
    val title: String,
    val description: String,
    val iconUrl: String? = null
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

sealed interface PayoutType {
    val id: String
    val displayName: String

    // "Cumulative" rather than "Maturity", which would read the same as [OnMaturity] beside it.
    data object Cumulative : PayoutType {
        override val id = "CUMULATIVE"
        override val displayName = "Cumulative"
    }

    data object Monthly : PayoutType {
        override val id = "MONTHLY"
        override val displayName = "Monthly"
    }

    data object Quarterly : PayoutType {
        override val id = "QUARTERLY"
        override val displayName = "Quarterly"
    }

    data object HalfYearly : PayoutType {
        override val id = "HALF_YEARLY"
        override val displayName = "Half Yearly"
    }

    data object Yearly : PayoutType {
        override val id = "YEARLY"
        override val displayName = "Yearly"
    }

    data object OnMaturity : PayoutType {
        override val id = "ON_MATURITY"
        override val displayName = "On Maturity"
    }

    data class Custom(
        override val id: String
    ) : PayoutType {
        override val displayName: String =
            id.lowercase().replace("_", " ")
                .replaceFirstChar { it.uppercase() }
    }

    companion object {
        /** Every frequency `GET /fd/{id}` accepts as `payout_frequency`, in dropdown order. */
        val all: List<PayoutType> = listOf(Cumulative, Monthly, Quarterly, HalfYearly, Yearly, OnMaturity)

        fun fromId(id: String): PayoutType {
            return when (id.uppercase()) {
                Cumulative.id -> Cumulative
                Monthly.id -> Monthly
                Quarterly.id -> Quarterly
                HalfYearly.id -> HalfYearly
                Yearly.id -> Yearly
                OnMaturity.id -> OnMaturity
                else -> Custom(id)
            }
        }

        fun defaultSelection(payouts: List<PayoutType>): PayoutType? {
            return payouts.firstOrNull { it is Cumulative }
                ?: payouts.firstOrNull()
        }
    }
}

/** The applicant categories `GET /fd/{id}` accepts as `customer_type`; [name] is the wire value. */
enum class CustomerType(val displayName: String) {
    STANDARD("Regular"),
    SENIOR_CITIZEN("Senior Citizen"),
    FEMALE("Women"),
    SENIOR_CITIZEN_FEMALE("Senior Citizen Women")
}
