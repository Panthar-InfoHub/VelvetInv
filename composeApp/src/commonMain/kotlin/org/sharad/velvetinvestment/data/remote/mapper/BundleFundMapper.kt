package org.sharad.velvetinvestment.data.remote.mapper

import org.sharad.velvetinvestment.domain.models.bundle.FundDomain
import org.sharad.velvetinvestment.domain.models.bundle.FundMetricsDomain
import org.sharad.velvetinvestment.domain.models.bundle.TransactionRulesDomain
import org.sharad.velvetinvestment.domain.models.mutualfunds.MutualFundDetailsDomain

fun MutualFundDetailsDomain.toFundDomain(): FundDomain {
    return FundDomain(
        id = id,
        schemeId = scheme_id,
        isin = isin ?: "",
        imageUrl = icon,
        mappingCode = mapping_code,
        schemeName = scheme_name,
        displayName1 = scheme_name, // Simplified for search result
        displayName2 = "",
        amcId = amc_id,
        amcCode = amc_code,
        amcName = amc_name,
        assetType = asset_type,
        schemeType = scheme_type,
        structure = structure,
        riskName = risk_name,
        riskLevel = risk_level,
        latestNav = latest_nav,
        latestNavDate = latest_nav_date,
        purchaseAllowed = purchase_allowed,
        sipAllowed = sip_allowed,
        redemptionAllowed = redemption_allowed,
        switchAllowed = switch_allowed,
        maturityDate = maturity_date,
        nfoEndDate = nfo_end_date,
        createdAt = createdAt,
        updatedAt = updatedAt,
        nseSchemeCode = nse_scheme_code,
        platformCode = platform_code,
        metrics = FundMetricsDomain(
            return1Y = metrics.return_1y ?: 0.0,
            return3Y = metrics.return_3y ?: 0.0,
            return5Y = metrics.return_5y ?: 0.0,
            return6M = metrics.return_6m ?: 0.0,
            return90D = metrics.return_90d ?: 0.0
        ),
        transactionRules = TransactionRulesDomain(
            id = "", // Not available in details
            mfProductId = id,
            minSipAmount = minSipAmount,
            minLumpSumAmount = minLumpSumAmount,
            minInvestmentAmount = minAmount,
            minLumpsumAddOnAmount = 0,
            minRedemptionQty = 0,
            minRedemptionAmount = 0,
            minDailySipAmount = 0,
            minWeeklySipAmount = 0,
            minFortnightlySipAmount = 0,
            minMonthlySipAmount = minSipAmount,
            minQuarterlySipAmount = 0,
            minSemiAnnualSipAmount = 0,
            minAnnualSipAmount = 0,
            sipAllowedDates = sipAllowedDated,
            sipFrequencies = investmentFrequency.map { it.code },
            createdAt = "",
            updatedAt = ""
        )
    )
}