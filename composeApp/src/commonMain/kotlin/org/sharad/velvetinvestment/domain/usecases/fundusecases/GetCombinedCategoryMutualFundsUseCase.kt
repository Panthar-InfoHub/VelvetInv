package org.sharad.velvetinvestment.domain.usecases.fundusecases

import org.sharad.velvetinvestment.domain.models.mutualfunds.CombinedFundsDomain
import org.sharad.velvetinvestment.domain.repository.MutualFundRepository
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse

class GetCombinedCategoryMutualFundsUseCase(
    private val repository: MutualFundRepository
) {
    suspend operator fun invoke(): NetworkResponse<CombinedFundsDomain, ErrorDomain> {
        return repository.getCombinedCategoryMutualFunds()
    }
}
