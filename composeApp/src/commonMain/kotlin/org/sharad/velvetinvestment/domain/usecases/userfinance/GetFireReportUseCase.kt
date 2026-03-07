package org.sharad.velvetinvestment.domain.usecases.userfinance

import org.sharad.velvetinvestment.domain.models.fire.FireCombinedDomainModel
import org.sharad.velvetinvestment.domain.models.fire.FireReportDomainModel
import org.sharad.velvetinvestment.domain.repository.UserFinance
import org.sharad.velvetinvestment.utils.networking.NetworkError
import org.sharad.velvetinvestment.utils.networking.NetworkResponse

class GetFireReportUseCase(
    private val repository: UserFinance
) {

    suspend operator fun invoke():
            NetworkResponse<FireCombinedDomainModel, NetworkError> {
        return repository.getFireReport()
    }
}