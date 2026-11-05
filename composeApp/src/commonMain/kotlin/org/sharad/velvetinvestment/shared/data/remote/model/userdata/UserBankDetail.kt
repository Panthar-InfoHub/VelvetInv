package org.sharad.velvetinvestment.shared.data.remote.model.userdata

import kotlinx.serialization.Serializable

/**
 * One row of `GET /user/`'s `user_bank_details`. The account-number hash and the provider's raw
 * verification response are not modelled; unknown keys are ignored by the shared Json config.
 */
@Serializable
data class UserBankDetail(
    val id: String,
    val user_id: String? = null,
    val account_no: String? = null,
    val ifsc_code: String? = null,
    val bank_name: String? = null,
    /** `savings` / `current`, lower-case as the server sends it. */
    val account_type: String? = null,
    val account_holder_name: String? = null,
    val fp_bank_account_id: String? = null,
    val fp_bank_account_old_id: Int? = null,
    val is_primary: Boolean = false,
    /** `VERIFIED` once the penny drop (or pre-verification) succeeds. */
    val verification_status: String? = null,
    val verification_method: String? = null,
    val verified_at: String? = null,
    val provider_reference_id: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null
)
