package org.sharad.velvetinvestment.domain.usecases.home

import org.sharad.velvetinvestment.domain.repository.HomeRepository

class GetFireReportSummaryUseCase(
    private val repository: HomeRepository
) {
    suspend operator fun invoke() =
        repository.getFireReportSummary()
}
