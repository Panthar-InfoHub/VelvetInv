package org.sharad.velvetinvestment.data.remote.mapper

import org.sharad.velvetinvestment.data.remote.model.usercart.LumpSumItem
import org.sharad.velvetinvestment.data.remote.model.usercart.SipItem
import org.sharad.velvetinvestment.data.remote.model.usercart.UserCartDto
import org.sharad.velvetinvestment.domain.models.usercart.CartItemDomain
import org.sharad.velvetinvestment.domain.models.usercart.CartType
import org.sharad.velvetinvestment.domain.models.usercart.SipDetails
import org.sharad.velvetinvestment.domain.models.usercart.UserCartDomain

fun UserCartDto.toDomain(): UserCartDomain {
    return UserCartDomain(
        sipItems = data.sip_items.map { it.toSipDomain() },
        lumpSumItems = data.lump_sum_items.map { it.toLumpSumDomain() }
    )
}

fun SipItem.toSipDomain(): CartItemDomain {
    return CartItemDomain(
        id = id,
        amcName = amc_name,
        productName = prod_name,
        amount = sip_amt.toLongOrNull() ?: 0,
        type = CartType.SIP,
        date = sip_st_date,
        sipDetails = SipDetails(
            startDate = sip_st_date,
            endDate = sip_en_date,
            frequency = sip_freq,
            day = sip_day.toIntOrNull() ?: 0,
            sipAmount = sip_amt.toLongOrNull() ?: 0
        ),
        imageUrl = img_url?:""
    )
}

fun LumpSumItem.toLumpSumDomain(): CartItemDomain {
    return CartItemDomain(
        id = id,
        amcName = amc_name,
        productName = prod_name,
        amount = txn_amount.toLongOrNull() ?: 0,
        type = CartType.LUMPSUM,
        date = adddate,
        sipDetails = null,
        imageUrl = img_url?:""

    )
}