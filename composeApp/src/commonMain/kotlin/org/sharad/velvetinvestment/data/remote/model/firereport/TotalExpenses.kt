package org.sharad.velvetinvestment.data.remote.model.firereport

import kotlinx.serialization.Serializable

@Serializable
data class TotalExpenses(
    val emi_exclude: Long,
    val emi_include: Long
)