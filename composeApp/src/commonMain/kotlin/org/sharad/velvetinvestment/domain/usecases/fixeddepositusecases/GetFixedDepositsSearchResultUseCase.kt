package org.sharad.velvetinvestment.domain.usecases.fixeddepositusecases

import org.sharad.velvetinvestment.domain.models.PaginatedData
import org.sharad.velvetinvestment.domain.models.fixeddeposits.FixedDepositDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse
import org.sharad.velvetinvestment.domain.repository.FixedDepositRepository
import org.sharad.velvetinvestment.utils.networking.ErrorDomain

class GetFixedDepositsSearchResultUseCase(
    private val repository: FixedDepositRepository,
) {

    suspend operator fun invoke(
        page: Int? = 1,
        limit: Int? = 30,
        tenure: String? = null,
        payoutFrequency: String? = null,
        minDeposit: Double? = null,
        maxDeposit: Double? = null,
        search: String? = null
    ): NetworkResponse<PaginatedData<FixedDepositDomain>, ErrorDomain> {

        return repository.getFDSearchResult(
            page = page,
            limit = limit,
            tenure = tenure,
            payoutFrequency = payoutFrequency,
            minDeposit = minDeposit,
            maxDeposit = maxDeposit,
            search = search
        )
    }
}
