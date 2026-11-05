package org.sharad.velvetinvestment.features.mutualfund.data.remote.model.fundredeem.response

import kotlinx.serialization.Serializable

@Serializable
data class Data(
    val payment_link: String
)