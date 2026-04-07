package org.sharad.velvetinvestment.data.remote.model.cartaddsip

import kotlinx.serialization.Serializable

@Serializable
data class AddCartSipResponseDto(
    val `data`: Data,
    val message: String,
    val success: Boolean
)