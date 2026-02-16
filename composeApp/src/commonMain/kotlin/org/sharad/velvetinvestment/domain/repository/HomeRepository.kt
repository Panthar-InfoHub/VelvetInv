package org.sharad.velvetinvestment.domain.repository

import org.sharad.velvetinvestment.domain.models.home.FireReportSummaryDomain
import org.sharad.velvetinvestment.domain.models.home.GoalsSummaryDomain
import org.sharad.velvetinvestment.domain.models.home.KYCCompletion
import org.sharad.velvetinvestment.domain.models.home.UserWorthCardDomain

interface HomeRepository {

    suspend fun getUserWorthCard(): UserWorthCardDomain

    suspend fun getFireReportSummary(): FireReportSummaryDomain

    suspend fun getGoalsSummary(): List<GoalsSummaryDomain>

    suspend fun getKycStatus(): KYCCompletion

}