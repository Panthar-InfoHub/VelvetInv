package org.sharad.velvetinvestment.data.remote.model.useedata

import kotlinx.serialization.Serializable

@Serializable
data class FireNumber(
    val emi_exclude: Double,
    val emi_include: Double
)