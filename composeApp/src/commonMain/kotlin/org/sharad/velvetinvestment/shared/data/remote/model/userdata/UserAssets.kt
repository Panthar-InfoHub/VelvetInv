package org.sharad.velvetinvestment.shared.data.remote.model.userdata

import kotlinx.serialization.Serializable

/** `user_assets` in `GET /user/`: the FIRE questionnaire's assets step, amounts as strings. */
@Serializable
data class UserAssets(
    val id: String? = null,
    val user_id: String? = null,
    val stocks: String? = null,
    val fd: String? = null,
    val real_estate: String? = null,
    val gold: String? = null,
    val cash_saving: String? = null,
    val mutual_funds: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null
)
