package org.sharad.velvetinvestment.data.remote.model.ServerError

import kotlinx.serialization.Serializable

@Serializable
data class Error(
    val message: String,
    val type: String
)