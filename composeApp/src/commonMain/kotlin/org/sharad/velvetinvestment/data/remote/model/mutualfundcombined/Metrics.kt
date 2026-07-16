package org.sharad.velvetinvestment.data.remote.model.mutualfundcombined

import kotlinx.serialization.Serializable

@Serializable
data class Metrics(
    val return_1y: Double? = null,
    val return_3y: Double? = null,
    val return_6m: Double? = null,
    val return_90d: Double? = null,
    val return_5y: Double? = null
)