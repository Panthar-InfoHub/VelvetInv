package org.sharad.velvetinvestment.features.portfolio.domain.usecases

import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse
import org.sharad.velvetinvestment.utils.PdfDownloadManager
import org.sharad.velvetinvestment.features.portfolio.domain.repository.PortfolioRepo

class DownloadFolioSpecificReportUseCase(
    private val repository: PortfolioRepo,
    private val downloadManager: PdfDownloadManager
) {
    suspend operator fun invoke(
        folio: String,
        onSuccess: () -> Unit,
        onFailed: (String) -> Unit
    ): NetworkResponse<Unit, ErrorDomain> {
        return when (val response = repository.getFolioSpecificReport(folio)) {
            is NetworkResponse.Success -> {
                downloadManager.downloadPdf(
                    pdfBytes = response.data,
                    fileName = "Fund_Holding_Report_$folio",
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