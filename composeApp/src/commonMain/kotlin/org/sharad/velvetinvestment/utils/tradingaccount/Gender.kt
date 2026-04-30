package org.sharad.velvetinvestment.utils.tradingaccount

enum class Gender(
    val code: String,
    val displayLabel: String
) {
    MALE(
        code = "M",
        displayLabel = "Male"
    ),
    FEMALE(
        code = "F",
        displayLabel = "Female"
    ),
    OTHER(
        code = "O",
        displayLabel = "Other"
    ),
    TRANSGENDER(
        code = "T",
        displayLabel = "Transgender"
    );

    companion object {
        fun fromCode(code: String?): Gender? {
            return entries.firstOrNull { it.code == code }
        }
    }
}