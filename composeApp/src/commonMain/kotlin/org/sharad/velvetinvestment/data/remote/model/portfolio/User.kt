package org.sharad.velvetinvestment.data.remote.model.portfolio

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val email: String,
    val full_name: String,
    val id: String
)