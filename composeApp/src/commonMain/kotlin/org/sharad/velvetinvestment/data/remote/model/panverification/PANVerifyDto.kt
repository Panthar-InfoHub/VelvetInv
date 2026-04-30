package org.sharad.velvetinvestment.data.remote.model.panverification

import kotlinx.serialization.Serializable

@Serializable
data class PANVerifyDto(
    val `data`: Data,
    val message: String,
    val success: Boolean
)