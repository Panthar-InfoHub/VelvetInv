package org.sharad.velvetinvestment.features.fd.domain.usecases

import org.sharad.velvetinvestment.features.fd.domain.model.FixedDepositDomain
import org.sharad.velvetinvestment.features.fd.domain.model.PaginatedData
import org.sharad.velvetinvestment.features.fd.domain.repository.FixedDepositRepository
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse

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
