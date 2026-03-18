package org.sharad.velvetinvestment.utils

interface PdfDownloadManager {
    fun downloadPdf(
        pdfBytes: ByteArray,
        fileName: String,
        onSuccess: () -> Unit,
        onFailed: (String) -> Unit
    )
}