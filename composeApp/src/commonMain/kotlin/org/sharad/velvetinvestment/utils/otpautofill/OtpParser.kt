package org.sharad.velvetinvestment.utils.otpautofill

interface OtpParser {
    fun extractOtp(message: String): String?
}

class RegexOtpParser(
    private val regex: Regex = Regex("""\b\d{6}\b""")
) : OtpParser {

    override fun extractOtp(message: String): String? {
        return regex.find(message)?.value
    }
}