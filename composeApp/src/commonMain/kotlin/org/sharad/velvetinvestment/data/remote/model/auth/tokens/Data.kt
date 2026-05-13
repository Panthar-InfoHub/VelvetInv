package org.sharad.velvetinvestment.data.remote.model.auth.tokens

import kotlinx.serialization.Serializable

@Serializable
data class Data(
    val refresh_token: String,
    val token: String
)