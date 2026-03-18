package org.sharad.velvetinvestment.domain.usecases.userfinance

import org.sharad.velvetinvestment.domain.repository.UserFinance
import org.sharad.velvetinvestment.utils.PdfDownloadManager
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse
import kotlin.time.Clock

class DownloadFirePdfUseCase(
    private val repo: UserFinance,
    private val pdfDownloader: PdfDownloadManager
) {

    suspend operator fun invoke(
        onSuccess: () -> Unit,
        onFailed: (String) -> Unit
    ): NetworkResponse<Unit, ErrorDomain> {

        return when (val response = repo.getFirePdf()) {
            is NetworkResponse.Success -> {
                pdfDownloader.downloadPdf(
                    pdfBytes = response.data,
                    fileName = "fire_report_${Clock.System.now().toEpochMilliseconds()}.pdf",
                    onSuccess = {
                        onSuccess()
                    },
                    onFailed = { error ->
                        onFailed(error)
                    }
                )
                NetworkResponse.Success(Unit)
            }
            is NetworkResponse.Error -> {
                response
            }
        }
    }
}

