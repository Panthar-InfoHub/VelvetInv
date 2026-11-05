package org.sharad.velvetinvestment.features.portfolio.domain.usecases

import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse
import org.sharad.velvetinvestment.features.portfolio.domain.repository.PortfolioRepo

class ExportTaxReportUseCase(
    private val repository: PortfolioRepo
) {
    suspend operator fun invoke(year: Int): NetworkResponse<String, ErrorDomain> {
        return repository.exportReport(type = "tax", year = year)
    }
}