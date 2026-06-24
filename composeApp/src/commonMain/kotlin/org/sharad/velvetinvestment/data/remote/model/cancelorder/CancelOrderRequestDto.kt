package org.sharad.velvetinvestment.data.remote.model.cancelorder

import kotlinx.serialization.Serializable

@Serializable
data class CancelOrderRequestDto(
    val order_no: String
)
