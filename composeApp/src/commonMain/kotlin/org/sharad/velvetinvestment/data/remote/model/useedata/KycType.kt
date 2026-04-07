package org.sharad.velvetinvestment.data.remote.model.useedata

import kotlinx.serialization.Serializable

@Serializable
data class KycType(
    val trading: KycStatus? =null,
    val mf: KycStatus?= null
)

@Serializable
data class KycStatus(
    val status: String
)