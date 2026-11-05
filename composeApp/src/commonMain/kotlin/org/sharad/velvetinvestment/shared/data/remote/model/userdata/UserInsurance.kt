package org.sharad.velvetinvestment.shared.data.remote.model.userdata

import kotlinx.serialization.Serializable

/** `user_insurance` in `GET /user/`: cover the user already holds, amounts as strings. */
@Serializable
data class UserInsurance(
    val id: String? = null,
    val user_id: String? = null,
    val life_insurance: String? = null,
    val health_insurance: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null
)
