package org.sharad.velvetinvestment.data.remote.model.cancelorder

import kotlinx.serialization.Serializable

@Serializable
data class CancelXsipRequestDto(
    val xsip_reg_no: String
)
