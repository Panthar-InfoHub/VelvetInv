package org.sharad.velvetinvestment.domain.usecases.userfinance

import org.sharad.velvetinvestment.domain.models.fire.FireReportDomain
import org.sharad.velvetinvestment.domain.repository.UserFinance
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkError
import org.sharad.velvetinvestment.utils.networking.NetworkResponse

class GetFireReportUseCase(
    private val repository: UserFinance
) {
    suspend operator fun invoke():
            NetworkResponse<FireReportDomain, ErrorDomain> {
        return repository.getFireReport()
    }
}