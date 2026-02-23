package org.sharad.velvetinvestment.domain.repository

import org.sharad.velvetinvestment.utils.networking.NetworkError
import org.sharad.velvetinvestment.utils.networking.NetworkResponse
import org.sharad.velvetinvestment.domain.models.explore.FixedDepositTopPicksDomain
import org.sharad.velvetinvestment.domain.models.mutualfunds.MutualFundDetailsDomain
import org.sharad.velvetinvestment.domain.models.portfolio.FDDetailsDomain
import org.sharad.velvetinvestment.presentation.portfolio.models.FDCardData

interface FDRepository {

    suspend fun getFDs():
            NetworkResponse<List<FDCardData>, NetworkError>
    suspend fun getFDDetails(fdId: String): NetworkResponse<FDDetailsDomain, NetworkError>

    suspend fun getFixedDepositTopPicks():
            NetworkResponse<List<FixedDepositTopPicksDomain>, NetworkError>

}
