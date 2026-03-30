package org.sharad.velvetinvestment.data.remote.model.fddetails

import kotlinx.serialization.Serializable

@Serializable
data class Usp(
    val description: String,
    val title: String
)