package org.sharad.velvetinvestment.data.remote.model.allbundles

import kotlinx.serialization.Serializable

@Serializable
data class AllBundlesDto(
    val data: AllBundlesDataDto,
    val message: String,
    val success: Boolean
)

@Serializable
data class AllBundlesDataDto(
    val bundles: List<Data>
)