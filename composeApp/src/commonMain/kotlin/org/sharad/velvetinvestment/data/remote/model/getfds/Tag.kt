package org.sharad.velvetinvestment.data.remote.model.getfds

import kotlinx.serialization.Serializable

@Serializable
data class Tag(
    val color: String,
    val text: String,
    val textColor: String
)