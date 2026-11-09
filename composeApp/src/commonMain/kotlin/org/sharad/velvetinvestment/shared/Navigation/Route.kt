package org.sharad.velvetinvestment.shared.Navigation

import kotlinx.serialization.Serializable
import org.sharad.velvetinvestment.features.auth.ui.viewmodels.EnterPinPurpose
import org.sharad.velvetinvestment.domain.webview.WebViewUrlMatchType

object Route {

    @Serializable
    data object SplashScreen

    @Serializable
    data object LoginScreen
    @Serializable
    data object OtpScreen

    @Serializable
    data object LoginGraph

    @Serializable
    data object MainAppGraph

    @Serializable
    data class OnboardingGraph(
        /**
         * True only when opened from inside the main app to finish KYC: the home screen behind it
         * is still showing the old status, so finishing the flow asks it to reload.
         */
        val shouldRefresh: Boolean = false
    )

    @Serializable
    data object BottomNav
    @Serializable
    data object Home
    @Serializable
    data object FundScreener
    @Serializable
    data object PortFolio
    @Serializable
    data object Insurance

    @Serializable
    data object HealthInsurance
    @Serializable
    data object TermInsurance
    @Serializable
    data object OtherInsurance

    /**
     * The order-details screen, for a SIP holding and a lumpsum one alike. Everything it renders
     * rides on the route: it is opened from the portfolio, which already has the numbers.
     */
    @Serializable
    data class SIPPortfolioDetails(
        val id: Int,
        /** `mf_holding_id` — what the redemption endpoints are keyed on. */
        val holdingId: String = "",
        val title: String,
        val category: String,
        val amount: Double,
        val isSip: Boolean,
        val startDate: String,
        val returnPercentage: String,
        val returnAmount: Double,
        val xirr: String,
        val currentNav: Double,
        val avgNav: Double,
        val folio: String,
        val balanceUnits: Double,
        val img_url: String? = "",
        val orderId: String,
        val actualFolio: String,
        /** Falls back to invested + return when the caller has no separate figure. */
        val currentValue: Double = 0.0,
        /**
         * Today's movement. The portfolio payload reports it for the whole mutual-fund book but
         * not per holding, so these read zero until it does.
         */
        val dayReturn: Double = 0.0,
        val dayReturnPercent: Double = 0.0,
        /** The badge on the header card. */
        val status: String = "ACTIVE"
    )

    /**
     * The redeem screen. Everything it needs travels on the route — it is reached from the order
     * details, which already holds the figures, so it loads nothing of its own.
     */
    @Serializable
    data class Redeem(
        val holdingId: String,
        val scheme: String,
        val folioNumber: String,
        val availableUnits: Double,
        val currentValue: Double,
        val isSip: Boolean
    )

    /**
     * Confirms a redemption that is already placed with the gateway. [redemptionId] is the
     * `fp_id` from `POST /mf/redemption/`.
     */
    @Serializable
    data class RedeemOtp(val redemptionId: String)
    @Serializable
    data class FDPortfolioDetailsScreen(val id:String)

    @Serializable
    data object CategoryMutualFund
    /**
     * The fund list. [tag], [category] and [amountType] are the `GET /mf/funds` filters the
     * screen opens with — set when it is entered from a category tile, and null when the user is
     * browsing everything. (JN's home micro-SIP cards also set [amountType]; Velvet has none.)
     */
    @Serializable
    data class MutualFundSearchResult(
        val search: String = "",
        val tag: String? = null,
        val category: String? = null,
        val amountType: String? = null
    )

    /**
     * A fund's details. [isin] rides along because every purchase call after this screen is keyed
     * on it; null when the caller doesn't have it (the details call supplies it then).
     * [folioId] is only set from a portfolio folio — TODO(top-up): unused while top-up is parked.
     */
    @Serializable
    data class MutualFundDetails(val id: String, val isin: String? = null, val folioId: String? = null)

    @Serializable
    data object KYCScreen
    @Serializable
    data object KYCFormScreen
    @Serializable
    data class KYCImageUploadScreen(val name:String)
    @Serializable
    data class KYCContractScreen(val name:String)

    @Serializable
    data object GoalsScreen

    @Serializable
    data object LoansFlowScreen

    @Serializable
    data object LoansScreen

    @Serializable
    data class AddLoanScreen(val loanId: String? = null)

    @Serializable
    data object Profile

    /**
     * PIN entry. Shown once per cold start as the app lock, and again — with [purpose] set to
     * [EnterPinPurpose.CHANGE_PIN] — in front of [ChangePin], which is why it carries a purpose
     * rather than being a single fixed destination.
     */
    @Serializable
    data class EnterPin(val purpose: String = EnterPinPurpose.APP_LOCK)

    /** Sets a new PIN. Entered through [EnterPin] with the change-PIN purpose once a PIN exists. */
    @Serializable
    data object ChangePin

    /** The switch controlling whether the app lock offers the biometric shortcut. */
    @Serializable
    data object BiometricLogin
    @Serializable
    data object Notifications
    @Serializable
    data object PersonalInformation
    @Serializable
    data object PrivacyPolicy
    @Serializable
    data object TermsAndConditions
    @Serializable
    data object AboutUs
    @Serializable
    data object AboutVelvet
    @Serializable
    data object AboutFire

    @Serializable
    data object FixedDepositCategory
    @Serializable
    data class FixedDepositSearchResult(val search: String?=null)
    @Serializable
    data class FixedDepositDetails(val id:String)
    @Serializable
    data class PurchaseFixedDeposit(val id:String)
    @Serializable
    data object CartScreen
    /**
     * Confirms the cart's SIP or lumpsum checkout; it shares the cart's view model, which holds
     * the batch.
     */
    @Serializable
    data object CartCheckoutOtp

    @Serializable
    data class FolioFundScreen(val folioId: String, val actualFolio: String)

    // TODO(top-up): the top-up screens' routes; their screens are parked in composeApp/later/.
    // @Serializable
    // data object ExistingFundScreen
    // @Serializable
    // data object ExistingFundLumpSumScreen

    @Serializable
    data object SingleGoalAdd

    @Serializable
    data object MutualFundTypeSelectionScreen
    @Serializable
    data class GoalProjectionFlow(val id: String)
    /** The holdings mapped to goal [id], and the sheet that maps more. */
    @Serializable
    data class MapSchemes(val id: String)
    @Serializable
    data object AllBundleScreen
    @Serializable
    data object InvestmentRateScreen

    @Serializable
    data class WebViewScreen(
        val url: String,
        val exitUrlPatterns: List<String> = emptyList(),
        val matchType: String = WebViewUrlMatchType.CONTAINS.name,
        val title: String? = null,
        val completionRouteKey: String? = null,
        val interceptExternalAppUrls: Boolean = true
    )

    // Orders (ported from Janta Nivesh)
    @Serializable
    data object MyOrders

    /**
     * One order, opened from the listing.
     *
     * There is no per-order endpoint, so everything the screen renders rides on the route — the
     * listing already holds it. The figures are non-null with zero defaults because the
     * navigation argument types have no nullable primitives; zero therefore stands for "the
     * payload did not report this", which is exactly what the screen shows as "--".
     */
    @Serializable
    data class OrderDetails(
        /** An [org.sharad.velvetinvestment.features.orders.domain.model.OrderPlanType] name. */
        val planType: String = "",
        /** An [org.sharad.velvetinvestment.features.orders.domain.model.OrderState] name. */
        val state: String = "",
        val stateLabel: String = "",
        val systematic: Boolean = false,
        val fundName: String = "",
        val fundIconUrl: String = "",
        val fundCategory: String = "",
        /** Product identity, so a failed order can be placed again from the details screen. */
        val mfProductId: String = "",
        val isin: String = "",
        val folioNumber: String = "",
        val orderId: String = "",
        val transactionId: String = "",
        val amount: Double = 0.0,
        val units: Double = 0.0,
        val allottedUnits: Double = 0.0,
        val purchasedAmount: Double = 0.0,
        val purchasedPrice: Double = 0.0,
        val latestNav: Double = 0.0,
        val paymentMethod: String = "",
        val frequency: String = "",
        val reason: String = "",
        val createdAt: String = "",
        val submittedAt: String = "",
        val succeededAt: String = "",
        val failedAt: String = "",
        val allottedNavDate: String = "",
        /**
         * The account a redemption pays out to. The order payload does not resolve it, so this
         * is blank today and the payout card is simply left off until it is filled.
         */
        val payoutAccount: String = ""
    )

    // Plans flow (ported from Janta Nivesh)

    /**
     * Everything shown on the success screen, carried from the confirm response. [mode] is a
     * [org.sharad.velvetinvestment.features.core.domain.models.PurchaseMode] name, and
     * decides whether the screen reads as a SIP registration or a completed purchase.
     * [installmentDay] is zero when the mode has no debit day.
     */
    @Serializable
    data class PurchaseSuccess(
        val mode: String,
        val schemeName: String,
        val amount: String,
        val installmentDay: Int,
        val startDate: String
    )

    /**
     * The buy screen. [mfProductId] is what the purchase endpoints take, while [isin] is what
     * the scheme lookup — and so every threshold on the screen — is keyed on. The two name
     * fields ride along so the header reads correctly before that lookup lands.
     * [purchaseMode] (a `PurchaseMode` name) is the mode picked on the investment-method screen;
     * the screen opens on it. (Velvet addition — JN opens on monthly.)
     */
    @Serializable
    data class FundPurchase(
        val mfProductId: String,
        val isin: String,
        val fundName: String = "",
        val fundSubtitle: String = "",
        val amountType: String? = null,
        val purchaseMode: String = "MONTHLY"
    )

    /**
     * Autopay setup reached from outside onboarding — from the purchase screen, when the user
     * has no approved mandate to debit a SIP against. Same screen, different exit.
     */
    @Serializable
    data object AddMandate

    /**
     * Runs a SIP's unattended steps after the user approves its mandate: the mandate read-back,
     * the plan registration and the OTP request. Everything it needs travels on the route, so it
     * survives the purchase form being rebuilt behind the authorization web view.
     */
    @Serializable
    data class SipSetup(
        val mandateId: Int,
        val mandateRecordId: String,
        val mfProductId: String,
        val schemeName: String,
        val amount: Int,
        val mode: String,
        /** Zero for a mode with no debit day. */
        val installmentDay: Int
    )

    /**
     * Confirms a registered SIP with the OTP the gateway sent. [planId] is what both OTP calls
     * are keyed on; the rest is what the success screen shows once the code lands.
     */
    @Serializable
    data class SipPurchaseOtp(
        val planId: String,
        val schemeName: String,
        val amount: Int,
        val mode: String,
        val installmentDay: Int
    )

    // Bundles (ported from Janta Nivesh)

    @Serializable
    data class BundleDetails(
        val bundleId: String,
        /** A [org.sharad.velvetinvestment.features.core.domain.models.PurchaseMode] name; read back with `PurchaseMode.fromName`. */
        val purchaseMode: String = "MONTHLY"
    )

    /** Picks funds for one category of the [BundleDetails] below it, sharing its view model. */
    @Serializable
    data class BundleSelectFund(val categoryId: String)

    /** The full fund list for one category, opened from [BundleSelectFund] to fill its active slot. */
    @Serializable
    data class BundleExploreFunds(val categoryId: String)

    //Onboarding Steps
    @Serializable
    data object OnboardingStageLoader

    @Serializable
    data object OnboardingKYCSplash

    @Serializable
    data class OnboardingEmail(val isKycRequired: Boolean = false)

    @Serializable
    data class OnboardingEmailOtp(val email: String, val isKycRequired: Boolean = false)

    @Serializable
    data object OnboardingBasicDetails

    @Serializable
    data object OnboardingPANVerification

    @Serializable
    data object OnboardingKYCInitiation

    @Serializable
    data object OnboardingSignatureUpload

    @Serializable
    data object OnboardingBankVerification

    @Serializable
    data object OnboardingNominee

    @Serializable
    data object NomineeOptOutTerms

    @Serializable
    data class OnboardingProfile(val isKycRequired: Boolean = false)

    @Serializable
    data class UpiPaymentWebView(
        val url: String,
        val title: String? = null,
        val completionRouteKey: String? = null
    )
}