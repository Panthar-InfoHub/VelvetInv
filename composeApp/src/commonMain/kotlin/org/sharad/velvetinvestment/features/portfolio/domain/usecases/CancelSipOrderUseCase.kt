package org.sharad.velvetinvestment.features.portfolio.domain.usecases

import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse
import org.sharad.velvetinvestment.features.portfolio.domain.repository.PortfolioRepo

class CancelSipOrderUseCase(
    private val repository: PortfolioRepo
) {
    suspend operator fun invoke(xsipRegNo: String): NetworkResponse<Unit, ErrorDomain> {
        return repository.cancelSipOrder(xsipRegNo)
    }
}
