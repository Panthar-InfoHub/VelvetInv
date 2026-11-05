package org.sharad.velvetinvestment.features.cart.domain.models

data class MutualFundPurchaseInitiateDomain(
    val mandateId:String,
    val url: String,
    val status: MandateStatus
)

enum class MandateStatus{
    PENDING,APPROVED
}