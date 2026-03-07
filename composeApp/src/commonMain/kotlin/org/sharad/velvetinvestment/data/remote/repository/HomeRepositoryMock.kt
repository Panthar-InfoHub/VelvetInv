package org.sharad.velvetinvestment.data.remote.repository


import kotlinx.coroutines.delay
import org.sharad.velvetinvestment.domain.GoalTypes
import org.sharad.velvetinvestment.domain.models.home.*
import org.sharad.velvetinvestment.domain.repository.HomeRepository
import org.sharad.velvetinvestment.utils.networking.NetworkError
import org.sharad.velvetinvestment.utils.networking.NetworkResponse

class HomeRepositoryMock : HomeRepository {

    private suspend fun simulateDelay() {
        delay(3_000)
    }

    override suspend fun getUserWorthCard(): NetworkResponse<UserWorthCardDomain, NetworkError> {
        simulateDelay()
        return NetworkResponse.Success(
            UserWorthCardDomain(
                totalNetWorth = 12_50_000L,
                absGrowth = 1_80_000L,
                CARGGrowth = 14.6,
                investingRate = 32.0
            )
        )
    }

    override suspend fun getFireReportSummary(): NetworkResponse<FireReportSummaryDomain, NetworkError> {
        simulateDelay()
        return NetworkResponse.Success(
            FireReportSummaryDomain(
                annualGrowth = 12.5,
                fireNumber = 3_20_00_000.0,
                setupStep = 2
            )
        )
    }

    override suspend fun getGoalsSummary(): NetworkResponse<List<GoalsSummaryDomain>, NetworkError> {
        simulateDelay()
        return NetworkResponse.Success(
            listOf(
                GoalsSummaryDomain(
                    goalTypes = GoalTypes.RETIREMENT,
                    amount = 4_50_000L,
                    targetAmount = 1_00_00_000L,
                    goalId = "1"
                ),
                GoalsSummaryDomain(
                    goalTypes = GoalTypes.WEALTH_BUILDING,
                    amount = 1_20_000L,
                    targetAmount = 3_00_000L,
                    goalId = "2"
                )
            )
        )
    }

    override suspend fun getKycStatus(): NetworkResponse<KYCCompletion, NetworkError> {
        simulateDelay()
        return NetworkResponse.Success(
            KYCCompletion(
                step = 2,
                totalStep = 4,
            )
        )
    }
}
