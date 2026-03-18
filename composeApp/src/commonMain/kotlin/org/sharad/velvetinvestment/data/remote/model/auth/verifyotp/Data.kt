package org.sharad.velvetinvestment.data.remote.model.auth.verifyotp

import kotlinx.serialization.Serializable

@Serializable
data class Data(
    val refresh_token: String,
    val token: String,
    val user: User
)