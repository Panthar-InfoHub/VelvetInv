package org.sharad.velvetinvestment.features.insurance.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class RequestConnectionDto(
    val type: String,
    val message: String = ""
)
