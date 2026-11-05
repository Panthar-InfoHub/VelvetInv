package org.sharad.velvetinvestment.features.portfolio.data.model.cancelorder

import kotlinx.serialization.Serializable

@Serializable
data class CancelOrderRequestDto(
    val order_no: String
)
