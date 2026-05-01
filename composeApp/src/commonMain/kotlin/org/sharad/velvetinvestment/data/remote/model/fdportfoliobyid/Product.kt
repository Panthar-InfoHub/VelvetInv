package org.sharad.velvetinvestment.data.remote.model.fdportfoliobyid

import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val id: String,
    val issuer: Issuer,
    val issuer_id: String
)