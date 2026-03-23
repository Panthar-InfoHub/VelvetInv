package org.sharad.velvetinvestment.data.remote.model.mfkyc.init

import kotlinx.serialization.Serializable

@Serializable
data class InitiateKycDto(
    val `data`: Data,
    val message: String,
    val success: Boolean
)