package org.sharad.velvetinvestment.data.remote.repository

import com.sharad.surakshakawachneo.utils.Networking.NetworkError
import com.sharad.surakshakawachneo.utils.Networking.NetworkResponse
import org.sharad.velvetinvestment.domain.models.explore.FixedDepositTopPicksDomain
import org.sharad.velvetinvestment.domain.models.portfolio.FDBankInfoDomain
import org.sharad.velvetinvestment.domain.models.portfolio.FDDetailsDomain
import org.sharad.velvetinvestment.domain.models.portfolio.FDInvestmentDetailsDomain
import org.sharad.velvetinvestment.domain.models.portfolio.FDNomineeDomain
import org.sharad.velvetinvestment.domain.models.portfolio.FDTimelineDomain
import org.sharad.velvetinvestment.domain.repository.FDRepository
import org.sharad.velvetinvestment.presentation.portfolio.models.FDCardData
import kotlin.time.Clock
import kotlin.time.Instant

class DummyFDRepository : FDRepository {

    override suspend fun getFDs():
            NetworkResponse<List<FDCardData>, NetworkError> {

        return try {

            val data = listOf(
                FDCardData(
                    id = "fd1",
                    icon = "ic_hdfc_bank",
                    fundName = "HDFC Bank FD",
                    fundNumber = "FD123456",
                    principle = 100000,
                    rate = 7.2,
                    maturity = 1700000000
                ),
                FDCardData(
                    id = "fd2",
                    icon = "ic_sbi_bank",
                    fundName = "SBI FD",
                    fundNumber = "FD654321",
                    principle = 200000,
                    rate = 6.8,
                    maturity = 1710000000
                )
            )

            NetworkResponse.Success(data)

        } catch (e: Exception) {
            NetworkResponse.Error(NetworkError.UNKNOWN)
        }
    }

    override suspend fun getFDDetails(
        fdId: String
    ): NetworkResponse<FDDetailsDomain, NetworkError> {

        return try {

            val startDate = Instant.parse("2025-02-01T00:00:00Z")
            val maturityDate = Instant.parse("2026-02-01T00:00:00Z")

            val now = Clock.System.now()

            val daysRemaining =
                ((maturityDate.epochSeconds - now.epochSeconds) / (60 * 60 * 24))
                    .toInt()
                    .coerceAtLeast(0)

            val data = FDDetailsDomain(
                bankInfo = FDBankInfoDomain(
                    bankName = "HDFC Bank",
                    fdAccountNumber = "FD123456789"
                ),

                investmentDetails = FDInvestmentDetailsDomain(
                    principalAmount = 200000,
                    interestRate = 7.80,
                    tenureMonths = 12,
                    maturityAmount = 215800,
                    interestEarnedTillDate = 4275
                ),

                nomineeDetails = listOf(
                    FDNomineeDomain(
                        fullName = "Amit Kumar",
                        relationship = "Brother",
                        dateOfBirth = "1995-01-06T00:00:00Z",
                        id="NonNon1"
                    )
                ),

                timelineDetails = FDTimelineDomain(
                    startDate = startDate.toString(),        // ISO UTC
                    maturityDate = maturityDate.toString(),  // ISO UTC
                    daysRemaining = daysRemaining
                )
            )

            NetworkResponse.Success(data)

        } catch (e: Exception) {
            NetworkResponse.Error(NetworkError.UNKNOWN)
        }
    }

    override suspend fun getFixedDepositTopPicks():
            NetworkResponse<List<FixedDepositTopPicksDomain>, NetworkError> {

        val data = listOf(
            FixedDepositTopPicksDomain(
                icon = "https://icon-url.com/bank1.png",
                name = "HDFC Bank FD",
                metadata = "Senior Citizen • 1Y",
                returnYears = "1",
                percentage = 7.25,
                id="NonNon1"
            ),
            FixedDepositTopPicksDomain(
                icon = "https://icon-url.com/bank2.png",
                name = "ICICI Bank FD",
                metadata = "Regular • 2Y",
                returnYears = "2",
                percentage = 7.10,
                id="NonNon2"
            )
        )

        return NetworkResponse.Success(data)
    }
}

