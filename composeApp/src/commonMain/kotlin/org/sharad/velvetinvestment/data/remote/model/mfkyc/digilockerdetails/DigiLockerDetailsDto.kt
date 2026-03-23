package org.sharad.velvetinvestment.data.remote.model.mfkyc.digilockerdetails

import kotlinx.serialization.Serializable

@Serializable
data class DigiLockerDetailsDto(
    val `data`: Data,
    val message: String,
    val success: Boolean
)