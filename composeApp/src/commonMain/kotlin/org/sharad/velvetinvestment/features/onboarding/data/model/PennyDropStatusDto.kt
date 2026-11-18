package org.sharad.velvetinvestment.features.onboarding.data.model

import kotlinx.serialization.Serializable
import org.sharad.velvetinvestment.features.onboarding.domain.model.PennyDropStatus

/**
 * The bank verification verdict. `POST /onboarding/penny-drop` (submitting the bank details) and
 * `GET /onboarding/penny-drop/status` (reading it back later) both answer with this body.
 * [PennyDropStatusDataDto.verification_status] decides what happens next; the onboarding record
 * that rides along is not needed here and is left off.
 */
@Serializable
data class PennyDropStatusResponseDto(
    val success: Boolean = false,
    val message: String? = null,
    val data: PennyDropStatusDataDto? = null
)

@Serializable
data class PennyDropStatusDataDto(
    val verification_status: String? = null,
    val is_processing_complete: Boolean = false,
    val is_manual_verification: Boolean = false,
    /** Turnaround time for a manual review, e.g. `"24 hours"`; null once verified. */
    val tat: String? = null,
    val bank_accounts: List<BankAccountStatusDto> = emptyList()
)

@Serializable
data class BankAccountStatusDto(
    val code: String? = null,
    val reason: String? = null,
    val status: String? = null,
    val value: BankAccountValueDto? = null
)

@Serializable
data class BankAccountValueDto(
    val account_number: String? = null,
    val ifsc_code: String? = null,
    val account_type: String? = null
)

fun PennyDropStatusResponseDto.toDomain(): PennyDropStatus = PennyDropStatus(
    verificationStatus = data?.verification_status,
    message = message?.takeIf { it.isNotBlank() },
    tat = data?.tat?.takeIf { it.isNotBlank() },
    reason = data?.bank_accounts?.firstNotNullOfOrNull { account ->
        account.reason?.takeIf { it.isNotBlank() }
    }
)
