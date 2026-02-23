package org.sharad.velvetinvestment.domain.usecases.fundusecases

import org.sharad.velvetinvestment.utils.networking.NetworkError
import org.sharad.velvetinvestment.utils.networking.NetworkResponse
import org.sharad.velvetinvestment.domain.models.mutualfunds.MutualFundDomain
import org.sharad.velvetinvestment.domain.repository.MutualFundRepository

class GetMutualFundSearchResultUseCase(
    private val repository: MutualFundRepository
) {

    suspend operator fun invoke(
        searchId: String
    ): NetworkResponse<List<MutualFundDomain>, NetworkError> {

        return repository.getMutualFundsBySearch(searchId)
    }
}
