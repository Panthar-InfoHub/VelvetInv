package org.sharad.velvetinvestment.features.onboarding.ui

object OnboardingInput {

    const val PAN_LENGTH = 10
    const val PINCODE_LENGTH = 6
    const val PHONE_LENGTH = 10
    const val AADHAAR_SUFFIX_LENGTH = 4
    const val ANNUAL_INCOME_MAX_DIGITS = 12

    private val PAN_REGEX = Regex("^[A-Z]{5}[0-9]{4}[A-Z]$")
    private val EMAIL_REGEX = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    private val ISO_DATE_REGEX = Regex("^\\d{4}-\\d{2}-\\d{2}$")

    fun sanitizePan(input: String): String =
        input.filter { it.isLetterOrDigit() }.uppercase().take(PAN_LENGTH)

    fun sanitizeDocumentNumber(input: String): String =
        input.filter { it.isLetterOrDigit() }.uppercase().take(20)

    fun sanitizeName(input: String): String =
        input.filter { it.isLetter() || it == ' ' || it == '.' || it == '\'' || it == '-' }
            .trimStart()
            .take(60)

    fun sanitizeDigits(input: String, maxLength: Int): String =
        input.filter { it.isDigit() }.take(maxLength)

    fun sanitizePercentage(input: String): String {
        val digits = input.filter { it.isDigit() }.take(3)
        val value = digits.toIntOrNull() ?: return ""
        return if (value > 100) digits.dropLast(1) else digits
    }

    fun sanitizeEmail(input: String): String =
        input.filterNot { it.isWhitespace() }.lowercase().take(80)

    fun sanitizeText(input: String, maxLength: Int = 120): String =
        input.trimStart().take(maxLength)

    fun isValidPan(value: String): Boolean = PAN_REGEX.matches(value)

    fun isValidEmail(value: String): Boolean = EMAIL_REGEX.matches(value)

    fun isValidPincode(value: String): Boolean =
        value.length == PINCODE_LENGTH && value.all { it.isDigit() }

    fun isValidPhone(value: String): Boolean =
        value.length == PHONE_LENGTH && value.all { it.isDigit() } && value.first() in '6'..'9'

    fun isValidIsoDate(value: String): Boolean = ISO_DATE_REGEX.matches(value)

    fun isFilled(value: String): Boolean = value.isNotBlank()
}
