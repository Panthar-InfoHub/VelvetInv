package org.sharad.velvetinvestment.data.remote.model.auth.tokens

import kotlinx.serialization.Serializable

@Serializable
data class RefreshTokenDto(
    val `data`: Data,
    val message: String,
    val success: Boolean
)