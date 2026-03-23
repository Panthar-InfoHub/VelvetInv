package org.sharad.velvetinvestment.data.remote.model.mfkyc.imageupload

import kotlinx.serialization.Serializable

@Serializable
data class File(
    val directURL: String,
    val filetype: String,
    val id: Int,
    val `protected`: Boolean,
    val size: Int
)