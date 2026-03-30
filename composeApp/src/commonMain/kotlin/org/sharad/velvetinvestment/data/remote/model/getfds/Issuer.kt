package org.sharad.velvetinvestment.data.remote.model.getfds

import kotlinx.serialization.Serializable

@Serializable
data class Issuer(
    val full_name: String,
    val logo_url: String
)