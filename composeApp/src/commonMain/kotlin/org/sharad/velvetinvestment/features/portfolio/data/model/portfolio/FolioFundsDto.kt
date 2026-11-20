package org.sharad.velvetinvestment.features.portfolio.data.model.portfolio

import kotlinx.serialization.Serializable

/** `GET /user/portfolio/{folio}` — every holding in one folio, each with its `mf_holding_id`. */
@Serializable
data class FolioFundsDto(
    val code: Int? = null,
    val message: String? = null,
    val `data`: List<FolioFundDataDto> = emptyList()
)

/**
 * One holding in a folio. Everything but [id] is optional so a holding with no units allotted yet
 * (null NAVs, returns) still reads.
 */
@Serializable
data class FolioFundDataDto(
    /** The scheme's product id. */
    val id: String,
    /** What redemption is keyed on, and how a folio screen row finds its holding here. */
    val mf_holding_id: String? = null,
    /** The scheme's ISIN. */
    val scheme_id: String? = null,
    val title: String? = null,
    val category: String? = null,
    val amount: Double? = null,
    val current_value: Double? = null,
    val is_sip: Boolean? = null,
    val start_date: String? = null,
    val return_percentage: Double? = null,
    val `return`: Double? = null,
    val xirr: Double? = null,
    val current_nav: Double? = null,
    val avg_nav: Double? = null,
    val folio: String? = null,
    val actual_folio: String? = null,
    val balance_units: Double? = null,
    val img_url: String? = null,
    val order_id: String? = null,
    /** The purchase plan behind a SIP holding (`mfpp_…`). */
    val fp_id: String? = null,
    /** The plan's state, e.g. ACTIVE / CANCELLED. */
    val state: String? = null,
    val frequency: String? = null,
    val next_installment_date: String? = null,
    val synced_at: String? = null
)
