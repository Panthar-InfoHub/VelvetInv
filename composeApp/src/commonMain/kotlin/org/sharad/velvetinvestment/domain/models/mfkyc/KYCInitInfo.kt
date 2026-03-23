package org.sharad.velvetinvestment.domain.models.mfkyc

import kotlinx.serialization.Serializable

data class KYCInitInfo(
    val digilocker_url: String,
    val kyc_access_token: String,
    val kyc_type_id: String,
    val session_id: String,
    val user_id: String
)
