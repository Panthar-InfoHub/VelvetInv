package org.sharad.velvetinvestment.data.remote.model.mfgraph

import kotlinx.serialization.Serializable

@Serializable
data class MFGraphDto(
    val `data`: List<Data>,
    val message: String,
    val success: Boolean
)