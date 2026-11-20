package org.sharad.velvetinvestment.utils.otpautofill.userconsent

import kotlinx.coroutines.flow.Flow

/**
 * Google's SMS User Consent API: the SMS does not need the app hash. When a one-time-code SMS arrives the system shows a dialog,
 * and the message is handed to the app only if the user taps Allow.
 *
 * Listening is one-shot: the flow completes after one SMS is accepted or denied, after the
 * 5-minute timeout, or on error. Cancelling collection stops the listener.
 */
interface SmsUserConsent {

    /**
     * @param senderPhoneNumber only accept SMS from this sender, or `null` to accept any sender
     * not in the user's contacts.
     */
    fun startListening(senderPhoneNumber: String? = null): Flow<SmsUserConsentEvent>
}
