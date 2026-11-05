package org.sharad.velvetinvestment.features.core.domain.models

/**
 * The three ways a fund or a bundle can be bought, as the API knows them.
 *
 * Velvet only lets the user pick [offered] — a normal (monthly) SIP or a one-time lumpsum. The
 * daily (₹10/day) and micro (₹100/month) starter options are not offered in Velvet; [DAILY] stays
 * so data the server already holds (an existing daily SIP) still reads correctly.
 * [frequency] is what the SIP endpoint expects; it is
 * unused for [ONE_TIME], which goes to the lumpsum endpoint instead.
 */
enum class PurchaseMode(val label: String, val frequency: String) {
    MONTHLY("Monthly", "monthly"),
    ONE_TIME("One-time", "");

    val isSip: Boolean
        get() = this != ONE_TIME

    /** Only a monthly SIP debits on a fixed day of the month. */
    val needsInstallmentDay: Boolean
        get() = this == MONTHLY

    companion object {
        /** The modes Velvet lets a user choose, in display order. */
        val offered: List<PurchaseMode> = listOf(MONTHLY, ONE_TIME)

        /**
         * Recovers a mode from a navigation argument. An unknown name — or one Velvet doesn't
         * offer, such as [DAILY] — falls back to a normal monthly SIP.
         */
        fun fromName(name: String?): PurchaseMode =
            entries.firstOrNull { it.name == name }?.takeIf { it in offered } ?: MONTHLY
    }
}
