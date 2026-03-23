package org.sharad.velvetinvestment.data.remote.model.mfkyc.init

import kotlinx.serialization.Serializable

@Serializable
data class Data(
    val digilocker_url: String,
    val kyc_access_token: String,
    val kyc_type_id: String,
    val session_id: String,
    val user_id: String
)