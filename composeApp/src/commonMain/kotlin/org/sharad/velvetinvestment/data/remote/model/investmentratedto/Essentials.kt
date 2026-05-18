package org.sharad.velvetinvestment.data.remote.model.investmentratedto

import kotlinx.serialization.Serializable

@Serializable
data class Essentials(
    val amount: Long,
    val percent: Double,
    val breakdown: EssentialsBreakdown,
)

@Serializable
data class EssentialsBreakdown(
    val house: EssentialItem,
    val food: EssentialItem,
    val transportation: EssentialItem,
    val others: EssentialItem,
)

@Serializable
data class EssentialItem(
    val amount: Long,
    val percent: Double,
)