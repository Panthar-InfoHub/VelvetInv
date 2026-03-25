package org.sharad.velvetinvestment.data.remote.model.mfdetails

import kotlinx.serialization.Serializable

@Serializable
data class Metrics(
    val nav_change_pct: Double,
    val return_1y: Double?,
    val return_30d: Double?,
    val return_3y: Double?,
    val return_6m: Double?,
    val return_90d: Double?
)