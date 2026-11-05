package org.sharad.velvetinvestment.features.bundles.data.remote.model

import kotlinx.serialization.Serializable

/** `GET /bundles` — every bundle with its allocation and category slots, without funds. */
@Serializable
data class AllBundlesDto(
    val success: Boolean,
    val message: String,
    val data: AllBundlesDataDto
)

@Serializable
data class AllBundlesDataDto(
    val bundles: List<BundleSummaryDto> = emptyList(),
    val pagination: BundlesPaginationDto? = null
)

@Serializable
data class BundleSummaryDto(
    val id: String,
    val bundle_name: String,
    val bundle_description: String? = null,
    val equity_percentage: Double = 0.0,
    val commodity_percentage: Double = 0.0,
    val debt_percentage: Double = 0.0,
    val hybrid_percentage: Double = 0.0,
    /** The card's background banner. */
    val img_url: String? = null,
    /** The card's border colour, as "#RRGGBB" (or "#AARRGGBB"). */
    val hex: String? = null,
    // NEW: meta_data now also carries daily_start_amount and monthly_start_amount (see BundleMetaDataDto).
    val meta_data: BundleMetaDataDto? = null,
    /** Legacy: one set of categories for every way of investing. Superseded by [types]. */
    val categories: List<BundleSummaryCategoryDto> = emptyList(),
    /** One entry per way of investing (DAILY_SIP, MONTHLY_SIP, LUMPSUM), each with its own funds. */
    val types: List<BundleInvestmentTypeDto> = emptyList()
)

/** A bundle's set-up for one way of investing: its start amount and its own category slots. */
@Serializable
data class BundleInvestmentTypeDto(
    val id: String,
    /** "DAILY_SIP", "MONTHLY_SIP" or "LUMPSUM". */
    val investment_type: String,
    val start_amount: Double? = null,
    val categories: List<BundleSummaryCategoryDto> = emptyList()
)

@Serializable
data class BundleSummaryCategoryDto(
    val id: String,
    val bundle_id: String = "",
    val category_name: String,
    val display_name: String,
    val total_percentage: Double = 0.0,
    val slots: List<BundleSlotDto> = emptyList()
)

@Serializable
data class BundlesPaginationDto(
    val total: Int = 0,
    val page: Int = 1,
    val limit: Int = 0,
    val totalPages: Int = 0
)
