package org.sharad.velvetinvestment.domain.models.fixeddeposits

data class TenureDuration(
    val years: Int = 0,
    val months: Int = 0,
    val days: Int = 0
) {
    val totalDays: Int
        get() = years * 365 + months * 30 + days

    val totalMonths: Int
        get() = years * 12 + months

    override fun toString(): String {
        return buildString {
            if (years > 0) append("${years}Y ")
            if (months > 0) append("${months}M ")
            if (days > 0) append("${days}D")
        }.trim()
    }
}