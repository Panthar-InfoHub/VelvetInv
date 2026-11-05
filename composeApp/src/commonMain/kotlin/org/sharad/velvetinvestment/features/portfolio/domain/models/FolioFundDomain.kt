package org.sharad.velvetinvestment.features.portfolio.domain.models

data class FolioFundDomain(
    val id: String,
    val schemeId: Int,
    val title: String,
    val category: String,
    val amount: Long,
    val isSip: Boolean,
    val startDate: String,
    val returnPercentage: String,
    val `return`: Double,
    val xirr: String,
    val currentNav: Double,
    val avgNav: Double,
    val folio: String,
    val balanceUnits: Double,
    val imgUrl: String,
    val orderId: String,
    val actualFolio: String
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
