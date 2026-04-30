package org.sharad.velvetinvestment.data.remote.model.portfolio

import kotlinx.serialization.Serializable

@Serializable
data class Issuer(
    val display_name: String,
    val logo_url: String
)