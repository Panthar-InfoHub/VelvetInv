package org.sharad.velvetinvestment.data.remote.model.useedata

import kotlinx.serialization.Serializable

@Serializable
data class UserInsurance(
    val createdAt: String,
    val health_insurance: String,
    val id: String,
    val life_insurance: String,
    val updatedAt: String,
    val user_id: String
)