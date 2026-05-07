package org.sharad.velvetinvestment.domain.usecases.fixeddepositusecases

import org.sharad.velvetinvestment.domain.models.fixeddeposits.FixedDepositDomain
import org.sharad.velvetinvestment.domain.repository.FixedDepositRepository
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse

class GetTopPickFDUseCase(
    private val repository: FixedDepositRepository
) {

    suspend operator fun invoke(): NetworkResponse<List<FixedDepositDomain>, ErrorDomain> {
        val response = repository.getFDSearchResult(
            tenure = "1y",
            limit = 4,
            maxDeposit = null,
            minDeposit = null,
            payoutFrequency = null,
            page = 1,
            search = null
        )
        return when(response){
            is NetworkResponse.Success -> {
                NetworkResponse.Success(response.data.items)
            }
            is NetworkResponse.Error -> {
                NetworkResponse.Error(response.error)
            }
        }
    }
}
