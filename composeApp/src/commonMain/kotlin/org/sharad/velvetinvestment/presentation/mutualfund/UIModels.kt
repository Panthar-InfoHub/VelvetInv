package org.sharad.velvetinvestment.presentation.mutualfund

import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import org.sharad.velvetinvestment.data.remote.model.cartaddsip.AddCartSipRequest
import org.sharad.velvetinvestment.domain.models.mutualfunds.InvestmentFrequency
import org.sharad.velvetinvestment.domain.models.mutualfunds.MutualFundDetailsDomain
import org.sharad.velvetinvestment.domain.models.mutualfunds.MutualFundDomain
import org.sharad.velvetinvestment.domain.models.mutualfunds.MutualFundGraphDomain
import org.sharad.velvetinvestment.domain.models.mutualfunds.MutualFundGraphPointsDomain
import kotlin.time.Clock

data class CategoryMutualFundDomain(
    val categoryName: String,
    val categorySearchReference: String,
    val mutualFunds: List<MutualFundDomain>
)



sealed interface DetailsState {
    data object Loading : DetailsState
    data class Success(val data: MutualFundDetailsDomain) : DetailsState
    data class Error(val error: String) : DetailsState
}

sealed interface GraphState {
    data object Loading : GraphState
    data class Success(val data: MutualFundGraphDomain) : GraphState
    data class Error(val error: String) : GraphState
}

data class MutualFundScreenState(
    val detailsState: DetailsState=DetailsState.Loading,
    val graphState: GraphState=GraphState.Loading,
    val chartPoints: List<MutualFundGraphPointsDomain> = emptyList()
)

data class StableMetricUi(
    val label: String,
    val value: Double
)

data class CalculatorInputState(
    val isSip: Boolean = true,
    val monthlyInvestment: Long = 5000,
    val timeInYears: Int = 5
)

data class CartBottomSheetState(
    val selectedType: MFPurchaseTypes= MFPurchaseTypes.LUMP_SUM,
    val amount:Long?=null,
    val minAmount:Long=500,
    val loading:Boolean=false,
    val selectedFrequency: InvestmentFrequency?=null,
    val selectedSIPDate:String?=null,
    val selectedDuration: Duration?=null,
    val frequencyDropDownExpanded:Boolean=false,
    val dayDropDownExpanded:Boolean=false,
    val durationDropDownExpanded:Boolean=false
)

enum class MFPurchaseTypes{
    LUMP_SUM,SIP
}

enum class Duration(
    val label: String,
    val months: Int // null = perpetual
) {
    PERPETUAL("Perpetual (Until 2099)", 0),

    SIX_MONTHS("6 Months", 6),
    ONE_YEAR("1 Year", 12),
    TWO_YEARS("2 Years", 24),
    THREE_YEARS("3 Years", 36),
    FIVE_YEARS("5 Years", 60),
    TEN_YEARS("10 Years", 120);
}


fun CartBottomSheetState.toSipRequest(
    productId: String
): AddCartSipRequest? {

    val amount = amount ?: return null
    val frequency = selectedFrequency ?: return null
    val duration = selectedDuration ?: return null
    val day = selectedSIPDate?.toIntOrNull() ?: return null


    if (amount < minAmount) return null

    val today = Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault())
        .date

    val startDate = today.plus(DatePeriod(days = 31))

    val maxEndDate = LocalDate(2099, 12, 31)

    val endDate = if (duration == Duration.PERPETUAL) {
        maxEndDate
    } else {
        val calculated = startDate.plus(
            DatePeriod(months = duration.months)
        )
        if (calculated > maxEndDate) maxEndDate else calculated
    }

    return AddCartSipRequest(
        amount = amount,
        mf_product_id = productId,
        sip_st_date = startDate.toString(),
        sip_en_date = endDate.toString(),
        sip_freq = frequency.code,
        sip_day = day,
        sip_amt = amount
    )
}