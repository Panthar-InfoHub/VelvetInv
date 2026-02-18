package org.sharad.velvetinvestment.domain.usecases.fundusecases

import org.sharad.velvetinvestment.domain.models.mutualfunds.MutualFundGraphDomain
import org.sharad.velvetinvestment.domain.repository.MutualFundRepository
import org.sharad.velvetinvestment.utils.networking.NetworkError
import org.sharad.velvetinvestment.utils.networking.NetworkResponse

class GetMutualFundGraphUseCase(
    private val repository: MutualFundRepository
) {

    suspend operator fun invoke(
        id: String,
        selectedYear: String
    ): NetworkResponse<MutualFundGraphDomain, NetworkError> {
        return repository.getMutualFundGraph(id)
    }
}
