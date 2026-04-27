package org.sharad.velvetinvestment.data.remote.model.mutualfundcombined

import kotlinx.serialization.Serializable

@Serializable
data class NormalFunds(
    val items: List<ItemX>,
    val key: String,
    val title: String
)