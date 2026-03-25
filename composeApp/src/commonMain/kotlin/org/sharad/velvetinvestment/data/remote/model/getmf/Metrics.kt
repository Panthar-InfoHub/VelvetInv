package org.sharad.velvetinvestment.data.remote.model.getmf

import kotlinx.serialization.Serializable

@Serializable
data class Metrics(
    val return_1y: Double?,
    val return_3y: Double?,
    val return_6m: Double?,
    val return_90d: Double?
)