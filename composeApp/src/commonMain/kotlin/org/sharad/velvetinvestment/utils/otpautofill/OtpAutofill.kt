package org.sharad.velvetinvestment.utils.otpautofill

import kotlinx.coroutines.flow.Flow

/**
 * What an OTP screen depends on to have its code filled in from an SMS.
 *
 * Autofill is best effort: the flow may complete without emitting (timeout, user denied) or throw
 * (API unavailable, e.g. on iOS). Callers should catch and let the user type the code.
 */
interface OtpAutofill {
    /** @param otpLength how many digits the code has, so the right number is picked from the SMS. */
    fun observeOtp(otpLength: Int): Flow<String>
}
