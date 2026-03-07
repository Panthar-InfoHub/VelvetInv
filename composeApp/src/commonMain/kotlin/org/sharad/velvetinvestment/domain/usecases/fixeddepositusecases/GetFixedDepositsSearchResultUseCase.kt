package org.sharad.velvetinvestment.domain.usecases.fixeddepositusecases

import org.sharad.velvetinvestment.domain.models.fixeddeposits.FixedDepositDomain
import org.sharad.velvetinvestment.utils.networking.NetworkError
import org.sharad.velvetinvestment.utils.networking.NetworkResponse
import org.sharad.velvetinvestment.domain.repository.FixedDepositRepository

class GetFixedDepositsSearchResultUseCase(
    private val repository: FixedDepositRepository
) {

    suspend operator fun invoke(
        searchId: String
    ): NetworkResponse<List<FixedDepositDomain>, NetworkError> {

        return repository.getFDSearchResult(searchId)
    }
}
