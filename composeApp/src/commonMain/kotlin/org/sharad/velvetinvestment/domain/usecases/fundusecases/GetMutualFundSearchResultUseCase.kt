package org.sharad.velvetinvestment.domain.usecases.fundusecases

import org.sharad.velvetinvestment.domain.models.PaginatedData
import org.sharad.velvetinvestment.domain.models.mutualfunds.MutualFundDomain
import org.sharad.velvetinvestment.domain.repository.MutualFundRepository
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse

class GetMutualFundSearchResultUseCase(
    private val repository: MutualFundRepository
) {

    suspend operator fun invoke(
        search: String?=null,
        page:Int?=1,
        limit:Int?=20,
        sort:String?=null,
        risk:Int?=null,
        category:String?=null,
        fundCategory:String?=null
    ): NetworkResponse<PaginatedData<MutualFundDomain>, ErrorDomain> {

        return repository.getMutualFundsBySearch(search, page, limit, sort, risk, category,fundCategory)
    }
}
