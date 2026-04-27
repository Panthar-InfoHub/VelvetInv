package org.sharad.velvetinvestment.domain.repository

import org.sharad.velvetinvestment.data.remote.model.cartaddsip.AddCartSipRequest
import org.sharad.velvetinvestment.domain.models.PaginatedData
import org.sharad.velvetinvestment.domain.models.explore.MutualFundTopPicksDomain
import org.sharad.velvetinvestment.domain.models.mutualfunds.BundledMutualFundDomain
import org.sharad.velvetinvestment.domain.models.mutualfunds.CombinedFundsDomain
import org.sharad.velvetinvestment.domain.models.mutualfunds.MutualFundDetailsDomain
import org.sharad.velvetinvestment.domain.models.mutualfunds.MutualFundDomain
import org.sharad.velvetinvestment.domain.models.mutualfunds.MutualFundGraphDomain
import org.sharad.velvetinvestment.domain.models.usercart.UserCartDomain
import org.sharad.velvetinvestment.presentation.portfolio.models.FundListCardData
import org.sharad.velvetinvestment.presentation.portfolio.models.MutualFundDashBoardData
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse

interface MutualFundRepository {

    suspend fun getPortfolioMutualFunds():
            NetworkResponse<List<FundListCardData>, ErrorDomain>

    suspend fun getDashboard():
            NetworkResponse<MutualFundDashBoardData, ErrorDomain>

    suspend fun getMutualFundTopPicks():
            NetworkResponse<List<MutualFundTopPicksDomain>, ErrorDomain>

    suspend fun getCategoryMutualFunds(
        page: Int? = null,
        limit: Int? = null
    ): NetworkResponse<List<BundledMutualFundDomain>, ErrorDomain>

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

    suspend fun getMutualFundCart() : NetworkResponse<UserCartDomain, ErrorDomain>

    suspend fun deleteCartItem(id: String) : NetworkResponse<Unit, ErrorDomain>

    suspend fun addToCartLumSumFund(id:String,amount:Long): NetworkResponse<Unit, ErrorDomain>
    suspend fun addToCartSipFund(request: AddCartSipRequest): NetworkResponse<Unit, ErrorDomain>

    suspend fun purchaseLumSumFund(): NetworkResponse<String, ErrorDomain>
    suspend fun purchaseSipFund(): NetworkResponse<String, ErrorDomain>

    suspend fun getCombinedCategoryMutualFunds(): NetworkResponse<CombinedFundsDomain, ErrorDomain>

}

