package org.sharad.velvetinvestment.data.remote.model.mfkyc.mfcontractpdf

import kotlinx.serialization.Serializable

@Serializable
data class Result(
    val combinedPdf: String
)