package org.sharad.velvetinvestment.data.remote.model.fdredirect

import kotlinx.serialization.Serializable

@Serializable
data class Data(
    val `data`: DataX,
    val success: Boolean
)