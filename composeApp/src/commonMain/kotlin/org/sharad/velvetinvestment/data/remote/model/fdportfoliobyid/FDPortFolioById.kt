package org.sharad.velvetinvestment.data.remote.model.fdportfoliobyid

import kotlinx.serialization.Serializable

@Serializable
data class FDPortFolioById(
    val `data`: Data,
    val message: String,
    val success: Boolean
)