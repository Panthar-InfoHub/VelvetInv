package org.sharad.velvetinvestment.domain.usecases.fundusecases

import org.sharad.velvetinvestment.domain.models.PaginatedData
import org.sharad.velvetinvestment.utils.networking.NetworkError
import org.sharad.velvetinvestment.utils.networking.NetworkResponse
import org.sharad.velvetinvestment.domain.models.mutualfunds.MutualFundDomain
import org.sharad.velvetinvestment.domain.repository.MutualFundRepository
import org.sharad.velvetinvestment.utils.networking.ErrorDomain

class GetMutualFundSearchResultUseCase(
    private val repository: MutualFundRepository
) {

    suspend operator fun invoke(
        search: String?=null,
        page:Int?=1,
        limit:Int?=20,
        sort:String?=null,
        risk:Int?=null,
        category:String?=null
    ): NetworkResponse<PaginatedData<MutualFundDomain>, ErrorDomain> {

        return repository.getMutualFundsBySearch(search, page, limit, sort, risk, category)
    }
}
