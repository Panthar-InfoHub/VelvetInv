package org.sharad.velvetinvestment.data.remote.model.cartaddsip

import kotlinx.serialization.Serializable

@Serializable
data class Data(
    val code: Int,
    val results: List<Result>
)