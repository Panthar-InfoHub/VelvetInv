package org.sharad.velvetinvestment.domain.usecases.fundusecases

import org.sharad.velvetinvestment.domain.models.mutualfunds.MutualFundDetailsDomain
import org.sharad.velvetinvestment.domain.repository.MutualFundRepository
import org.sharad.velvetinvestment.utils.networking.NetworkError
import org.sharad.velvetinvestment.utils.networking.NetworkResponse

class GetMutualFundDetailsUseCase(
    private val repository: MutualFundRepository
) {

    suspend operator fun invoke(
        id: String
    ): NetworkResponse<MutualFundDetailsDomain, NetworkError> {
        return repository.getMutualFundDetails(id)
    }
}
