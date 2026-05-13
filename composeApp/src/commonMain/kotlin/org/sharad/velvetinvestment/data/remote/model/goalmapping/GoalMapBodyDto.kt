package org.sharad.velvetinvestment.data.remote.model.goalmapping

import kotlinx.serialization.Serializable

@Serializable
data class GoalMapBodyDto(
    val goal_id: Int,
    val map_data: List<MapData>
)