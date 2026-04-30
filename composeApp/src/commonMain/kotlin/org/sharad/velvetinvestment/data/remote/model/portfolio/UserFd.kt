package org.sharad.velvetinvestment.data.remote.model.portfolio

import kotlinx.serialization.Serializable

@Serializable
data class UserFd(
    val fd_transactions: List<FdTransaction>,
    val pagination: Pagination
)