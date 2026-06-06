package org.sharad.velvetinvestment.domain.usecases.userfinance

import org.sharad.velvetinvestment.data.remote.mapper.toInvestMoreDto
import org.sharad.velvetinvestment.domain.repository.UserFinance
import org.sharad.velvetinvestment.presentation.portfolio.viewmodel.LumpSumAdd
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse

class InvestMoreLumpsumUseCase(
    private val repository: UserFinance
) {
    suspend operator fun invoke(items: List<LumpSumAdd>): NetworkResponse<String, ErrorDomain> {
        return repository.investMoreLumpsum(items.toInvestMoreDto())
    }
}
