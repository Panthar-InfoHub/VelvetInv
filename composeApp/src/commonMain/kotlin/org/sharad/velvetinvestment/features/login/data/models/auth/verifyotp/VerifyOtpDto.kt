package org.sharad.velvetinvestment.features.login.data.models.auth.verifyotp

import kotlinx.serialization.Serializable

@Serializable
data class VerifyOtpDto(
    val `data`: Data,
    val message: String,
    val success: Boolean
)