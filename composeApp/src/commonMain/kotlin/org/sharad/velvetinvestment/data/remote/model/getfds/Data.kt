package org.sharad.velvetinvestment.data.remote.model.getfds

import kotlinx.serialization.Serializable

@Serializable
data class Data(
    val fd_products: List<FdProduct>,
    val pagination: Pagination
)