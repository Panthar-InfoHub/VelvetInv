package org.sharad.velvetinvestment.data.remote.model.auth.tokens

import kotlinx.serialization.Serializable

@Serializable
data class RefreshTokenBody(
    val token: String
)