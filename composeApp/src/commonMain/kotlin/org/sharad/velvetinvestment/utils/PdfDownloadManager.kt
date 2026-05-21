package org.sharad.velvetinvestment.utils

interface PdfDownloadManager {
    fun downloadPdf(
        pdfBytes: ByteArray,
        fileName: String,
        onSuccess: (String?) -> Unit,
        onFailed: (String) -> Unit
    )
    suspend fun downloadUrlPdf(
        url: String,
        fileName: String,
        onProgress: (progress: Int) -> Unit = {},
        onSuccess: () -> Unit = {},
        onFailure: () -> Unit = {}
    )
}