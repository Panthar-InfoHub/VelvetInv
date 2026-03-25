package org.sharad.velvetinvestment.data.remote.model.getmf

import kotlinx.serialization.Serializable

@Serializable
data class Data(
    val mutual_funds: List<MutualFund>,
    val pagination: Pagination
)