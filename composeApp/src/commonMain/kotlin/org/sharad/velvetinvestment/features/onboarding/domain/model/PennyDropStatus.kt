package org.sharad.velvetinvestment.features.onboarding.domain.model

/**
 * Where the bank verification stands, as returned by both the bank details submission and the
 * penny drop status read.
 * - [isVerified]: the account is verified and onboarding moves on.
 * - [isInProgress]: the account is under (usually manual) review for [tat]; the user waits on home.
 * - anything else: the verification did not go through; [failureMessage] says why.
 */
data class PennyDropStatus(
    val verificationStatus: String?,
    /** The server's `message`, e.g. "Bank details verified successfully". */
    val message: String?,
    /** Turnaround time for the review, e.g. "24 hours". */
    val tat: String?,
    /** The bank's reason on the account, present when it was rejected. */
    val reason: String?
) {
    val isVerified: Boolean
        get() = verificationStatus.equals(VERIFIED, ignoreCase = true)

    val isInProgress: Boolean
        get() = verificationStatus.equals(IN_PROGRESS, ignoreCase = true)

    val failureMessage: String
        get() = reason ?: message ?: DEFAULT_FAILURE_MESSAGE

    /** The pending notice, e.g. "...will be updated within 24 hours." */
    val pendingMessage: String
        get() = "Your bank account verification is pending and will be updated " +
                (tat?.let { "within $it." } ?: "soon.")

    private companion object {
        const val VERIFIED = "VERIFIED"
        const val IN_PROGRESS = "IN_PROGRESS"
        const val DEFAULT_FAILURE_MESSAGE =
            "We could not verify this bank account. Please check the details and try again."
    }
}
