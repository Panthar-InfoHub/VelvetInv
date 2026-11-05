package org.sharad.velvetinvestment.data.remote.model.mfgraph

import kotlinx.serialization.Serializable

@Serializable
data class MFGraphDto(
    val `data`: List<Data> = emptyList(),
    val message: String = "",
    val success: Boolean = false
)
