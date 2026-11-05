package org.sharad.velvetinvestment.data.remote.model.firereport

import kotlinx.serialization.Serializable

@Serializable
data class YearlyGoalRequirement(
    val year: Int = 0,
    val monthly_required: Double = 0.0,
    val yearly_required: Double = 0.0
)
