package org.sharad.velvetinvestment.data.remote.mapper

import org.sharad.velvetinvestment.data.remote.model.bundledfundbyid.BundleByIdDto
import org.sharad.velvetinvestment.data.remote.model.bundledfundbyid.Category
import org.sharad.velvetinvestment.data.remote.model.bundledfundbyid.Fund
import org.sharad.velvetinvestment.data.remote.model.bundledfundbyid.Metrics
import org.sharad.velvetinvestment.data.remote.model.bundledfundbyid.TransactionRules
import org.sharad.velvetinvestment.domain.models.bundle.AssetAllocationDomain
import org.sharad.velvetinvestment.domain.models.bundle.BundleCategoryDomain
import org.sharad.velvetinvestment.domain.models.bundle.BundleDomain
import org.sharad.velvetinvestment.domain.models.bundle.BundleMetaDataDomain
import org.sharad.velvetinvestment.domain.models.bundle.FundDomain
import org.sharad.velvetinvestment.domain.models.bundle.FundMetricsDomain
import org.sharad.velvetinvestment.domain.models.bundle.PortfolioSlotDomain
import org.sharad.velvetinvestment.domain.models.bundle.TransactionRulesDomain
import org.sharad.velvetinvestment.presentation.bundle.viewmodel.BundleTransactionRules
import kotlin.math.ceil

fun BundleByIdDto.toDomain(): BundleDomain {
    return BundleDomain(
        name = data.bundle_name,
        description = data.bundle_description,
        assetAllocation = AssetAllocationDomain(
            equity = data.equity_percentage,
            debt = data.debt_percentage,
            hybrid = data.hybrid_percentage,
            commodity = data.commodity_percentage
        ),
        metaData = BundleMetaDataDomain(
            riskLevel = data.meta_data.risk_level,
            investmentTime = data.meta_data.investment_time,
            investmentGrowth = data.meta_data.investment_growth
        ),
        categories = data.categories.map { it.toDomain() }
    )
}
private fun Category.toDomain(): BundleCategoryDomain {

    val domainFunds = funds.map { it.toDomain() }

    return BundleCategoryDomain(
        id = id,
        categoryName = category_name,
        displayName = display_name,
        allocationPercentage = total_percentage,
        funds = domainFunds,
        slots = slots.mapIndexed { index, slot ->
            PortfolioSlotDomain(
                id = slot.id,
                allocationPercentage = slot.allocation_percentage,
                rank = slot.default_rank,
                selectedFund = domainFunds.getOrElse(index) {
                    domainFunds.first()
                }
            )
        }
    )
}
private fun Fund.toDomain(): FundDomain {
    return FundDomain(
        id = id,
        schemeId = scheme_id,
        isin = isin,
        imageUrl = img_url,
        mappingCode = mapping_code,
        schemeName = scheme_name,
        displayName1 = display_name_001,
        displayName2 = display_name_002,
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
        metrics = metrics.toDomain(),
        transactionRules = transaction_rules.toDomain()
    )
}
private fun Metrics.toDomain(): FundMetricsDomain {
    return FundMetricsDomain(
        return1Y = return_1y?:0.0,
        return3Y = return_3y?:0.0,
        return5Y = return_5y?:0.0,
        return6M = return_6m?:0.0,
        return90D = return_90d?:0.0
    )
}

private fun TransactionRules.toDomain(): TransactionRulesDomain {
    return TransactionRulesDomain(
        id = id,
        mfProductId = mf_product_id,
        minSipAmount = min_sip_amount.toIntOrNull() ?: 0,
        minLumpSumAmount = min_lump_sum_amount.toIntOrNull() ?: 0,
        minInvestmentAmount = min_investment_amount.toIntOrNull() ?: 0,
        minLumpsumAddOnAmount = min_lumpsum_add_on_amount.toIntOrNull() ?: 0,
        minRedemptionQty = min_redem_qty.toIntOrNull() ?: 0,
        minRedemptionAmount = min_redem_amount.toIntOrNull() ?: 0,
        minDailySipAmount = min_daily_sip_amount.toIntOrNull() ?: 0,
        minWeeklySipAmount = min_weekly_sip_amount.toIntOrNull() ?: 0,
        minFortnightlySipAmount = min_fortnightly_sip_amount.toIntOrNull() ?: 0,
        minMonthlySipAmount = min_monthly_sip_amount.toIntOrNull() ?: 0,
        minQuarterlySipAmount = min_quarterly_sip_amount.toIntOrNull() ?: 0,
        minSemiAnnualSipAmount = min_semi_annual_sip_amount.toIntOrNull() ?: 0,
        minAnnualSipAmount = min_annual_sip_amount.toIntOrNull() ?: 0,
        sipAllowedDates = sip_allowed_dates,
        sipFrequencies = sip_frequencies,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun BundleDomain.deriveTransactionRules(): BundleTransactionRules {

    val slots = categories.flatMap { it.slots }

    val dates = slots
        .map { it.selectedFund.transactionRules.sipAllowedDates.toSet() }
        .reduce(Set<Int>::intersect)
        .sorted()

    val frequencies = slots
        .map { it.selectedFund.transactionRules.sipFrequencies.toSet() }
        .reduce(Set<String>::intersect)
        .sorted()

    return BundleTransactionRules(
        minBundleSipAmount = calculateBundleMinimum(
            slots,
            selector = { it.minSipAmount }
        ),

        minBundleLumpsumAmount = calculateBundleMinimum(
            slots,
            selector = { it.minLumpSumAmount }
        ),

        minDailySipAmount = calculateBundleMinimum(
            slots,
            selector = { it.minDailySipAmount }
        ),

        minWeeklySipAmount = calculateBundleMinimum(
            slots,
            selector = { it.minWeeklySipAmount }
        ),

        minFortnightlySipAmount = calculateBundleMinimum(
            slots,
            selector = { it.minFortnightlySipAmount }
        ),

        minMonthlySipAmount = calculateBundleMinimum(
            slots,
            selector = { it.minMonthlySipAmount }
        ),

        minQuarterlySipAmount = calculateBundleMinimum(
            slots,
            selector = { it.minQuarterlySipAmount }
        ),

        minSemiAnnualSipAmount = calculateBundleMinimum(
            slots,
            selector = { it.minSemiAnnualSipAmount }
        ),

        minAnnualSipAmount = calculateBundleMinimum(
            slots,
            selector = { it.minAnnualSipAmount }
        ),

        sipAllowedDates = dates,
        sipFrequencies = frequencies
    )
}

private fun calculateBundleMinimum(
    slots: List<PortfolioSlotDomain>,
    selector: (TransactionRulesDomain) -> Int
): Int {

    return slots.maxOfOrNull { slot ->

        val min = selector(slot.selectedFund.transactionRules)

        if (min == 0) {
            0
        } else {
            val required = ceil(
                min * 100.0 / slot.allocationPercentage
            ).toInt()

            roundUpToNext10(required)
        }

    } ?: 0
}

private fun roundUpToNext10(value: Int): Int {
    return ((value + 9) / 10) * 10
}