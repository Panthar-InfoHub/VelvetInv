package org.sharad.velvetinvestment.data.remote.model.goalmapping

import kotlinx.serialization.Serializable

@Serializable
data class MapData(
    val folio: String,
    val scheme_id: String
)