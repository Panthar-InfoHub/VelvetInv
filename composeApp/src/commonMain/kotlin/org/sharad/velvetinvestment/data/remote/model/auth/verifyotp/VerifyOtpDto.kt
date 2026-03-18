package org.sharad.velvetinvestment.data.remote.model.auth.verifyotp

import kotlinx.serialization.Serializable

@Serializable
data class VerifyOtpDto(
    val `data`: Data,
    val message: String,
    val success: Boolean
)