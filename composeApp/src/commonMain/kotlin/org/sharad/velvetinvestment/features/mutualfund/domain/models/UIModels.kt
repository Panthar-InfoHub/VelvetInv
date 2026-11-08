package org.sharad.velvetinvestment.features.mutualfund.domain.models

import org.sharad.velvetinvestment.features.cart.data.remote.model.cartaddsip.AddCartSipRequest
import org.sharad.velvetinvestment.features.core.domain.models.PurchaseMode

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
    /**
     * The sheet's three-way switch (daily / monthly / one-time). Opens on the mode picked
     * on the investment-method screen; the user can change it here. Velvet-only — JN's sheet is
     * stuck on that earlier pick.
     */
    val purchaseMode: PurchaseMode = PurchaseMode.MONTHLY,
    /** Modes the fund can be bought in, in switch order. */
    val availableModes: List<PurchaseMode> = listOf(PurchaseMode.DAILY, PurchaseMode.MONTHLY, PurchaseMode.ONE_TIME),
    val minDailySipAmount: Long = 10,
    val selectedType: MFPurchaseTypes= MFPurchaseTypes.LUMP_SUM,
    val amount:Long?=null,
    val minLumpSumAmount:Long = 500,
    val minSipAmount:Long = 500,
    val loading:Boolean=false,
    val selectedFrequency: InvestmentFrequency?=null,
    val selectedSIPDate:String?=null,
    val selectedDuration: Duration?=null,
    val frequencyDropDownExpanded:Boolean=false,
    val dayDropDownExpanded:Boolean=false,
    val durationDropDownExpanded:Boolean=false
) {
    /** The minimum for the selected mode. */
    val minAmount: Long
        get() = when (purchaseMode) {
            PurchaseMode.DAILY -> minDailySipAmount
            PurchaseMode.MONTHLY -> minSipAmount
            PurchaseMode.ONE_TIME -> minLumpSumAmount
        }
}

enum class MFPurchaseTypes{
    LUMP_SUM,SIP
}

enum class Duration(
    val label: String,
    val months: Int // null = perpetual
) {
    PERPETUAL("Perpetual (Until Cancelled)", 0),

    SIX_MONTHS("6 Months", 6),
    ONE_YEAR("1 Year", 12),
    TWO_YEARS("2 Years", 24),
    THREE_YEARS("3 Years", 36),
    FIVE_YEARS("5 Years", 60),
    TEN_YEARS("10 Years", 120);
}


/**
 * The `POST /mf/cart` SIP body for the sheet's current input, or null while it is incomplete.
 * A daily SIP has no installment day; anything else is sent as a monthly SIP on the picked date.
 * Daily is the sheet's [CartBottomSheetState.purchaseMode] (Velvet's switch) or JN's frequency pick.
 */
fun CartBottomSheetState.toSipRequest(productId: String): AddCartSipRequest? {
    val amount = amount ?: return null
    val isDaily = purchaseMode == PurchaseMode.DAILY ||
            selectedFrequency == InvestmentFrequency.DAILY ||
            selectedFrequency == InvestmentFrequency.DAILY_Z

    if (isDaily) {
        return AddCartSipRequest(
            mf_product_id = productId,
            amount = amount,
            frequency = "DAILY"
        )
    }

    val day = selectedSIPDate?.toIntOrNull() ?: return null
    return AddCartSipRequest(
        mf_product_id = productId,
        amount = amount,
        frequency = "MONTHLY",
        installment_day = day
    )
}
