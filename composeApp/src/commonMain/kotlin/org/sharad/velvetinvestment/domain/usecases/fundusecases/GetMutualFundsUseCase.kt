package org.sharad.velvetinvestment.domain.usecases.fundusecases

import org.sharad.velvetinvestment.utils.networking.NetworkError
import org.sharad.velvetinvestment.utils.networking.NetworkResponse
import org.sharad.velvetinvestment.domain.repository.MutualFundRepository
import org.sharad.velvetinvestment.presentation.portfolio.models.FundListCardData

class GetMutualFundsUseCase(
    private val repository: MutualFundRepository
) {
    suspend operator fun invoke():
            NetworkResponse<List<FundListCardData>, NetworkError> {

        return repository.getMutualFunds()
    }
}
