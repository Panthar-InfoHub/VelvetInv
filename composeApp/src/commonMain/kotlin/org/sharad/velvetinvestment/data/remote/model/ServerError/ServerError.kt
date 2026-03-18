package org.sharad.velvetinvestment.data.remote.model.ServerError

import kotlinx.serialization.Serializable

@Serializable
data class ServerError(
    val error: Error,
    val success: Boolean
)