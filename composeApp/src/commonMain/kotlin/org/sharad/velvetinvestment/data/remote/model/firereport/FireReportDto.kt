package org.sharad.velvetinvestment.data.remote.model.firereport

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FireReportDto(
    val code: Int = 0,
    val `data`: FireReportWrapperDto,
    val message: String = ""
)

@Serializable
data class FireReportWrapperDto(
    @SerialName("actual")
    val actual: Data = Data(),

    @SerialName("projected")
    val projected: Data = Data()
)
