package org.sharad.velvetinvestment.domain.usecases.fundusecases

import org.sharad.velvetinvestment.domain.models.mutualfunds.MutualFundDomain
import org.sharad.velvetinvestment.domain.repository.MutualFundRepository
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse

class GetMutualFundTopPicksUseCase(
    private val repository: MutualFundRepository
) {
    suspend operator fun invoke(): NetworkResponse<List<MutualFundDomain>, ErrorDomain> {
        val response= repository.getMutualFundsBySearch(
            search = null,
            page = 1,
            limit = 4,
            sort = "3y",
            risk = null,
            category = null,
            fundCategory = null
        )
        return when (response) {
            is NetworkResponse.Success -> {
                NetworkResponse.Success(response.data.items)
            }

            is NetworkResponse.Error -> {
                NetworkResponse.Error(response.error)
            }
        }
    }
}
