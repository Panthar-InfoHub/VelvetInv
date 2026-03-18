package org.sharad.velvetinvestment.data.remote.model.onboarding

import kotlinx.serialization.Serializable

@Serializable
data class Finance(
    val annual_income: Long,
    val expense_food: Long,
    val expense_house: Long,
    val expense_others: Long,
    val expense_transportation: Long
)