package org.sharad.velvetinvestment.utils

interface PdfDownloadManager {
    fun downloadPdf(
        pdfBytes: ByteArray,
        fileName: String,
        onSuccess: (String?) -> Unit,
        onFailed: (String) -> Unit
    )
}