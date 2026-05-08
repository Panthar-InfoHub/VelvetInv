package org.sharad.velvetinvestment.data.remote.model.investmentratedto

import kotlinx.serialization.Serializable

@Serializable
data class Essentials(
    val amount: Long,
    val percent: Double
)