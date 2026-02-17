package org.sharad.velvetinvestment.domain.usecases.fundusecases

import com.sharad.surakshakawachneo.utils.Networking.NetworkError
import com.sharad.surakshakawachneo.utils.Networking.NetworkResponse
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
