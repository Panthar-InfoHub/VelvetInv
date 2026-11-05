package org.sharad.velvetinvestment.features.portfolio.data.model.cancelorder

import kotlinx.serialization.Serializable

@Serializable
data class CancelXsipRequestDto(
    val xsip_reg_no: String
)
