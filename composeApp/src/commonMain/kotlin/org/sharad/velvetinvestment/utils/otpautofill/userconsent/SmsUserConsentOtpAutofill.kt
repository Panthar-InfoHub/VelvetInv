package org.sharad.velvetinvestment.utils.otpautofill.userconsent

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.transformWhile
import org.sharad.velvetinvestment.utils.otpautofill.OtpAutofill
import org.sharad.velvetinvestment.utils.otpautofill.OtpParser

class SmsUserConsentOtpAutofill(
    private val smsUserConsent: SmsUserConsent,
    private val otpParser: OtpParser
) : OtpAutofill {

    override fun observeOtp(): Flow<String> =
        smsUserConsent.startListening()
            .transformWhile { event ->
                when (event) {
                    is SmsUserConsentEvent.Error -> throw event.throwable
                    SmsUserConsentEvent.ListeningStarted -> true
                    is SmsUserConsentEvent.SmsReceived -> {
                        emit(event.message)
                        false
                    }
                    SmsUserConsentEvent.ConsentDenied,
                    SmsUserConsentEvent.Timeout -> false
                }
            }
            .mapNotNull { message -> otpParser.extractOtp(message) }
}
