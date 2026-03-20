package org.sharad.velvetinvestment.utils

import androidx.compose.runtime.Composable


@Composable
expect fun rememberDocumentManager(onResult: (SharedDocument?) -> Unit): DocumentManager

expect class DocumentManager(
    onLaunch: () -> Unit
) {
    fun launch()
}

expect class SharedDocument {
    fun toByteArray(): ByteArray?
    fun toText(): String?
    fun fileName(): String?
}