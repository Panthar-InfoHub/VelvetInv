package org.sharad.velvetinvestment.data.remote.repository

import com.sharad.surakshakawachneo.utils.Networking.NetworkError
import com.sharad.surakshakawachneo.utils.Networking.NetworkResponse
import kotlinx.coroutines.delay
import org.sharad.velvetinvestment.domain.models.explore.MutualFundTopPicksDomain
import org.sharad.velvetinvestment.domain.models.mutualfunds.CategoryMutualFundDomain
import org.sharad.velvetinvestment.domain.models.mutualfunds.MutualFundDomain
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