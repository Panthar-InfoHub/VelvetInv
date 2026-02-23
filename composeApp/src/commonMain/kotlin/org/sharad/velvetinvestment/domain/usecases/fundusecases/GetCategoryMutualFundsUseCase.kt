package org.sharad.velvetinvestment.domain.usecases.fundusecases

import org.sharad.velvetinvestment.utils.networking.NetworkError
import org.sharad.velvetinvestment.utils.networking.NetworkResponse
import org.sharad.velvetinvestment.domain.models.mutualfunds.CategoryMutualFundDomain
import org.sharad.velvetinvestment.domain.repository.MutualFundRepository

class GetCategoryMutualFundsUseCase(
    private val repository: MutualFundRepository
) {

    suspend operator fun invoke():
            NetworkResponse<List<CategoryMutualFundDomain>, NetworkError> {
        return repository.getCategoryMutualFunds()
    }
}
