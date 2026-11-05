package org.sharad.velvetinvestment.features.core.ui.otp

data class OtpUiState(
    val otpValue: String = "",
    val otpLength: Int = DEFAULT_OTP_LENGTH,
    val resendTimerSeconds: Int = 0,
    val isLoading: Boolean = false
) {
    val isComplete: Boolean
        get() = otpValue.length == otpLength

    val isSubmitEnabled: Boolean
        get() = isComplete && !isLoading

    val canResend: Boolean
        get() = resendTimerSeconds == 0 && !isLoading

    companion object {
        const val DEFAULT_OTP_LENGTH = 4
    }
}
