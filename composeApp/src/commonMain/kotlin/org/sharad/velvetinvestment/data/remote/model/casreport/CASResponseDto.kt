package org.sharad.velvetinvestment.data.remote.model.casreport

import kotlinx.serialization.Serializable

@Serializable
data class CASResponseDto(
    val summary: Summary
)