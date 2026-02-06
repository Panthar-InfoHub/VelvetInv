package org.sharad.velvetinvestment.utils


fun formatMoneyWithK(amount: Long?): String {
    if (amount == null) return "0"

    if (amount < 1_000) return amount.toString()

    val value = amount / 1_000.0
    val text = value.toString()

    return if (text.endsWith(".0")) {
        "${text.dropLast(2)}K"
    } else {
        "${text}K"
    }
}
