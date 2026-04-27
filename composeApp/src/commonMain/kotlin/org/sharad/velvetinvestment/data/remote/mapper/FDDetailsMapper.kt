package org.sharad.velvetinvestment.data.remote.mapper

import org.sharad.velvetinvestment.data.remote.model.fddetails.FDDetailsDto
import org.sharad.velvetinvestment.domain.models.fd.FDDetailsDomain
import org.sharad.velvetinvestment.domain.models.fd.FDFaqDomain
import org.sharad.velvetinvestment.domain.models.fd.FDTenureDomain
import org.sharad.velvetinvestment.domain.models.fixeddeposits.RiskLevel
import org.sharad.velvetinvestment.utils.parseHtmlToReadableText

fun FDDetailsDto.toDomain(): FDDetailsDomain {
    val payouts = data.interest_rates
        .distinctBy { it.payout_frequency }
        .map {
            PayoutType.fromId(it.payout_frequency)
        }

    val selectedPayout = PayoutType.defaultSelection(payouts)
    return FDDetailsDomain(
        id = data.id,
        invest =  data.min_deposit.toLongOrNull() ?: 1000,
        selectedPayout = selectedPayout,
        applicable = data.interest_rates.firstOrNull()?.customer_type ?: "",


        // Header
        bankName = data.issuer.display_name,
        bankLogo = data.issuer.logo_url,
        rating = data.issuer.rating_text?: "N/A",
        maxInterestRate = data.interest_rates.maxOfOrNull {
            it.interest_rate.toDoubleOrNull() ?: 0.0
        } ?: 0.0,
        riskLabel = if (data.issuer.rating_text != null) extractRiskLevel(data.issuer.rating_text) else RiskLevel.LOW,

        minDeposit = data.min_deposit.toLongOrNull() ?: 0L,

        payoutOptions = payouts,

        applicableFor = data.interest_rates
            .map { it.customer_type }
            .distinct()
            .map { it.toReadableCustomerType() },

        // Interest Table
        interestRates = data.interest_rates.map {
            FDTenureDomain(
                id = it.id,
                tenureLabel = it.tenure_label,
                tenureDays = it.tenure_days,
                interestRate = it.interest_rate.toDoubleOrNull() ?: 0.0,
                annualYield = it.annualized_yield.toDoubleOrNull() ?: 0.0,
                isDefault = it.is_default_selection,
                payoutFrequency = PayoutType.fromId(it.payout_frequency)
            )
        }.sortedBy { it.tenureDays }, // IMPORTANT → UI order

        // Lock Section
        lockInDays = data.lock_in_period_days,
        prematurePenalty = data.premature_penalty_percent.toDouble(),

        insuranceAmount = "₹5L",

        // About
        about = data.issuer.about_description,

        // FAQ
        faqs = data.faqs.map {
            FDFaqDomain(
                question = it.title,
                answer = it.description.parseHtmlToReadableText()
            )
        }
    )
}

sealed interface PayoutType {
    val id: String
    val displayName: String

    data object Cumulative : PayoutType {
        override val id = "CUMULATIVE"
        override val displayName = "Maturity"
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

    data class Custom(
        override val id: String
    ) : PayoutType {
        override val displayName: String =
            id.lowercase().replace("_", " ")
                .replaceFirstChar { it.uppercase() }
    }

    companion object {
        fun fromId(id: String): PayoutType {
            return when (id.uppercase()) {
                Cumulative.id -> Cumulative
                Monthly.id -> Monthly
                Quarterly.id -> Quarterly
                HalfYearly.id -> HalfYearly
                Yearly.id -> Yearly
                else -> Custom(id)
            }
        }

        fun defaultSelection(payouts: List<PayoutType>): PayoutType? {
            return payouts.firstOrNull { it is Cumulative }
                ?: payouts.firstOrNull()
        }
    }
}

fun String.toReadableCustomerType(): String {
    return when (this.uppercase()) {
        "STANDARD" -> "Regular"
        "SENIOR_CITIZEN" -> "Senior Citizen"
        "WOMEN" -> "Women"
        else -> this.lowercase().replaceFirstChar { it.uppercase() }
    }
}

fun extractRiskLevel(ratingText: String): RiskLevel {
    return when {
        "AAA" in ratingText || "A1+" in ratingText -> RiskLevel.LOW
        "AA" in ratingText || "A+" in ratingText -> RiskLevel.MODERATE
        else -> RiskLevel.HIGH
    }
}