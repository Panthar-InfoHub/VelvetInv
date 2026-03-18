package org.sharad.velvetinvestment.data.remote.model.firereport

import kotlinx.serialization.Serializable

@Serializable
data class Data(
    val computed_metrics: ComputedMetrics,
    val goals: List<Goal>,
    val projection: List<Projection>,
    val user_profile: UserProfile
)