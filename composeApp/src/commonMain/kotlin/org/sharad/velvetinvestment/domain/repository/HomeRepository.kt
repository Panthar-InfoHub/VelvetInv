package org.sharad.velvetinvestment.domain.repository

import org.sharad.velvetinvestment.domain.models.home.FireReportSummaryDomain
import org.sharad.velvetinvestment.domain.models.home.GoalsSummaryDomain
import org.sharad.velvetinvestment.domain.models.home.KYCCompletion
import org.sharad.velvetinvestment.domain.models.home.UserWorthCardDomain
import org.sharad.velvetinvestment.utils.networking.NetworkError
import org.sharad.velvetinvestment.utils.networking.NetworkResponse

interface HomeRepository {

    suspend fun getUserWorthCard(): NetworkResponse<UserWorthCardDomain, NetworkError>

    suspend fun getFireReportSummary(): NetworkResponse<FireReportSummaryDomain, NetworkError>

    suspend fun getGoalsSummary(): NetworkResponse<List<GoalsSummaryDomain>, NetworkError>

    suspend fun getKycStatus(): NetworkResponse<KYCCompletion, NetworkError>
}