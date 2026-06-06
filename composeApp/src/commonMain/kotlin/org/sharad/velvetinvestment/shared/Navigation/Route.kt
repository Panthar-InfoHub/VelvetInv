package org.sharad.velvetinvestment.shared.Navigation

import kotlinx.serialization.Serializable

object Route {

    @Serializable
    data object SplashScreen

    @Serializable
    data object LoginScreen
    @Serializable
    data object OtpScreen

    @Serializable
    data object LoginFlow

    @Serializable
    data object MainAppFlow

    @Serializable
    data class OnBoardingFlow(val index: Int)
    @Serializable
    data object OnBoardingPersonalDetails
    @Serializable
    data object OnBoardingFinancialFlow
    @Serializable
    data object OnBoardingCurrentAssets
    @Serializable
    data object OnBoardingLoan
    @Serializable
    data object OnBoardingAddLoan
    @Serializable
    data object OnBoardingInsuranceCoverage
    @Serializable
    data object OnBoardingGoal
    @Serializable
    data object OnBoardingGoalAdd
    @Serializable
    data object OnBoardingSummary
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


    @Serializable

    data class SIPPortfolioDetails(
        val id: Int,
        val title: String,
        val category: String,
        val amount: Double,
        val isSip: Boolean,
        val startDate: String,
        val returnPercentage: String,
        val returnAmount: Int,
        val xirr: String,
        val currentNav: Double,
        val avgNav: Double,
        val folio: String,
        val balanceUnits: Double,
        val img_url: String? = ""
    )
    @Serializable
    data class SIPCancellationScreen(val id:String)
    @Serializable
    data class CancelSIPReason(val id:String)
    @Serializable
    data class FDPortfolioDetailsScreen(val id:String)

    @Serializable
    data object CategoryMutualFund
    @Serializable
    data class MutualFundSearchResult(val search:String="", val fundCategory:String?=null)
    @Serializable
    data class MutualFundDetails(val id:String, val folioId:String?=null)
    @Serializable
    data object FireReport
    @Serializable
    data object FireReportUpdateOptions
    @Serializable
    data object FinancialFlowEdit
    @Serializable
    data object InsuranceCoverageEdit
    @Serializable
    data object CurrentAssetEdit


    @Serializable
    data object KYCScreen
    @Serializable
    data object KYCFormScreen
    @Serializable
    data class KYCImageUploadScreen(val name:String)
    @Serializable
    data class KYCContractScreen(val name:String)
    @Serializable
    data class KYCCompleteScreen(val name:String,val verifiedDate:String)

    @Serializable
    data object CheckKYC

    @Serializable
    data object CheckKYCAnimation

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

    @Serializable
    data class FolioFundScreen(val folioId: String)

    @Serializable
    data object ExistingFundScreen
    @Serializable
    data object ExistingFundLumpSumScreen

    @Serializable
    data object TradingAccountNavigation

    @Serializable
    data object TradingAccountBasicDetails
    @Serializable
    data object TradingAccountPANDetails
    @Serializable
    data object TradingAccountFinancialDetails
    @Serializable
    data object TradingAccountClientInfo
    @Serializable
    data object TradingAccountBankDetails
    @Serializable
    data object TradingAccountAddressDetails
    @Serializable
    data object TradingAccountGuardianDetails
    @Serializable
    data object TradingAccountGuardiansPANDetails

    @Serializable
    data object SingleGoadAdd

    @Serializable
    data object MutualFundTypeSelectionScreen
    @Serializable
    data class GoalProjectionFlow(val id: String)
    @Serializable
    data class GoalProjectionImpact(val id:String)

    @Serializable
    data object GoalMapScreen
    @Serializable
    data class BundleResultScreen(val bundleKey: String)
    @Serializable
    data object AllBundleScreen
    @Serializable
    data object InvestmentRateScreen
}