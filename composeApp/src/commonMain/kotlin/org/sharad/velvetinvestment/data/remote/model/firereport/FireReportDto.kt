package org.sharad.velvetinvestment.data.remote.model.firereport

import kotlinx.serialization.Serializable

@Serializable
data class FireReportDto(
    val code: Int,
    val `data`: Data,
    val message: String
)