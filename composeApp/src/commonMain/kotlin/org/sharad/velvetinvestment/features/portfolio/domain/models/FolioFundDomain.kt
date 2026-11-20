package org.sharad.velvetinvestment.features.portfolio.domain.models

/** One holding from `GET /user/portfolio/{folio}`. */
data class FolioFundDomain(
    /** The scheme's product id. */
    val id: String,
    /** `mf_holding_id` — what redemption is keyed on. */
    val mfHoldingId: String,
    /** The scheme's ISIN. */
    val schemeId: String,
    val title: String,
    val category: String,
    val amount: Double,
    val currentValue: Double,
    val isSip: Boolean,
    val startDate: String,
    /** Formatted for display, e.g. "0.08%". */
    val returnPercentage: String,
    val `return`: Double,
    /** Formatted for display, e.g. "6.14%". */
    val xirr: String,
    val currentNav: Double,
    val avgNav: Double,
    val folio: String,
    val balanceUnits: Double,
    val imgUrl: String,
    val orderId: String,
    val actualFolio: String,
    /** The purchase plan behind a SIP holding (`mfpp_…`); blank for a lumpsum. */
    val fpId: String = "",
    /** The plan's state, e.g. ACTIVE / CANCELLED; blank when there is no plan. */
    val state: String = ""
)

/**
 * One fund in a lumpsum top-up of existing folios. JN declares it beside
 * `ExistingFundsLumpSumViewModel`, which is parked (composeApp/later/) with the top-up flow; the
 * invest-more use case and mapper still compile against it.
 */
data class LumpSumAdd(
    val prod_Id: String,
    val folio: String,
    val amount: Long
)
