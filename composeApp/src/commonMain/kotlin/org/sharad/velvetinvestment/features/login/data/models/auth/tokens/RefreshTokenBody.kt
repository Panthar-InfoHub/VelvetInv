package org.sharad.velvetinvestment.features.login.data.models.auth.tokens

import kotlinx.serialization.Serializable

@Serializable
data class RefreshTokenBody(
    val token: String
)