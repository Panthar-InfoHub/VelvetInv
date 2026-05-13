package org.sharad.velvetinvestment.data.remote.model.goalbyid

import kotlinx.serialization.Serializable

@Serializable
data class Scheme(
    val actualfolio: String,
    val bal_units: String,
    val current_val: String,
    val folio: String,
    val nav: String,
    val scheme_id: String,
    val scheme_name: String
)