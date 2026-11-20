package org.sharad.velvetinvestment.utils.otpautofill

import org.koin.dsl.module
import org.sharad.velvetinvestment.utils.otpautofill.userconsent.SmsUserConsentOtpAutofill

/** Needs [userconsent.SmsUserConsent] bound by the platform module. */
val smsModule = module {
    single<OtpParser> { RegexOtpParser() }

    factory<OtpAutofill> {
        SmsUserConsentOtpAutofill(smsUserConsent = get(), otpParser = get())
    }
}
