package org.sharad.velvetinvestment.data.remote.model.casreport

import kotlinx.serialization.Serializable

@Serializable
data class RedirectBody(
    val fd_trans_id: String,
    val event: String
)
