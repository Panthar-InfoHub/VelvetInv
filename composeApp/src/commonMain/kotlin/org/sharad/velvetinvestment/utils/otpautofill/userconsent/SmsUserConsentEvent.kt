package org.sharad.velvetinvestment.utils.otpautofill.userconsent

sealed interface SmsUserConsentEvent {

    data object ListeningStarted : SmsUserConsentEvent

    /** The user tapped Allow on the consent dialog. */
    data class SmsReceived(val message: String) : SmsUserConsentEvent

    /** The user tapped Deny, or dismissed the consent dialog. */
    data object ConsentDenied : SmsUserConsentEvent

    data object Timeout : SmsUserConsentEvent

    data class Error(val throwable: Throwable) : SmsUserConsentEvent
}
