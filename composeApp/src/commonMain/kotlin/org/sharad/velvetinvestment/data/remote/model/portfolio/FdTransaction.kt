package org.sharad.velvetinvestment.data.remote.model.portfolio

import kotlinx.serialization.Serializable

@Serializable
data class FdTransaction(
    val amount: String,
    val fd_issued_at: String?,
    val id: String,
    val maturity_amount: String?,
    val product: Product,
    val roi_at_booking: String,
    val status: String,
    val tenure_at_booking: Int,
    val user: User
)