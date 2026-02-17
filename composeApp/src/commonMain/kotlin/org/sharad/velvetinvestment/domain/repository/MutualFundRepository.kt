package org.sharad.velvetinvestment.domain.repository

import com.sharad.surakshakawachneo.utils.Networking.NetworkError
import com.sharad.surakshakawachneo.utils.Networking.NetworkResponse
import org.sharad.velvetinvestment.domain.models.explore.MutualFundTopPicksDomain
import org.sharad.velvetinvestment.domain.models.mutualfunds.CategoryMutualFundDomain
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
}

