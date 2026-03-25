package org.sharad.velvetinvestment.domain.repository

import org.sharad.velvetinvestment.domain.models.PaginatedData
import org.sharad.velvetinvestment.utils.networking.NetworkError
import org.sharad.velvetinvestment.utils.networking.NetworkResponse
import org.sharad.velvetinvestment.domain.models.explore.MutualFundTopPicksDomain
import org.sharad.velvetinvestment.domain.models.mutualfunds.CategoryMutualFundDomain
import org.sharad.velvetinvestment.domain.models.mutualfunds.MutualFundDetailsDomain
import org.sharad.velvetinvestment.domain.models.mutualfunds.MutualFundDomain
import org.sharad.velvetinvestment.domain.models.mutualfunds.MutualFundGraphDomain
import org.sharad.velvetinvestment.presentation.portfolio.models.FundListCardData
import org.sharad.velvetinvestment.presentation.portfolio.models.MutualFundDashBoardData
import org.sharad.velvetinvestment.utils.networking.ErrorDomain

interface MutualFundRepository {

    suspend fun getPortfolioMutualFunds():
            NetworkResponse<List<FundListCardData>, NetworkError>

    suspend fun getDashboard():
            NetworkResponse<MutualFundDashBoardData, NetworkError>

    suspend fun getMutualFundTopPicks():
            NetworkResponse<List<MutualFundTopPicksDomain>, NetworkError>

    suspend fun getCategoryMutualFunds():
            NetworkResponse<List<CategoryMutualFundDomain>, NetworkError>

    suspend fun getMutualFundsBySearch(
        search: String?,
        page:Int?,
        limit:Int?,
        sort:String?,
        risk:Int?,
        category:String?
    ): NetworkResponse<PaginatedData<MutualFundDomain>, ErrorDomain>


    suspend fun getMutualFundDetails(
        id: String
    ): NetworkResponse<MutualFundDetailsDomain, ErrorDomain>

    suspend fun getMutualFundGraph(
        id: String,
        period:String
    ): NetworkResponse<MutualFundGraphDomain, ErrorDomain>
}

