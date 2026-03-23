package org.sharad.velvetinvestment.data.remote.model.mfkyc.imageupload

import kotlinx.serialization.Serializable

@Serializable
data class UrlUploadBodyDto(
    val img_url: String,
    val type: String
)