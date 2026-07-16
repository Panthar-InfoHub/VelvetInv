package org.sharad.velvetinvestment.domain.usecases.mutualfunds

import org.sharad.velvetinvestment.domain.models.PaginatedData
import org.sharad.velvetinvestment.domain.models.mutualfunds.BundledMutualFundDomain
import org.sharad.velvetinvestment.domain.repository.MutualFundRepository
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse

class GetAllBundledFundsUseCase(
    private val repository: MutualFundRepository
) {
    suspend operator fun invoke(
        page: Int?=null,
        limit: Int?=null
    ): NetworkResponse<PaginatedData<BundledMutualFundDomain>, ErrorDomain> {
        return repository.getAllBundledFunds(
            page=page,
            limit=limit
        )
    }
}