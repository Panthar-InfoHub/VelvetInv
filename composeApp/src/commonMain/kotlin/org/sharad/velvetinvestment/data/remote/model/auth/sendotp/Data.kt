package org.sharad.velvetinvestment.data.remote.model.auth.sendotp

import kotlinx.serialization.Serializable

@Serializable
data class Data(
    val phone_no: String,
    val user_id: String
)