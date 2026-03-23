package org.sharad.velvetinvestment.data.remote.model.useedata

import kotlinx.serialization.Serializable

@Serializable
data class UserDataDto(
    val code: Int,
    val `data`: Data,
    val message: String
)