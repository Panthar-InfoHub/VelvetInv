package org.sharad.velvetinvestment.data.remote.model.mfkyc.imageupload

import kotlinx.serialization.Serializable

@Serializable
data class ImageUploadDto(
    val `file`: File
)