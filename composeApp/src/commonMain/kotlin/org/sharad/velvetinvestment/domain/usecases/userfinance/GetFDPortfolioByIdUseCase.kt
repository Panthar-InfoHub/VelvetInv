package org.sharad.velvetinvestment.domain.usecases.userfinance

import org.sharad.velvetinvestment.domain.models.portfolio.FixedDepositTransactionDomain
import org.sharad.velvetinvestment.domain.repository.UserFinance
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse

class GetFDPortfolioByIdUseCase(
    private val repository: UserFinance
) {
    suspend operator fun invoke(id: String): NetworkResponse<FixedDepositTransactionDomain, ErrorDomain> {
        return repository.getFDPortfolioById(id)
    }
}
