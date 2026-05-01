package org.sharad.velvetinvestment.domain.usecases.userfinance

import org.sharad.velvetinvestment.domain.models.portfolio.PortfolioDomain
import org.sharad.velvetinvestment.domain.repository.UserFinance
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse

class GetPortfolioUseCase(
    private val repository: UserFinance
) {
    suspend operator fun invoke(): NetworkResponse<PortfolioDomain, ErrorDomain> {
        return repository.getPortfolio()
    }
}
