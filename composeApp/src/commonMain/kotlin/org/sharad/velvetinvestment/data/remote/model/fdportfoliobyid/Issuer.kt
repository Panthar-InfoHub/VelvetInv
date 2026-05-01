package org.sharad.velvetinvestment.data.remote.model.fdportfoliobyid

import kotlinx.serialization.Serializable

@Serializable
data class Issuer(
    val banner_url: String,
    val display_name: String,
    val full_name: String,
    val id: String,
    val issuer_type: String,
    val logo_url: String,
    val rating_text: String
)