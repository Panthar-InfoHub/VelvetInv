package org.sharad.velvetinvestment.domain.usecases.fundusecases

import com.sharad.surakshakawachneo.utils.Networking.NetworkError
import com.sharad.surakshakawachneo.utils.Networking.NetworkResponse
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
