package org.sharad.velvetinvestment.data.remote.model.firereport

import kotlinx.serialization.Serializable

@Serializable
data class UserProfile(
    val name: String = "",
    val age: Int? = null,
    /** Null until the user has a city on file. */
    val city: String? = null
)
