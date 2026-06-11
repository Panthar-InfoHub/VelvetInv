package org.sharad.velvetinvestment.domain.models.mutualfunds

data class MutualFundPurchaseInitiateDomain(
    val mandateId:String,
    val url: String,
    val status: MandateStatus
)

enum class MandateStatus{
    PENDING,APPROVED
}