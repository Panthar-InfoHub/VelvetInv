package org.sharad.velvetinvestment.data.remote.model.cartaddlumpsum

import kotlinx.serialization.Serializable

@Serializable
data class Data(
    val code: Int,
    val results: List<Result>
)