package org.sharad.velvetinvestment.data.remote.model.investmentratedto

import kotlinx.serialization.Serializable

@Serializable
data class Savings(
    val amount: Long,
    val percent: Double
)