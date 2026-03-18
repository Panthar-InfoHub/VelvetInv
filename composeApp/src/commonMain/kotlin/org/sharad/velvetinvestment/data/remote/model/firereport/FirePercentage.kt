package org.sharad.velvetinvestment.data.remote.model.firereport

import kotlinx.serialization.Serializable

@Serializable
data class FirePercentage(
    val emi_exclude: Double,
    val emi_include: Double
)