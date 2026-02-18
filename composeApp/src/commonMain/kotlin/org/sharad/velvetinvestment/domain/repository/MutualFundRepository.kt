package org.sharad.velvetinvestment.domain.repository

import org.sharad.velvetinvestment.utils.networking.NetworkError
import org.sharad.velvetinvestment.utils.networking.NetworkResponse
import org.sharad.velvetinvestment.domain.models.explore.MutualFundTopPicksDomain
import org.sharad.velvetinvestment.domain.models.mutualfunds.CategoryMutualFundDomain
import org.sharad.velvetinvestment.domain.models.mutualfunds.MutualFundDetailsDomain
import org.sharad.velvetinvestment.domain.models.mutualfunds.MutualFundDomain
import org.sharad.velvetinvestment.domain.models.mutualfunds.MutualFundGraphDomain
import org.sharad.velvetinvestment.presentation.portfolio.models.FundListCardData
import org.sharad.velvetinvestment.presentation.portfolio.models.MutualFundDashBoardData

interface MutualFundRepository {

    suspend fun getMutualFunds():
            NetworkResponse<List<FundListCardData>, NetworkError>

    suspend fun getDashboard():
            NetworkResponse<MutualFundDashBoardData, NetworkError>

    suspend fun getMutualFundTopPicks():
            NetworkResponse<List<MutualFundTopPicksDomain>, NetworkError>

    suspend fun getCategoryMutualFunds():
            NetworkResponse<List<CategoryMutualFundDomain>, NetworkError>

    suspend fun getMutualFundsBySearch(
        searchId: String
    ): NetworkResponse<List<MutualFundDomain>, NetworkError>


    suspend fun getMutualFundDetails(
        id: String
    ): NetworkResponse<MutualFundDetailsDomain, NetworkError>

    suspend fun getMutualFundGraph(
        id: String
    ): NetworkResponse<MutualFundGraphDomain, NetworkError>
}

