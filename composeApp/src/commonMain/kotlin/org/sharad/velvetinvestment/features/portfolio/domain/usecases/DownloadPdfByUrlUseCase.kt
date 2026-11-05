package org.sharad.velvetinvestment.features.portfolio.domain.usecases

import org.sharad.velvetinvestment.utils.PdfDownloadManager


class DownloadPdfByUrlUseCase(
    private val pdfDownloadManager: PdfDownloadManager
) {
    suspend operator fun invoke(
        url: String,
        fileName: String,
        onProgress: (Int) -> Unit = {},
        onSuccess: () -> Unit = {},
        onFailure: () -> Unit = {}
    ) {
        pdfDownloadManager.downloadUrlPdf(
            url = url,
            fileName = fileName,
            onProgress = onProgress,
            onSuccess = onSuccess,
            onFailure = onFailure
        )
    }
}
