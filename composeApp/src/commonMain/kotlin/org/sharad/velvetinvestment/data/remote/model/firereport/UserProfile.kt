package org.sharad.velvetinvestment.data.remote.model.firereport

import kotlinx.serialization.Serializable

@Serializable
data class UserProfile(
    val age: Int,
    val city: String,
    val name: String
)