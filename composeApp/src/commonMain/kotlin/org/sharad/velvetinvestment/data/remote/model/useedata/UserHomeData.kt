package org.sharad.velvetinvestment.data.remote.model.useedata

import kotlinx.serialization.Serializable

@Serializable
data class UserHomeData(
    val fire_number: FireNumber,
    val net_worth: Double,
    val total_expenses: TotalExpenses
)