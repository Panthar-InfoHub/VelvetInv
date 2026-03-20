package org.sharad.velvetinvestment.data.remote.model.firereport

import kotlinx.serialization.Serializable

@Serializable
data class GoalHit(
    val amount: Long,
    val label: String
)