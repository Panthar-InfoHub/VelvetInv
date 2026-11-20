package org.sharad.velvetinvestment.features.core.ui.otp

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.sharad.velvetinvestment.utils.otpautofill.OtpAutofill

class OtpController(
    private val scope: CoroutineScope,
    otpLength: Int = OtpUiState.DEFAULT_OTP_LENGTH,
    private val resendCooldownSeconds: Int = DEFAULT_RESEND_COOLDOWN_SECONDS,
    startTimerImmediately: Boolean = true
) {
    private val _state = MutableStateFlow(OtpUiState(otpLength = otpLength))
    val state: StateFlow<OtpUiState> = _state.asStateFlow()

    private var timerJob: Job? = null

    private var autofillJob: Job? = null

    init {
        if (startTimerImmediately) startResendTimer()
    }

    fun onOtpChange(value: String) {
        if (!value.all { it.isDigit() }) return
        _state.update { it.copy(otpValue = value.take(it.otpLength)) }
    }

    fun clearOtp() {
        _state.update { it.copy(otpValue = "") }
    }

    fun setLoading(loading: Boolean) {
        _state.update { it.copy(isLoading = loading) }
    }

    fun startResendTimer() {
        timerJob?.cancel()
        timerJob = scope.launch {
            for (second in resendCooldownSeconds downTo 0) {
                _state.update { it.copy(resendTimerSeconds = second) }
                if (second > 0) delay(1000)
            }
        }
    }

    /**
     * Fills the field from the OTP SMS and then calls [onOtpFilled], which every flow uses to submit
     * the code. Listening is one-shot, so call this again *before* each resend request; any earlier
     * listener is replaced. Autofill is best effort: if it fails the user simply types the code.
     *
     * Listening ends with [scope]. A ViewModel that outlives its OTP screen must also call
     * [stopSmsAutofill] when the user leaves, or a late SMS would submit a code nobody is waiting on.
     */
    fun startSmsAutofill(otpAutofill: OtpAutofill, onOtpFilled: () -> Unit) {
        autofillJob?.cancel()
        autofillJob = scope.launch {
            otpAutofill.observeOtp(_state.value.otpLength)
                .catch { /* Autofill is best effort; manual entry still works. */ }
                .collect { code ->
                    onOtpChange(code)
                    onOtpFilled()
                }
        }
    }

    fun stopSmsAutofill() {
        autofillJob?.cancel()
        autofillJob = null
    }

    suspend fun <T> withLoading(block: suspend () -> T): T {
        setLoading(true)
        return try {
            block()
        } finally {
            setLoading(false)
        }
    }

    companion object {
        const val DEFAULT_RESEND_COOLDOWN_SECONDS = 30
    }
}
