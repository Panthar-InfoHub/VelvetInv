package org.sharad.velvetinvestment.data.remote.model.panverification

import kotlinx.serialization.Serializable

@Serializable
data class Data(
    val app_verified: Boolean,
    val full_name: String,
    val pan_verified: Boolean
)