package org.sharad.velvetinvestment.features.portfolio.domain.usecases

import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse
import org.sharad.velvetinvestment.features.portfolio.domain.models.PortfolioDomain
import org.sharad.velvetinvestment.features.portfolio.domain.repository.PortfolioRepo

class GetPortfolioUseCase(
    private val repository: PortfolioRepo
) {
    suspend operator fun invoke(): NetworkResponse<PortfolioDomain, ErrorDomain> {
        return repository.getPortfolio()
    }
}
