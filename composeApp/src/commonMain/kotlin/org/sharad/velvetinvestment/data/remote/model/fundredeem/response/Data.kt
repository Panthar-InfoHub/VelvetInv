package org.sharad.velvetinvestment.data.remote.model.fundredeem.response

import kotlinx.serialization.Serializable

@Serializable
data class Data(
    val payment_link: String
)