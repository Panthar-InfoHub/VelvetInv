package org.sharad.velvetinvestment.presentation.portfolio.viewmodel

/**
 * One fund in a lumpsum top-up of existing folios. Lifted out of `ExistingFundsLumpSumViewModel`
 * (parked in `composeApp/later/` with the top-up flow) because the invest-more use case and mapper
 * still compile against it.
 */
data class LumpSumAdd(
    val prod_Id: String,
    val folio: String,
    val amount:Long
)
