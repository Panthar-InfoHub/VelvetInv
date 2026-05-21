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
        fileName: String,
        onSuccess: () -> Unit,
        onFailed: (String) -> Unit,
    ): NetworkResponse<Unit, ErrorDomain> {

        return when (val response = repo.getFirePdf()) {
            is NetworkResponse.Success -> {
                pdfDownloader.downloadPdf(
                    pdfBytes = response.data,
                    fileName = fileName,
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

