package org.sharad.velvetinvestment.domain.usecases.userfinance

import org.sharad.velvetinvestment.domain.models.portfolio.FolioFundDomain
import org.sharad.velvetinvestment.domain.repository.UserFinance
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse

class GetFolioFundsUseCase(
    private val repository: UserFinance
) {
    suspend operator fun invoke(folioId: String): NetworkResponse<List<FolioFundDomain>, ErrorDomain> {
        return repository.getFolioFunds(folioId)
    }
}
