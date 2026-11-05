package org.sharad.velvetinvestment.features.portfolio.domain.usecases

import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse
import org.sharad.velvetinvestment.features.portfolio.domain.models.FolioFundDomain
import org.sharad.velvetinvestment.features.portfolio.domain.repository.PortfolioRepo

class GetFolioFundsUseCase(
    private val repository: PortfolioRepo
) {
    suspend operator fun invoke(folioId: String): NetworkResponse<List<FolioFundDomain>, ErrorDomain> {
        return repository.getFolioFunds(folioId)
    }
}
