package org.sharad.velvetinvestment.features.portfolio.domain.usecases

import org.sharad.velvetinvestment.features.portfolio.domain.models.LumpSumAdd
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse
import org.sharad.velvetinvestment.features.portfolio.data.mapper.toInvestMoreDto
import org.sharad.velvetinvestment.features.portfolio.domain.repository.PortfolioRepo

class InvestMoreLumpsumUseCase(
    private val repository: PortfolioRepo
) {
    suspend operator fun invoke(items: List<LumpSumAdd>): NetworkResponse<String, ErrorDomain> {
        return repository.investMoreLumpsum(items.toInvestMoreDto())
    }
}
