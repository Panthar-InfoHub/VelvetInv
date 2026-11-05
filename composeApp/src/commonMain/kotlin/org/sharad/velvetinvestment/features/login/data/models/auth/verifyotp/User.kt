package org.sharad.velvetinvestment.features.login.data.models.auth.verifyotp

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val user_id: String,
    val phone_no: String,
    val mpin_enabled: Boolean = false,
    val mpin_is_setup: Boolean = false
)
