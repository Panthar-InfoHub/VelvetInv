package org.sharad.velvetinvestment.data.remote.model.auth.verifyotp

import kotlinx.serialization.Serializable

@Serializable
data class VerifyOtpBodyDto(
    val mob: String,
    val otp: String
)