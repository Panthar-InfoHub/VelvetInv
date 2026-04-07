package org.sharad.velvetinvestment.data.remote.model.mfkyc

import kotlinx.serialization.Serializable

@Serializable
data class ESignDto(
    val `data`: String,
    val message: String,
    val success: Boolean
)