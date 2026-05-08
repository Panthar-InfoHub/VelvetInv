package org.sharad.velvetinvestment.domain.usecases.userfinance

import org.sharad.velvetinvestment.domain.models.user.InvestmentRateDomain
import org.sharad.velvetinvestment.domain.repository.UserFinance
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse

class GetInvestmentRateDataUseCase(
    private val repo: UserFinance
) {
    suspend operator fun invoke(): NetworkResponse<InvestmentRateDomain, ErrorDomain> {
        return repo.getInvestmentRateData()
    }
}