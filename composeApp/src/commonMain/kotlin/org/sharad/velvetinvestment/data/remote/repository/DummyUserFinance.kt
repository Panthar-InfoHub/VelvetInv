package org.sharad.velvetinvestment.data.remote.repository

import org.sharad.velvetinvestment.domain.models.fire.FireCombinedDomainModel
import org.sharad.velvetinvestment.domain.models.fire.FireProjections
import org.sharad.velvetinvestment.domain.models.fire.FireReportDomainModel
import org.sharad.velvetinvestment.domain.models.fire.QuarterlyTrendDomain
import org.sharad.velvetinvestment.domain.models.fire.TargetProjection
import org.sharad.velvetinvestment.domain.repository.UserFinance
import org.sharad.velvetinvestment.utils.networking.NetworkError
import org.sharad.velvetinvestment.utils.networking.NetworkResponse

class DummyUserFinance : UserFinance {
    override suspend fun getFireReport():
            NetworkResponse<FireCombinedDomainModel, NetworkError> {

        val emiIncluded = FireReportDomainModel(
            fireNumber = 5_50_00_000,
            fireNumberAnnualGrowth = 11.2,
            fireProgress = 38.0,
            fireProgressThisYear = 4.1,
            yearsToFire = 9,
            yearsToFirePercentage = 32.0,
            currentNetWorth = 2_10_00_000,
            fireInsights = listOf(
                "EMI slows down your FIRE timeline.",
                "Consider prepaying loan to accelerate FIRE.",
                "Debt allocation slightly high."
            ),
            trend = listOf(
                QuarterlyTrendDomain("Q1 2024", 1_40_00_000, 25.0),
                QuarterlyTrendDomain("Q2 2024", 1_55_00_000, 28.0),
                QuarterlyTrendDomain("Q3 2024", 1_75_00_000, 33.0),
                QuarterlyTrendDomain("Q4 2024", 2_10_00_000, 38.0)
            ),
            netWorthGrowth = 14.2,
            progressIncrement = 2.8,
            projectionList = listOf(
                FireProjections(
                    year = 2027,
                    age = 27,
                    firePercent = 45.0,
                    currentPortFolio = 2_80_00_000,
                    netOutflow = 4_50_000,
                    goals = 60_00_000,
                    fireNumber = 5_80_00_000
                ),
                FireProjections(
                    year = 2030,
                    age = 30,
                    firePercent = 75.0,
                    currentPortFolio = 4_50_00_000,
                    netOutflow = 6_00_000,
                    goals = 90_00_000,
                    fireNumber = 6_20_00_000
                )
            ),
            targetProjection = TargetProjection(
                targetYear = 2030,
                projectedPortfolio = 6_20_00_000,
                fireProgress = 75.0
            )
        )

        val emiExcluded = FireReportDomainModel(
            fireNumber = 4_80_00_000,
            fireNumberAnnualGrowth = 12.8,
            fireProgress = 44.0,
            fireProgressThisYear = 5.4,
            yearsToFire = 7,
            yearsToFirePercentage = 40.0,
            currentNetWorth = 2_10_00_000,
            fireInsights = listOf(
                "Without EMI you reach FIRE 2 years earlier.",
                "Investment rate improves significantly.",
                "Equity allocation is optimal."
            ),
            trend = listOf(
                QuarterlyTrendDomain("Q1 2024", 1_50_00_000, 30.0),
                QuarterlyTrendDomain("Q2 2024", 1_70_00_000, 34.0),
                QuarterlyTrendDomain("Q3 2024", 1_95_00_000, 39.0),
                QuarterlyTrendDomain("Q4 2024", 2_10_00_000, 44.0)
            ),
            netWorthGrowth = 16.0,
            progressIncrement = 3.6,
            projectionList = listOf(
                FireProjections(
                    year = 2026,
                    age = 26,
                    firePercent = 55.0,
                    currentPortFolio = 3_00_00_000,
                    netOutflow = 3_50_000,
                    goals = 50_00_000,
                    fireNumber = 5_20_00_000
                ),
                FireProjections(
                    year = 2029,
                    age = 29,
                    firePercent = 100.0,
                    currentPortFolio = 4_80_00_000,
                    netOutflow = 5_00_000,
                    goals = 1_00_00_000,
                    fireNumber = 5_50_00_000
                )
            ),
            targetProjection = TargetProjection(
                targetYear = 2030,
                projectedPortfolio = 6_20_00_000,
                fireProgress = 75.0
            )
        )

        return NetworkResponse.Success(
            FireCombinedDomainModel(
                emiIncluded = emiIncluded,
                emiExcluded = emiExcluded
            )
        )
    }
}