package org.sharad.velvetinvestment.domain.models.portfolio

data class PendingOrderDomain(
    val id: String,
    val type: String,
    val schemeName: String,
    val amount: Double,
    val date: String,
    val status: String,
    val statusRemark: String,
    val amc: String,
    val frequency: String,
    val startDate: String
)