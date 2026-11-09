package org.sharad.velvetinvestment.features.investmentrate.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class Savings(
    val amount: Long,
    val percent: Double
)