package org.sharad.velvetinvestment.data.remote.model.fddetails

import kotlinx.serialization.Serializable

@Serializable
data class FDDetailsDto(
    val `data`: Data,
    val message: String,
    val success: Boolean
)