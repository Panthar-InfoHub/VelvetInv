package org.sharad.velvetinvestment.data.remote.model.mfkyc.imageupload

import kotlinx.serialization.Serializable

@Serializable
data class UrlUploadResultDto(
    val `data`: Data,
    val message: String,
    val success: Boolean
)