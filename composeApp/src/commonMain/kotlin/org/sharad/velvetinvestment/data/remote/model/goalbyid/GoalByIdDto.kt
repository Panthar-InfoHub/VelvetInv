package org.sharad.velvetinvestment.data.remote.model.goalbyid

import kotlinx.serialization.Serializable

@Serializable
data class GoalByIdDto(
    val `data`: Data,
    val message: String,
    val success: Boolean
)