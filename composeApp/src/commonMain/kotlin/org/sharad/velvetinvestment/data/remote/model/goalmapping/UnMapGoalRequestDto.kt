package org.sharad.velvetinvestment.data.remote.model.goalmapping

import kotlinx.serialization.Serializable

@Serializable
data class UnMapGoalRequestDto(
    val goal_id: Int
)
