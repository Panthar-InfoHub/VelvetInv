package org.sharad.velvetinvestment.features.onboarding.domain.model

data class PANVerificationError(
    val code: Int,
    val message: String,
    val type: String
) {
    /**
     * The error types the app acts on: the user has no usable KYC ([KYC_NEW_REQUIRED]) or their
     * existing KYC has to be modified ([KYC_MODIFY_REQUIRED]). Both are sent through the KYC
     * initiate flow; every other type is just surfaced as a message.
     */
    val isKycInitiateRequired: Boolean
        get() = type == KYC_NEW_REQUIRED || type == KYC_MODIFY_REQUIRED

    companion object {
        const val KYC_NEW_REQUIRED = "READINESS_KYC_NEW_REQUIRED"
        const val KYC_MODIFY_REQUIRED = "READINESS_KYC_MODIFY_REQUIRED"
        const val UNKNOWN = "UNKNOWN"
    }
}
data class KYCError(
    val code: Int,
    val message: String,
    val type: String
) {
    /**
     * The only error type the app can act on: the user has no usable KYC and must be sent
     * through the KYC initiate flow. Every other type is just surfaced as a message.
     */
    val isNewKycRequired: Boolean
        get() = type == KYC_FORM_NOT_FOUND

    /**
     * The server answered with an error status. `false` when no response arrived at all (timeout,
     * no internet: `code == -1`) or when a 2xx body could not be read.
     */
    val isServerError: Boolean
        get() = code >= 400

    companion object {
        const val  KYC_FORM_NOT_FOUND = "KYC_FORM_NOT_FOUND"
        const val UNKNOWN = "UNKNOWN"
    }
}
