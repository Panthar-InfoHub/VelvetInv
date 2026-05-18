package org.sharad.velvetinvestment.data.remote.model.mfpurchasemandatestatus

import kotlinx.serialization.Serializable

@Serializable
data class CheckMFPurchaseMandateStatusDto(
    val `data`: Data,
    val message: String,
    val success: Boolean
)