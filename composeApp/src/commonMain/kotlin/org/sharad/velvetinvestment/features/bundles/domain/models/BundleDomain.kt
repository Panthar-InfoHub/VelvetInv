package org.sharad.velvetinvestment.features.bundles.domain.models

import org.sharad.velvetinvestment.features.core.domain.models.PurchaseMode

/** One row of `GET /bundles` — enough to draw a bundle card. */
data class BundleSummaryDomain(
    val id: String,
    val name: String,
    val description: String,
    /** The card's background banner; blank when the API sends none. */
    val imageUrl: String,
    /** The card's border colour as an ARGB value, or null when the API's hex is missing or unreadable. */
    val borderColor: Long? = null,
    val assetAllocation: AssetAllocationDomain,
    val metaData: BundleMetaDataDomain,
    /** Legacy categories shared by every way of investing; used only where [types] has no entry. */
    val categories: List<BundleCategorySummaryDomain>,
    /** The bundle's set-up for each way of investing, as `GET /bundles` now sends it per type. */
    val types: Map<PurchaseMode, BundleInvestmentTypeDomain> = emptyMap()
) {
    /** The categories for [mode], falling back to the legacy shared ones. */
    fun categoriesFor(mode: PurchaseMode): List<BundleCategorySummaryDomain> =
        types[mode]?.categories ?: categories

    /** Each slot is one fund in the bundle, so the fund count is the slot count. */
    fun fundCountFor(mode: PurchaseMode): Int = categoriesFor(mode).sumOf { it.slotCount }

    /** The start amount for [mode], falling back to meta_data's when the type doesn't give one. */
    fun startAmountFor(mode: PurchaseMode): Long? =
        types[mode]?.startAmount ?: metaData.startAmountFor(mode)

    /** The monthly SIP fund count — what a card without a mode selector shows. */
    val fundCount: Int
        get() = fundCountFor(PurchaseMode.MONTHLY)

    /** The asset classes the bundle actually holds, e.g. "Equity + Commodity + Hybrid". */
    val assetClassLabel: String
        get() = listOf(
            "Equity" to assetAllocation.equity,
            "Commodity" to assetAllocation.commodity,
            "Debt" to assetAllocation.debt,
            "Hybrid" to assetAllocation.hybrid
        )
            .filter { (_, percentage) -> percentage > 0 }
            .joinToString(" + ") { (name, _) -> name }
}

/** One way of investing in a bundle: its start amount and its own categories. */
data class BundleInvestmentTypeDomain(
    val id: String,
    val startAmount: Long?,
    val categories: List<BundleCategorySummaryDomain>
)

data class BundleCategorySummaryDomain(
    val id: String,
    val categoryName: String,
    val displayName: String,
    val allocationPercentage: Double,
    val slotCount: Int
)

/**
 * `GET /bundles/{id}` — the bundle the user configures before investing.
 *
 * As mapped, [modes] holds the API's set-up for each way of investing. The screen works on one
 * mode at a time — see [forMode] — where [categories] and [startAmount] are that mode's.
 */
data class BundleDetailsDomain(
    val name: String,
    val description: String,
    val assetAllocation: AssetAllocationDomain,
    val metaData: BundleMetaDataDomain,
    val categories: List<BundleCategoryDomain>,
    /** The start amount with the pre-selected funds, for the mode this bundle was made for. */
    val startAmount: Long? = null,
    val modes: Map<PurchaseMode, BundleModeDetailsDomain> = emptyMap()
) {
    val slots: List<PortfolioSlotDomain>
        get() = categories.flatMap { it.slots }

    /**
     * The bundle as set up for [mode]: that mode's categories, slots and pre-selected funds, and
     * its start amount. Falls back to the legacy shared categories and meta_data amounts where the
     * API sends no entry for the mode.
     */
    fun forMode(mode: PurchaseMode): BundleDetailsDomain {
        val modeDetails = modes[mode]
        return copy(
            categories = modeDetails?.categories ?: categories,
            startAmount = modeDetails?.startAmount ?: metaData.startAmountFor(mode)
        )
    }
}

/** One way of investing in a bundle: its start amount and its own categories. */
data class BundleModeDetailsDomain(
    val startAmount: Long?,
    val categories: List<BundleCategoryDomain>
)

data class AssetAllocationDomain(
    val equity: Double,
    val debt: Double,
    val hybrid: Double,
    val commodity: Double
)

data class BundleMetaDataDomain(
    /** The risk as the API words it, shown as-is in the bundle's tags. */
    val riskLevel: String,
    val investmentTime: String,
    val investmentGrowth: String,
    /** The smallest one-time (lumpsum) amount the bundle can be started with, when the API gives one. */
    val startAmount: Long? = null,
    // NEW: the smallest daily SIP amount, when the API gives one.
    val dailyStartAmount: Long? = null,
    // NEW: the smallest monthly SIP amount, when the API gives one.
    val monthlyStartAmount: Long? = null
) {
    // NEW: the start amount for [mode], or null when the API didn't send that one.
    fun startAmountFor(mode: PurchaseMode): Long? = when (mode) {
        PurchaseMode.MONTHLY -> monthlyStartAmount
        PurchaseMode.ONE_TIME -> startAmount
    }
}

data class BundleCategoryDomain(
    val id: String,
    val categoryName: String,
    val displayName: String,
    val allocationPercentage: Double,
    val funds: List<FundDomain>,
    val slots: List<PortfolioSlotDomain>
)

/**
 * One fund position inside a category, with a fixed share of the bundle.
 *
 * [defaultFund] is what the bundle recommends for the slot and never changes once mapped;
 * [selectedFund] is what the user has picked. Keeping both is what lets the user go back to the
 * recommendation after switching away from it.
 */
data class PortfolioSlotDomain(
    val id: String,
    val allocationPercentage: Double,
    val rank: Int,
    val defaultFund: FundDomain?,
    val selectedFund: FundDomain?
)

/**
 * A fund that can fill a slot.
 *
 * `GET /bundles/{id}` lists only a fund's identity, NAV and returns, so everything after
 * [metrics] is known only for a fund fetched on its own (`GET /mf/{id}`) — one picked while
 * exploring the full category — and is null or blank otherwise.
 */
data class FundDomain(
    val id: String,
    val name: String,
    val isin: String,
    val imageUrl: String,
    val latestNav: String,
    val latestNavDate: String,
    val metrics: FundMetricsDomain,

    val amcName: String = "",
    val assetType: String = "",
    val riskName: String = "",
    val sipAllowed: Boolean? = null,
    val transactionRules: TransactionRulesDomain? = null
)

data class FundMetricsDomain(
    val return1M: Double,
    val return1Y: Double,
    val return3Y: Double,
    val return5Y: Double,
    val return6M: Double,
    val return90D: Double
)

/**
 * A fund's limits. The three minimums the bundle uses — [minLumpSumAmount], [minMonthlySipAmount]
 * (with [minSipAmount]) and [minDailySipAmount] — are null when the fund can't be bought that way.
 */
data class TransactionRulesDomain(
    val id: String,
    val mfProductId: String,

    val minSipAmount: Long?,
    val minLumpSumAmount: Long?,
    val minInvestmentAmount: Long,

    val minLumpsumAddOnAmount: Long,
    val minRedemptionQty: Int,
    val minRedemptionAmount: Long,

    val minDailySipAmount: Long?,
    val minWeeklySipAmount: Long,
    val minFortnightlySipAmount: Long,
    val minMonthlySipAmount: Long?,
    val minQuarterlySipAmount: Long,
    val minSemiAnnualSipAmount: Long,
    val minAnnualSipAmount: Long,

    val sipAllowedDates: List<Int>,
    val sipFrequencies: List<String>,

    val createdAt: String,
    val updatedAt: String
)
