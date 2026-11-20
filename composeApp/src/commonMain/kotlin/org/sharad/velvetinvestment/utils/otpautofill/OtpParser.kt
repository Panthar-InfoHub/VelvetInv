package org.sharad.velvetinvestment.utils.otpautofill

interface OtpParser {
    /** The first standalone run of exactly [otpLength] digits in [message], or `null`. */
    fun extractOtp(message: String, otpLength: Int): String?
}

/**
 * Matches on length because flows differ (login is four digits, purchases and redemptions six),
 * and a fixed length would otherwise pick up an amount such as "5000" from a six-digit OTP SMS.
 */
class RegexOtpParser : OtpParser {

    override fun extractOtp(message: String, otpLength: Int): String? {
        return Regex("""\b\d{$otpLength}\b""").find(message)?.value
    }
}
