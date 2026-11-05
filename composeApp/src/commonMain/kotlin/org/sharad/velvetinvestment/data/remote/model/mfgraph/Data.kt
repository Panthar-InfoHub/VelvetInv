package org.sharad.velvetinvestment.data.remote.model.mfgraph

import kotlinx.serialization.Serializable

/** One NAV point from `/mf/history/{id}`. */
@Serializable
data class Data(
    val nav: String = "0",
    val nav_date: String = "",
    val daily_change: Double? = null
)
