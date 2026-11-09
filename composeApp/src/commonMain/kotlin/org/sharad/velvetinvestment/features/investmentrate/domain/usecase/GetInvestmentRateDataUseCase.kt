package org.sharad.velvetinvestment.features.investmentrate.domain.usecase

import org.sharad.velvetinvestment.features.investmentrate.domain.model.InvestmentRateDomain
import org.sharad.velvetinvestment.features.investmentrate.domain.repository.InvestmentRateRepository
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse

class GetInvestmentRateDataUseCase(
    private val repo: InvestmentRateRepository
) {
    suspend operator fun invoke(): NetworkResponse<InvestmentRateDomain, ErrorDomain> {
        return repo.getInvestmentRateData()
    }
}