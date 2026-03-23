package org.sharad.velvetinvestment.data.remote.model.onboarding

import kotlinx.serialization.Serializable

@Serializable
data class Profile(
    val city: String,
    val dob: String,
    val full_name: String,
    val email: String
)