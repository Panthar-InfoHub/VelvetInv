package org.sharad.velvetinvestment.domain.usecases.report

import org.sharad.velvetinvestment.domain.repository.UserFinance
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse

class ExportTaxReportUseCase(
    private val repository: UserFinance
) {
    suspend operator fun invoke(year: Int): NetworkResponse<String, ErrorDomain> {
        return repository.exportReport(type = "tax", year = year)
    }
}