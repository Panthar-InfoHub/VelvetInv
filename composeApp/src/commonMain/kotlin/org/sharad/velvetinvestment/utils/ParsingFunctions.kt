package org.sharad.velvetinvestment.utils

fun parseSafeLong(
    input: String,
    previous: Long?
): Long? {
    if (input.isEmpty()) return null

    if (!input.all { it.isDigit() }) return previous

    return try {
        input.toLong()
    } catch (e: NumberFormatException) {
        previous
    }
}
