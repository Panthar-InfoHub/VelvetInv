package org.sharad.velvetinvestment.data.remote.repository

import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import org.sharad.velvetinvestment.utils.networking.NetworkError
import org.sharad.velvetinvestment.utils.networking.NetworkResponse
import kotlinx.coroutines.delay
import org.sharad.velvetinvestment.domain.models.explore.MutualFundTopPicksDomain
import org.sharad.velvetinvestment.domain.models.mutualfunds.AssetsAllocationDomain
import org.sharad.velvetinvestment.domain.models.mutualfunds.CategoryMutualFundDomain
import org.sharad.velvetinvestment.domain.models.mutualfunds.MutualFundDetailsDomain
import org.sharad.velvetinvestment.domain.models.mutualfunds.MutualFundDomain
import org.sharad.velvetinvestment.domain.models.mutualfunds.MutualFundGraphDomain
import org.sharad.velvetinvestment.domain.models.mutualfunds.MutualFundGraphPointsDomain
import org.sharad.velvetinvestment.domain.repository.MutualFundRepository
import org.sharad.velvetinvestment.presentation.portfolio.models.FundListCardData
import org.sharad.velvetinvestment.presentation.portfolio.models.MutualFundDashBoardData

class DummyMutualFundRepository : MutualFundRepository {

    override suspend fun getMutualFunds():
            NetworkResponse<List<FundListCardData>, NetworkError> {

        return try {
            val data = listOf(
                FundListCardData(
                    id = "mf1",
                    icon = "ic_hdfc",
                    fundName = "HDFC Flexi Cap Fund",
                    fundCategory = "Equity",
                    amount = "₹1.5L",
                    fundRemark = "Next due on 05 Feb 2026",
                    fundType = "SIP"
                ),
                FundListCardData(
                    id = "mf2",
                    icon = "ic_sbi",
                    fundName = "SBI Bluechip Fund",
                    fundCategory = "Large Cap",
                    amount = "₹2.2L",
                ),
                FundListCardData(
                    id = "mf3",
                    icon = "ic_sbi",
                    fundName = "SBI Bluechip Fund",
                    fundCategory = "Large Cap",
                    amount = "₹2.2L",
                ),
                FundListCardData(
                    id = "mf4",
                    icon = "ic_hdfc",
                    fundName = "HDFC Flexi Cap Fund",
                    fundCategory = "Equity",
                    amount = "₹1.5L",
                    fundRemark = "Next due on 05 Feb 2026",
                    fundType = "SIP"
                ),
                FundListCardData(
                    id = "mf5",
                    icon = "ic_hdfc",
                    fundName = "HDFC Flexi Cap Fund",
                    fundCategory = "Equity",
                    amount = "₹1.5L",
                    fundRemark = "Next due on 05 Feb 2026",
                    fundType = "SIP"
                ),
                FundListCardData(
                    id = "mf6",
                    icon = "ic_sbi",
                    fundName = "SBI Bluechip Fund",
                    fundCategory = "Large Cap",
                    amount = "₹2.2L",
                )
            )

            NetworkResponse.Success(data)

        } catch (e: Exception) {
            NetworkResponse.Error(NetworkError.UNKNOWN)
        }
    }

    override suspend fun getDashboard():
            NetworkResponse<MutualFundDashBoardData, NetworkError> {

        return try {

            val investedAmount = 300_000L
            val currentValue = 350_000L
            val oneDayReturns = 2_500L

            val totalReturns = currentValue - investedAmount

            val totalReturnsPercentage =
                if (investedAmount != 0L)
                    (totalReturns.toDouble() / investedAmount) * 100
                else 0.0

            val oneDayReturnsPercentage =
                if (currentValue != 0L)
                    (oneDayReturns.toDouble() / currentValue) * 100
                else 0.0

            val dashboard = MutualFundDashBoardData(
                total = 2,
                currentValue = currentValue,
                investedAmount = investedAmount,
                totalReturns = totalReturns,
                totalReturnsPercentage = totalReturnsPercentage,
                oneDayReturns = oneDayReturns,
                oneDayReturnsPercentage = oneDayReturnsPercentage
            )

            NetworkResponse.Success(dashboard)

        } catch (e: Exception) {
            NetworkResponse.Error(NetworkError.UNKNOWN)
        }
    }

    override suspend fun getMutualFundTopPicks():
            NetworkResponse<List<MutualFundTopPicksDomain>, NetworkError> {

        val data = listOf(
            MutualFundTopPicksDomain(
                icon = "",
                name = "Axis Bluechip Fund",
                metadata = "Large Cap • Moderate Risk",
                returnYears = 3,
                percentage = 18.5,
                id="dbusdbihsdb123"
            ),
            MutualFundTopPicksDomain(
                icon = "",
                name = "Parag Parikh Flexi Cap",
                metadata = "Flexi Cap • Moderate Risk",
                returnYears = 5,
                percentage = 21.2,
                id="321dscbysgacvhuscb"
            )
        )

        return NetworkResponse.Success(data)
    }

    override suspend fun getCategoryMutualFunds():
            NetworkResponse<List<CategoryMutualFundDomain>, NetworkError> {

        return try {

            delay(1200)

            NetworkResponse.Success(
                generateFakeCategories()
            )

        } catch (e: Exception) {
            NetworkResponse.Error(NetworkError.UNKNOWN)
        }
    }

    override suspend fun getMutualFundsBySearch(
        searchId: String
    ): NetworkResponse<List<MutualFundDomain>, NetworkError> {

        return try {

            delay(1000)

            val fakeList = generateFakeFunds(searchId)

            NetworkResponse.Success(fakeList)

        } catch (e: Exception) {
            NetworkResponse.Error(NetworkError.UNKNOWN)
        }
    }


    override suspend fun getMutualFundDetails(
        id: String
    ): NetworkResponse<MutualFundDetailsDomain, NetworkError> {

        val details = MutualFundDetailsDomain(
            id = id,
            name = "Axis Bluechip Fund",
            icon = "",
            category = "Large Cap",
            amount = "₹25,000",
            risk = "Moderate",
            rating = 4,
            returnYear = 5,
            type = "Growth",
            percentage = 12.45,
            oneDayPercentage = 0.38,
            today = "2026-02-18T00:00:00Z",
            todayNav = 45.67,
            minAmount = 500,
            fundSize = 1_500_000_000,
            assets = AssetsAllocationDomain(
                equity = 76.5,
                debt = 18.2,
                cash = 5.3
            ),
            topFunds = listOf(
                MutualFundDomain(
                    id = "2",
                    name = "SBI Bluechip Fund",
                    icon = "",
                    category = "Large Cap",
                    amount = "₹20,000",
                    remark = "Direct Plan",
                    rating = 5,
                    returnYear = 5,
                    type = "Growth",
                    percentage = 13.4
                ),
                MutualFundDomain(
                    id = "3",
                    name = "ICICI Prudential Bluechip",
                    icon = "",
                    category = "Large Cap",
                    amount = "₹15,000",
                    remark = "Regular Plan",
                    rating = 4,
                    returnYear = 5,
                    type = "Growth",
                    percentage = 12.1
                )
            )
        )

        return NetworkResponse.Success(details)
    }

    override suspend fun getMutualFundGraph(
        id: String
    ): NetworkResponse<MutualFundGraphDomain, NetworkError> {

        val graph = MutualFundGraphDomain(
            graphPoints = listOf(
                MutualFundGraphPointsDomain(38.20, "2025-02-01T00:00:00Z"),
                MutualFundGraphPointsDomain(39.10, "2025-04-01T00:00:00Z"),
                MutualFundGraphPointsDomain(40.35, "2025-06-01T00:00:00Z"),
                MutualFundGraphPointsDomain(41.80, "2025-08-01T00:00:00Z"),
                MutualFundGraphPointsDomain(43.25, "2025-10-01T00:00:00Z"),
                MutualFundGraphPointsDomain(44.10, "2025-12-01T00:00:00Z"),
                MutualFundGraphPointsDomain(45.67, "2026-02-01T00:00:00Z")
            )
        )

        return NetworkResponse.Success(graph)
    }
}


private fun generateFakeFunds(
    searchId: String
): List<MutualFundDomain> {

    return List(10) { index ->

        MutualFundDomain(
            id = "${searchId.capitalize(Locale.current)}-$index",
            name = "$searchId Fund ${index + 1}",
            icon = "",
            category = searchId,
            amount = "₹${(5000..50000).random()}",
            remark = if (index % 3 == 0) "Top Performer" else null,
            rating = (3..5).random(),
            returnYear = (1..5).random(),
            type = "Equity",
            percentage = (5..25).random().toDouble()
        )
    }
}



private fun generateFakeCategories():
        List<CategoryMutualFundDomain> {

    return listOf(

        CategoryMutualFundDomain(
            categoryName = "Large Cap Funds",
            categorySearchReference = "large_cap",
            mutualFunds = generateFunds(
                category = "Large Cap",
                type = "Equity"
            )
        ),

        CategoryMutualFundDomain(
            categoryName = "Mid Cap Funds",
            categorySearchReference = "mid_cap",
            mutualFunds = generateFunds(
                category = "Mid Cap",
                type = "Equity"
            )
        ),

        CategoryMutualFundDomain(
            categoryName = "Debt Funds",
            categorySearchReference = "debt",
            mutualFunds = generateFunds(
                category = "Debt",
                type = "Debt"
            )
        )

    )
}

private fun generateFunds(
    category: String,
    type: String
): List<MutualFundDomain> {

    return List(5) { index ->

        MutualFundDomain(
            id = "$category-$index",
            name = "$category Fund ${index + 1}",
            icon = "",
            category = category,
            amount = "₹${(5000..50000).random()}",
            remark = if (index % 2 == 0) "Top Performer" else null,
            rating = (3..5).random(),
            returnYear = (8..18).random(),
            type = type,
            percentage = (-10..20).random().toDouble()

        )
    }
}