package org.sharad.velvetinvestment.data.remote.mapper

import org.sharad.velvetinvestment.data.remote.model.fddetails.FDDetailsDto
import org.sharad.velvetinvestment.domain.models.fd.FDDetailsDomain
import org.sharad.velvetinvestment.domain.models.fd.FDFaqDomain
import org.sharad.velvetinvestment.domain.models.fd.FDTenureDomain
import org.sharad.velvetinvestment.domain.models.fixeddeposits.RiskLevel
import org.sharad.velvetinvestment.utils.parseHtmlToReadableText

fun FDDetailsDto.toDomain(): FDDetailsDomain {
    return FDDetailsDomain(
        id = data.id,
        invest =  data.min_deposit.toLongOrNull() ?: 1000,
        interestPayout = data.interest_rates.firstOrNull()?.payout_frequency?.toReadablePayout()?: "",
        applicable = data.interest_rates.firstOrNull()?.customer_type ?: "",


        // Header
        bankName = data.issuer.display_name,
        bankLogo = data.issuer.logo_url,
        rating = data.issuer.rating_text,
        maxInterestRate = data.interest_rates.maxOfOrNull {
            it.interest_rate.toDoubleOrNull() ?: 0.0
        } ?: 0.0,
        riskLabel = extractRiskLevel(data.issuer.rating_text),

        minDeposit = data.min_deposit.toLongOrNull() ?: 0L,

        payoutOptions = data.interest_rates
            .map { it.payout_frequency }
            .distinct()
            .map { it.toReadablePayout() },

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
                payoutFrequency = it.payout_frequency
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

fun String.toReadablePayout(): String {
    return when (this.uppercase()) {
        "CUMULATIVE" -> "Maturity"
        "MONTHLY" -> "Monthly"
        "QUARTERLY" -> "Quarterly"
        "HALF_YEARLY" -> "Half Yearly"
        "YEARLY" -> "Yearly"
        else -> this.lowercase().replaceFirstChar { it.uppercase() }
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