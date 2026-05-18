package org.sharad.velvetinvestment.data.remote.model.mfpurchasemandatestatus

import kotlinx.serialization.Serializable

@Serializable
data class PurchaseBodyDto(
    val mandate_id: String
)