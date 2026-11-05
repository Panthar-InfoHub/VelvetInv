package org.sharad.velvetinvestment.features.mutualfund.domain.usecases

import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse
import org.sharad.velvetinvestment.features.mutualfund.domain.models.CategoryMutualFundDomain
import org.sharad.velvetinvestment.features.mutualfund.domain.repository.MutualFundRepository

class GetCategoryMutualFundsUseCase(
    private val repository: MutualFundRepository
) {

    suspend operator fun invoke():
            NetworkResponse<List<CategoryMutualFundDomain>, ErrorDomain> {
        return repository.getCategoryMutualFunds()
    }
}
