package org.sharad.velvetinvestment.features.portfolio.domain.usecases

import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse
import org.sharad.velvetinvestment.features.portfolio.domain.repository.PortfolioRepo

class CancelLumpSumOrderUseCase(
    private val repository: PortfolioRepo
) {
    suspend operator fun invoke(orderId: String): NetworkResponse<Unit, ErrorDomain> {
        return repository.cancelLumpSumOrder(orderId)
    }
}
