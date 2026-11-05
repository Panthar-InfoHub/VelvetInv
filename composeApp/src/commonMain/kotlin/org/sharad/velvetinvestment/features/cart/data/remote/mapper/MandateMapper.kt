package org.sharad.velvetinvestment.features.cart.data.remote.mapper

import org.sharad.velvetinvestment.features.cart.data.remote.model.initiatemfpurchase.Data
import org.sharad.velvetinvestment.features.cart.domain.models.MandateStatus
import org.sharad.velvetinvestment.features.cart.domain.models.MutualFundPurchaseInitiateDomain

fun Data.toDomain(): MutualFundPurchaseInitiateDomain {
    return MutualFundPurchaseInitiateDomain(
        mandateId = mandate_id,
        url = mandate_short_url,
        status = if (status=="MANDATE_APPROVED") MandateStatus.APPROVED else MandateStatus.PENDING,
    )
}
