package org.sharad.velvetinvestment.data.remote.model.mfkyc.mfcontractpdf

import kotlinx.serialization.Serializable

@Serializable
data class ContractPdfDto(
    val `data`: Data,
    val message: String,
    val success: Boolean
)