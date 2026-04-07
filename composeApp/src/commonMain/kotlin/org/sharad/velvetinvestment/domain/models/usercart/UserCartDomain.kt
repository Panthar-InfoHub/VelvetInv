package org.sharad.velvetinvestment.domain.models.usercart

data class UserCartDomain(
    val sipItems: List<CartItemDomain>,
    val lumpSumItems: List<CartItemDomain>
)

data class CartItemDomain(
    val id: String,
    val amcName: String,
    val productName: String,
    val amount: Long,
    val type: CartType,
    val date: String,
    val sipDetails: SipDetails? = null
)

enum class CartType {
    SIP,
    LUMPSUM
}

data class SipDetails(
    val startDate: String,
    val endDate: String,
    val frequency: String,
    val day: Int,
    val sipAmount: Long
)