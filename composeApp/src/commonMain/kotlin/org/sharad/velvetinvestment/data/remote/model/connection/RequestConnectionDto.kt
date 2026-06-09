package org.sharad.velvetinvestment.data.remote.model.connection

import kotlinx.serialization.Serializable

@Serializable
data class RequestConnectionDto(
    val type: String,
    val message: String = ""
)
