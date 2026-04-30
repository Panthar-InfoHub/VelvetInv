package org.sharad.velvetinvestment.utils.tradingaccount

enum class Holding(
    val heading: String = "",
    val subHeading: String = "",
    val code: String
) {
    SINGLE(heading = "Single Holding", subHeading = "One account holder - you alone", code = "SI"),
    JOINT(
        heading = "Joint Holding",
        subHeading = "Multiple account holders (additional details required)",
        code = "JO"
    )
}