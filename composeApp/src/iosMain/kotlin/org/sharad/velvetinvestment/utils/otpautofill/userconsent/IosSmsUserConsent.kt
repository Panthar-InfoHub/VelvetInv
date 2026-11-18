package org.sharad.velvetinvestment.utils.otpautofill.userconsent

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.sharad.velvetinvestment.utils.otpautofill.userconsent.SmsUserConsent
import org.sharad.velvetinvestment.utils.otpautofill.userconsent.SmsUserConsentEvent

/** iOS fills OTPs from the keyboard (`.oneTimeCode`); there is no consent API to call. */
class IosSmsUserConsent : SmsUserConsent {

    override fun startListening(senderPhoneNumber: String?): Flow<SmsUserConsentEvent> =
        flowOf(
            SmsUserConsentEvent.Error(
                UnsupportedOperationException("SMS User Consent is not supported on iOS")
            )
        )
}
