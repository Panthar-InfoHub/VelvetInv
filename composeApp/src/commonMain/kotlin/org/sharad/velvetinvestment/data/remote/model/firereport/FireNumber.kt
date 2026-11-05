package org.sharad.velvetinvestment.data.remote.model.firereport

import kotlinx.serialization.Serializable

@Serializable
data class FireNumber(
    val emi_include: Double = 0.0,
    val emi_exclude: Double = 0.0
)
