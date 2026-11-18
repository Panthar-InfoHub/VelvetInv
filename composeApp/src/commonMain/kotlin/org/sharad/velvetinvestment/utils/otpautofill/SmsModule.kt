package org.sharad.velvetinvestment.utils.otpautofill

import org.koin.dsl.module
import org.sharad.velvetinvestment.features.core.ui.otp.OtpUiState
import org.sharad.velvetinvestment.utils.otpautofill.userconsent.SmsUserConsentOtpAutofill

/** Needs [userconsent.SmsUserConsent] bound by the platform module. */
val smsModule = module {
    single<OtpParser> {
        // Matches the login OTP field, which takes [OtpUiState.DEFAULT_OTP_LENGTH] digits.
        RegexOtpParser(Regex("""\b\d{${OtpUiState.DEFAULT_OTP_LENGTH}}\b"""))
    }

    factory<OtpAutofill> {
        SmsUserConsentOtpAutofill(smsUserConsent = get(), otpParser = get())
    }
}
