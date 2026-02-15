package org.sharad.velvetinvestment.domain.repository

import com.sharad.surakshakawachneo.utils.Networking.NetworkError
import com.sharad.surakshakawachneo.utils.Networking.NetworkResponse
import org.sharad.velvetinvestment.presentation.portfolio.models.FundListCardData
import org.sharad.velvetinvestment.presentation.portfolio.models.MutualFundDashBoardData

interface MutualFundRepository {

    suspend fun getMutualFunds():
            NetworkResponse<List<FundListCardData>, NetworkError>

    suspend fun getDashboard():
            NetworkResponse<MutualFundDashBoardData, NetworkError>
}

