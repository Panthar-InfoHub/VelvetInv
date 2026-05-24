package org.sharad.velvetinvestment.utils

val EMAIL_REGEX=Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

fun isValidEmail(email: String): Boolean {
    return EMAIL_REGEX.matches(email)
}

fun String.isValidPhoneNumberInput(): Boolean {
    return all { it.isDigit() } && length <= 10
}

private val keepUppercase = setOf(
    "ETF", "ELSS", "SIP", "STP", "SWP",
    "NFO", "FOF", "LIC", "ITI", "UTI",
    "DSP", "HDFC", "HSBC", "JM", "SBI",
    "MF", "S&P", "BSE", "NSE", "ICICI"
)

fun String.toTitleCase(): String {
    return lowercase()
        .replace(Regex("(^|[\\s\\-_.\\/]+)([a-z])")) {
            it.groupValues[1] + it.groupValues[2].uppercase()
        }
        .split(" ")
        .joinToString(" ") { word ->
            val normalized = word.uppercase()
            if (normalized in keepUppercase) {
                normalized
            } else {
                word
            }
        }
}