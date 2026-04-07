package org.sharad.velvetinvestment.data.remote.model.cartaddlumpsum

import kotlinx.serialization.Serializable

@Serializable
data class AddCartLumpSumResponseDto(
    val `data`: Data,
    val message: String,
    val success: Boolean
)