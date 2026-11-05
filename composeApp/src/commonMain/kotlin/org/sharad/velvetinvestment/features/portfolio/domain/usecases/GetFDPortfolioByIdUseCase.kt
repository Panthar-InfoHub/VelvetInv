package org.sharad.velvetinvestment.features.portfolio.domain.usecases

import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse
import org.sharad.velvetinvestment.features.portfolio.domain.models.FixedDepositTransactionDomain
import org.sharad.velvetinvestment.features.portfolio.domain.repository.PortfolioRepo

class GetFDPortfolioByIdUseCase(
    private val repository: PortfolioRepo
) {
    suspend operator fun invoke(id: String): NetworkResponse<FixedDepositTransactionDomain, ErrorDomain> {
        return repository.getFDPortfolioById(id)
    }
}
