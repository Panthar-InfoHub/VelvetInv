package org.sharad.velvetinvestment.data.remote.model.firereport

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FireReportDto(
    val code: Int,
    val `data`: FireReportWrapperDto,
    val message: String
)

@Serializable
data class FireReportWrapperDto(
    @SerialName("actual")
    val actual: Data,

    @SerialName("projected")
    val projected: Data
)