package org.sharad.velvetinvestment.data.remote.repository

import org.sharad.velvetinvestment.domain.models.fixeddeposits.CategoryFixedDepositDomain
import org.sharad.velvetinvestment.domain.models.fixeddeposits.FixedDepositDomain
import org.sharad.velvetinvestment.domain.models.fixeddeposits.FixedDepositTenureDomain
import org.sharad.velvetinvestment.domain.models.fixeddeposits.RiskLevel
import org.sharad.velvetinvestment.domain.models.fixeddeposits.TenureDuration
import org.sharad.velvetinvestment.domain.models.fixeddeposits.TenureRange
import org.sharad.velvetinvestment.domain.repository.FixedDepositRepository
import org.sharad.velvetinvestment.utils.networking.NetworkError
import org.sharad.velvetinvestment.utils.networking.NetworkResponse

class FakeFixedDepositRepository : FixedDepositRepository {

    override suspend fun getTopPickFDs():
            NetworkResponse<List<CategoryFixedDepositDomain>, NetworkError> {

        val topPicks = CategoryFixedDepositDomain(
            categoryName = "Top Picks",
            categorySearchReference = "top_picks",
            fixedDeposits = listOf(
                hdfcFD(),
                iciciFD(),
                sbiFD(),
                kotakFD()
            )
        )

        return NetworkResponse.Success(listOf(topPicks))
    }

    override suspend fun getAllFDs():
            NetworkResponse<List<FixedDepositDomain>, NetworkError> {

        val fds = listOf(
            hdfcFD(),
            iciciFD(),
            sbiFD(),
            kotakFD(),
            axisFD(),
        )

        return NetworkResponse.Success(fds)
    }
}


private fun hdfcFD() = FixedDepositDomain(
    bankName = "HDFC Bank",
    bankLogoUrl = "",
    riskLevel = RiskLevel.LOW,
    baseInterest = 7.5,
    id="1",
    tenures = listOf(

        FixedDepositTenureDomain(
            tenure = TenureRange(TenureDuration(0,0,7), TenureDuration(0,0,29)),
            interestRate = 3.5,
            receiveMin = 10030,
            receiveMax = 10030
        ),

        FixedDepositTenureDomain(
            tenure = TenureRange(TenureDuration(0,1,0), TenureDuration(0,5,29)),
            interestRate = 4.5,
            receiveMin = 10220,
            receiveMax = 10220
        ),

        FixedDepositTenureDomain(
            tenure = TenureRange(TenureDuration(0,6,0), TenureDuration(0,11,29)),
            interestRate = 5.75,
            receiveMin = 10580,
            receiveMax = 10580
        ),

        FixedDepositTenureDomain(
            tenure = TenureRange(TenureDuration(1,0,0), TenureDuration(2,0,0)),
            interestRate = 6.6,
            receiveMin = 11030,
            receiveMax = 11030
        ),

        FixedDepositTenureDomain(
            tenure = TenureRange(TenureDuration(4,6,30), TenureDuration(5,0,0)),
            interestRate = 7.5,
            receiveMin = 13590,
            receiveMax = 13590
        )
    )
)

private fun iciciFD() = FixedDepositDomain(
    bankName = "ICICI Bank",
    bankLogoUrl = "",
    id="2",
    riskLevel = RiskLevel.LOW,
    baseInterest = 7.25,
    tenures = listOf(

        FixedDepositTenureDomain(
            tenure = TenureRange(TenureDuration(0,0,7), TenureDuration(0,0,29)),
            interestRate = 3.4,
            receiveMin = 10028,
            receiveMax = 10028
        ),

        FixedDepositTenureDomain(
            tenure = TenureRange(TenureDuration(0,1,0), TenureDuration(0,5,29)),
            interestRate = 4.4,
            receiveMin = 10200,
            receiveMax = 10200
        ),

        FixedDepositTenureDomain(
            tenure = TenureRange(TenureDuration(0,6,0), TenureDuration(0,11,29)),
            interestRate = 5.6,
            receiveMin = 10560,
            receiveMax = 10560
        ),

        FixedDepositTenureDomain(
            tenure = TenureRange(TenureDuration(1,0,0), TenureDuration(2,0,0)),
            interestRate = 6.8,
            receiveMin = 11150,
            receiveMax = 11150
        ),

        FixedDepositTenureDomain(
            tenure = TenureRange(TenureDuration(2,0,0), TenureDuration(3,0,0)),
            interestRate = 7.25,
            receiveMin = 12500,
            receiveMax = 12500
        )
    )
)
private fun sbiFD() = FixedDepositDomain(
    bankName = "SBI Bank",
    bankLogoUrl = "",
    id="3",
    riskLevel = RiskLevel.LOW,
    baseInterest = 7.1,
    tenures = listOf(

        FixedDepositTenureDomain(
            tenure = TenureRange(TenureDuration(0,0,7), TenureDuration(0,0,45)),
            interestRate = 3.3,
            receiveMin = 10025,
            receiveMax = 10025
        ),

        FixedDepositTenureDomain(
            tenure = TenureRange(TenureDuration(0,2,0), TenureDuration(0,5,29)),
            interestRate = 4.2,
            receiveMin = 10180,
            receiveMax = 10180
        ),

        FixedDepositTenureDomain(
            tenure = TenureRange(TenureDuration(0,6,0), TenureDuration(0,11,29)),
            interestRate = 5.5,
            receiveMin = 10540,
            receiveMax = 10540
        ),

        FixedDepositTenureDomain(
            tenure = TenureRange(TenureDuration(1,0,0), TenureDuration(2,0,0)),
            interestRate = 6.7,
            receiveMin = 11100,
            receiveMax = 11100
        ),

        FixedDepositTenureDomain(
            tenure = TenureRange(TenureDuration(3,0,0), TenureDuration(5,0,0)),
            interestRate = 7.1,
            receiveMin = 13000,
            receiveMax = 13000
        )
    )
)
private fun kotakFD() = FixedDepositDomain(
    bankName = "Kotak Bank",
    bankLogoUrl = "",
    id="4",
    riskLevel = RiskLevel.LOW,
    baseInterest = 7.4,
    tenures = listOf(

        FixedDepositTenureDomain(
            tenure = TenureRange(TenureDuration(0,0,7), TenureDuration(0,0,29)),
            interestRate = 3.5,
            receiveMin = 10030,
            receiveMax = 10030
        ),

        FixedDepositTenureDomain(
            tenure = TenureRange(TenureDuration(0,1,0), TenureDuration(0,5,29)),
            interestRate = 4.6,
            receiveMin = 10230,
            receiveMax = 10230
        ),

        FixedDepositTenureDomain(
            tenure = TenureRange(TenureDuration(0,6,0), TenureDuration(0,11,29)),
            interestRate = 5.9,
            receiveMin = 10610,
            receiveMax = 10610
        ),

        FixedDepositTenureDomain(
            tenure = TenureRange(TenureDuration(1,0,0), TenureDuration(2,0,0)),
            interestRate = 6.9,
            receiveMin = 11200,
            receiveMax = 11200
        ),

        FixedDepositTenureDomain(
            tenure = TenureRange(TenureDuration(3,0,0), TenureDuration(5,0,0)),
            interestRate = 7.4,
            receiveMin = 13200,
            receiveMax = 13200
        )
    )
)
private fun axisFD() = FixedDepositDomain(
    bankName = "Axis Bank",
    bankLogoUrl = "",
    riskLevel = RiskLevel.LOW,
    id="5",
    baseInterest = 7.2,
    tenures = listOf(

        FixedDepositTenureDomain(
            tenure = TenureRange(TenureDuration(0,0,7), TenureDuration(0,0,30)),
            interestRate = 3.5,
            receiveMin = 10030,
            receiveMax = 10030
        ),

        FixedDepositTenureDomain(
            tenure = TenureRange(TenureDuration(0,1,0), TenureDuration(0,5,29)),
            interestRate = 4.4,
            receiveMin = 10200,
            receiveMax = 10200
        ),

        FixedDepositTenureDomain(
            tenure = TenureRange(TenureDuration(0,6,0), TenureDuration(0,11,29)),
            interestRate = 5.8,
            receiveMin = 10590,
            receiveMax = 10590
        ),

        FixedDepositTenureDomain(
            tenure = TenureRange(TenureDuration(1,0,0), TenureDuration(2,0,0)),
            interestRate = 6.8,
            receiveMin = 11130,
            receiveMax = 11130
        ),

        FixedDepositTenureDomain(
            tenure = TenureRange(TenureDuration(3,0,0), TenureDuration(5,0,0)),
            interestRate = 7.2,
            receiveMin = 13100,
            receiveMax = 13100
        )
    )
)