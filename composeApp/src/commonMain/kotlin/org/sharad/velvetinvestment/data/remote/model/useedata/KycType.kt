package org.sharad.velvetinvestment.data.remote.model.useedata

import kotlinx.serialization.Serializable

@Serializable
data class KycType(
    val kyc_type: String,
    val status: String
)