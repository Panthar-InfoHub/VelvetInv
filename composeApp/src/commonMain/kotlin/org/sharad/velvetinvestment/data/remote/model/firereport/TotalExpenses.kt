package org.sharad.velvetinvestment.data.remote.model.firereport

import kotlinx.serialization.Serializable

@Serializable
data class TotalExpenses(
    val emi_exclude: Int,
    val emi_include: Int
)