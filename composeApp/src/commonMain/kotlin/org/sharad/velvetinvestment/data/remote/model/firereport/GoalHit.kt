package org.sharad.velvetinvestment.data.remote.model.firereport

import kotlinx.serialization.Serializable

/** Shape not seen in a live response yet (always empty so far); fields default so it can't break parsing. */
@Serializable
data class GoalHit(
    val amount: Double = 0.0,
    val label: String = ""
)
