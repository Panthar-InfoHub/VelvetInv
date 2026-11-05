package org.sharad.velvetinvestment.shared.data.remote.model.userdata

import kotlinx.serialization.Serializable

@Serializable
data class UserDataDto(
    val code: Int,
    val `data`: Data,
    val message: String
)