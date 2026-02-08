package org.sharad.velvetinvestment.utils

import kotlin.math.abs
import kotlin.math.round


fun formatMoneyWithUnits(amount: Long?): String {
    if (amount == null || amount == 0L) return "0"

    val absAmount = abs(amount)

    val (value, suffix) = when {
        absAmount >= 10_000_000 -> amount / 10_000_000.0 to "Cr"
        absAmount >= 100_000 -> amount / 100_000.0 to "L"
        absAmount >= 1_000 -> amount / 1_000.0 to "K"
        else -> return amount.toString()
    }

    val rounded = round(value * 100) / 100

    val text = rounded.toString()
        .removeSuffix(".0")
        .removeSuffix(".00")

    return "$text$suffix"
}
