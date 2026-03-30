package org.sharad.velvetinvestment.data.remote.model.fddetails

import kotlinx.serialization.Serializable

@Serializable
data class Issuer(
    val about_description: String,
    val banner_url: String,
    val customer_served: String,
    val display_name: String,
    val full_name: String,
    val id: String,
    val issuer_type: String,
    val logo_url: String,
    val operating_since: String,
    val rating_text: String,
    val support_email: String,
    val support_phone: String
)