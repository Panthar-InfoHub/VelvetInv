package org.sharad.velvetinvestment.data.remote.model.auth.sendotp

import kotlinx.serialization.Serializable

@Serializable
data class SendOtpDto(
    val `data`: Data,
    val message: String,
    val success: Boolean
)