package org.sharad.velvetinvestment.shared.Navigation

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import org.sharad.velvetinvestment.presentation.firereport.compose.CurrentAssetEditScreen
import org.sharad.velvetinvestment.presentation.firereport.compose.FinancialFlowEditScreen
import org.sharad.velvetinvestment.presentation.firereport.compose.FireReportScreen
import org.sharad.velvetinvestment.presentation.firereport.compose.InsuranceCoverageEditScreen
import org.sharad.velvetinvestment.presentation.firereport.compose.UpdateDetailsScreen
import org.sharad.velvetinvestment.presentation.fixeddeposits.compose.FDDetailsScreenRoot
import org.sharad.velvetinvestment.presentation.fixeddeposits.compose.FDPurchaseScreenRoot
import org.sharad.velvetinvestment.presentation.fixeddeposits.compose.FDSearchScreenRoot
import org.sharad.velvetinvestment.presentation.goals.compose.GoalProjectionNavigation
import org.sharad.velvetinvestment.presentation.goals.compose.GoalScreen
import org.sharad.velvetinvestment.presentation.goals.compose.SingleGoalScreen
import org.sharad.velvetinvestment.presentation.insurance.HealthInsuranceScreen
import org.sharad.velvetinvestment.presentation.insurance.OtherInsuranceScreen
import org.sharad.velvetinvestment.presentation.insurance.TermInsuranceScreen
import org.sharad.velvetinvestment.presentation.investmentrate.compose.InvestmentRateScreen
import org.sharad.velvetinvestment.presentation.kyc.compose.FileUploadScreen
import org.sharad.velvetinvestment.presentation.kyc.compose.KYCFormScreen
import org.sharad.velvetinvestment.presentation.loans.compose.LoanFlowScreen
import org.sharad.velvetinvestment.presentation.mutualfund.compose.CategoryMutualFundScreenRoot
import org.sharad.velvetinvestment.presentation.mutualfund.compose.MutualFundDetailsScreenRoot
import org.sharad.velvetinvestment.presentation.mutualfund.compose.MutualFundSearchScreenRoot
import org.sharad.velvetinvestment.presentation.portfolio.compose.CancelSIPConfirmationScreen
import org.sharad.velvetinvestment.presentation.portfolio.compose.FDPortfolioDetailsScreen
import org.sharad.velvetinvestment.presentation.portfolio.compose.SIPCancellationReasonScreen
import org.sharad.velvetinvestment.presentation.portfolio.compose.MFPortfolioDetailsScreen
import org.sharad.velvetinvestment.presentation.kyc.compose.KYCScreen
import org.sharad.velvetinvestment.presentation.kyc.compose.KycContractScreen
import org.sharad.velvetinvestment.presentation.mutualfund.compose.AllBundlesScreen
import org.sharad.velvetinvestment.presentation.mutualfund.compose.BundleResultScreenRoot
import org.sharad.velvetinvestment.presentation.cart.compose.CartScreen
import org.sharad.velvetinvestment.presentation.mutualfund.compose.InvestmentMethodScreen
import org.sharad.velvetinvestment.presentation.portfolio.compose.ExistingFundLumpSumScreen
import org.sharad.velvetinvestment.presentation.portfolio.compose.FolioFundMFScreen
import org.sharad.velvetinvestment.presentation.portfolio.compose.ExistingFundScreenRoot
import org.sharad.velvetinvestment.presentation.profile.compose.NotificationScreen
import org.sharad.velvetinvestment.presentation.profile.compose.PersonalInformationScreen
import org.sharad.velvetinvestment.presentation.profile.compose.ProfileNew.compose.CheckKYCScreen
import org.sharad.velvetinvestment.presentation.profile.compose.ProfileNew.compose.KYCCheckAnimationScreen
import org.sharad.velvetinvestment.presentation.profile.compose.ProfileNew.compose.AboutUsScreen
import org.sharad.velvetinvestment.presentation.profile.compose.ProfileNew.compose.AboutVelvetScreen
import org.sharad.velvetinvestment.presentation.profile.compose.ProfileNew.compose.AboutFireScreen
import org.sharad.velvetinvestment.presentation.profile.compose.ProfileNew.compose.PrivacyPolicyScreen
import org.sharad.velvetinvestment.presentation.profile.compose.ProfileNew.compose.TermsAndConditionsScreen
import org.sharad.velvetinvestment.presentation.profile.compose.ProfileNew.compose.KYCCompletedScreen
import org.sharad.velvetinvestment.utils.AppEvent
import org.sharad.velvetinvestment.utils.AppEventsController
import org.sharad.velvetinvestment.utils.DateTimeUtils
import org.sharad.velvetinvestment.utils.FundTypeSelector
import org.sharad.velvetinvestment.utils.SnackBarController

@Composable
fun AppNavigation(onSignOut: () -> Unit) {
    LaunchedEffect(Unit){
        AppEventsController.appEvent.collect {
            when(it){
                AppEvent.LogOut -> {
                    AppEventsController.clear()
                    onSignOut()
                    SnackBarController.showInfo("Token Expired. Login Again.")
                }
                else -> {}
            }
        }
    }

    Scaffold(
        containerColor = Color.White
    ) {
        val navController= rememberNavController()
        NavHost(
            navController = navController,
            startDestination = Route.BottomNav,
            // Forward navigation animation
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(
                        durationMillis = 350,
                        easing = FastOutSlowInEasing
                    )
                )
            },

            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -it },
                    animationSpec = tween(
                        durationMillis = 350,
                        easing = FastOutSlowInEasing
                    )
                )
            },

            // Back navigation animation
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -it }, // From Left
                    animationSpec = tween(
                        durationMillis = 350,
                        easing = FastOutSlowInEasing
                    )
                )
            },

            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it }, // To Right
                    animationSpec = tween(
                        durationMillis = 350,
                        easing = FastOutSlowInEasing
                    )
                )
            }
        ){

            composable<Route.BottomNav> {
                BottomNavigation(
                    navigateToSIPDetailsScreen = {
                        navController.navigate(Route.FolioFundScreen(it.folio)) {
                            launchSingleTop = true
                        }
                    },
                    navigateToFDDetailsScreen = { id ->
                        navController.navigate(Route.FixedDepositDetails(id)) {
                            launchSingleTop = true
                        }
                    },
                    navigateToCategoryMutualFundScreen = {
                        navController.navigate(Route.MutualFundTypeSelectionScreen) {
                            launchSingleTop = true
                        }
                    },
                    navigateToCategoryFDScreen = {
                        navController.navigate(Route.FixedDepositSearchResult()) {
                            launchSingleTop = true
                        }
                    },
                    navigateToFireReportScreen = {
                        navController.navigate(Route.FireReport) {
                            launchSingleTop = true
                        }
                    },
                    navigateToKYCScreen = {
                        navController.navigate(Route.CheckKYC) {
                            launchSingleTop = true
                        }
                    },
                    navigateToGoalScreen = {
                        navController.navigate(Route.GoalsScreen) {
                            launchSingleTop = true
                        }
                    },
                    navigateToNotification = {
                        navController.navigate(Route.Notifications) {
                            launchSingleTop = true
                        }
                    },
                    navigateToPersonalInfo = {
                        navController.navigate(Route.PersonalInformation) {
                            launchSingleTop = true
                        }
                    },
                    navigateToMutualFundDetailScreen = {
                        navController.navigate(Route.MutualFundDetails(it)) {
                            launchSingleTop = true
                        }
                    },
                    navigateToHealthInsurance = {
                        navController.navigate(Route.HealthInsurance) {
                            launchSingleTop = true
                        }
                    },
                    navigateToTermInsurance = {
                        navController.navigate(Route.TermInsurance) {
                            launchSingleTop = true
                        }
                    },
                    navigateToOtherInsurance = {
                        navController.navigate(Route.OtherInsurance) {
                            launchSingleTop = true
                        }

                    },
                    navigateToAddGoal={
                        navController.navigate(Route.SingleGoadAdd){
                            launchSingleTop=true
                        }
                    },
                    navigateToSpecificGoalProjection={id->
                        navController.navigate(Route.GoalProjectionFlow(id))
                    },
                    navigateToMutualFundList={
                        navController.navigate(
                            Route.MutualFundTypeSelectionScreen
                        ){
                            launchSingleTop=true
                        }
                    },
                    navigateToTradingAccountSetup={
                      navController.navigate(
                          Route.TradingAccountNavigation
                      )
                    },
                    navigateToFD={
                        navController.navigate(
                            Route.FixedDepositSearchResult()
                        ){
                            launchSingleTop=true
                        }
                    },
                    navigateToPrivacyPolicy = {
                        navController.navigate(Route.PrivacyPolicy) {
                            launchSingleTop = true
                        }
                    },
                    navigateToTermsAndConditions = {
                        navController.navigate(Route.TermsAndConditions) {
                            launchSingleTop = true
                        }
                    },
                    navigateToAboutUs = {
                        navController.navigate(Route.AboutUs) {
                            launchSingleTop = true
                        }
                    },
                    navigateToAboutVelvet = {
                        navController.navigate(Route.AboutVelvet) {
                            launchSingleTop = true
                        }
                    },
                    navigateToAboutFire = {
                        navController.navigate(Route.AboutFire) {
                            launchSingleTop = true
                        }
                    },
                    navigateToKYC = {
                        navController.navigate(Route.CheckKYC) {
                            launchSingleTop = true
                        }
                    },
                    navigateToInvestmentRateScree={
                        navController.navigate(Route.InvestmentRateScreen){
                            launchSingleTop=true
                        }
                    },
                    navigateToPortfolioFdDetailsScreen={id->
                      navController.navigate(Route.FDPortfolioDetailsScreen(id)){
                          launchSingleTop=true
                      }
                    },
                    onSignOut = onSignOut
                )
            }

            composable<Route.SIPPortfolioDetails> {
                val data = it.toRoute<Route.SIPPortfolioDetails>()
                MFPortfolioDetailsScreen(
                    onBackClick = { navController.popBackStack() },
//                    onCancelClick = {
//                        navController.navigate(Route.SIPCancellationScreen(it)) {
//                            launchSingleTop = true
//                        }
//                    },
                    data = data,
                )
            }
            composable<Route.SIPCancellationScreen> {
                val id = it.toRoute<Route.SIPCancellationScreen>().id
                CancelSIPConfirmationScreen(
                    id = id,
                    onConfirmClick = { id ->
                        navController.navigate(Route.CancelSIPReason(id)) {
                            launchSingleTop = true
                        }
                    },
                    onCancelClick = { navController.popBackStack() },
                )
            }
            composable<Route.CancelSIPReason> {
                val id = it.toRoute<Route.CancelSIPReason>().id
                SIPCancellationReasonScreen(
                    id = id,
                    onConfirmClick = {},
                    onBackClick = { navController.popBackStack() },
                )
            }

            composable<Route.FDPortfolioDetailsScreen> {
                val id = it.toRoute<Route.FDPortfolioDetailsScreen>().id
                FDPortfolioDetailsScreen(
                    id = id,
                    onBackClick = { navController.popBackStack() },
                )
            }

            composable<Route.CategoryMutualFund> {
                CategoryMutualFundScreenRoot(
                    onBackClick = { navController.popBackStack() },
                    onIconClick = {
                        navController.navigate(Route.CartScreen){
                            launchSingleTop=true
                        }
                    },
                    onFundClick = { id ->
                        navController.navigate(Route.MutualFundDetails(id)) {
                            launchSingleTop = true
                        }
                    },
                    onSearchClick = { search ->
                        navController.navigate(Route.MutualFundSearchResult(search)) {
                            launchSingleTop = true
                        }
                    },
                    onCategoryClick = { id ->
                        navController.navigate(Route.MutualFundSearchResult(fundCategory = id)) {
                            launchSingleTop = true
                        }
                    },
                    onBundledFundClick={
                        navController.navigate(Route.BundleResultScreen(it))
                    },
                    onBundleClick = {
                        navController.navigate(Route.AllBundleScreen)
                    }
                )
            }
            composable<Route.MutualFundSearchResult> {
                MutualFundSearchScreenRoot(
                    onBackClick = { navController.popBackStack() },
                    heading = "Mutual Funds",
                    onFundClick = {
                        navController.navigate(Route.MutualFundDetails(it)) {
                            launchSingleTop = true
                        }
                    },
                    searchText= it.toRoute<Route.MutualFundSearchResult>().search,
                    category = it.toRoute<Route.MutualFundSearchResult>().fundCategory,
                    onSearchClick = {search->
                        navController.navigate(Route.MutualFundSearchResult(search = search))
                    }
                )
            }
            composable<Route.MutualFundDetails> {
                val id = it.toRoute<Route.MutualFundDetails>().id
                val folioId = it.toRoute<Route.MutualFundDetails>().folioId
                MutualFundDetailsScreenRoot(
                    onBackClick = { navController.popBackStack() },
                    id = id,
                    folioId=folioId,
                    onCartClick = {
                        navController.navigate(Route.CartScreen)
                    },
                    onKycClick={
                        navController.navigate(Route.CheckKYC){
                            launchSingleTop=true
                        }
                    },
                    onTradingAccountClick={
                        navController.navigate(Route.CheckKYC){
                            launchSingleTop=true
                        }
                    }
                )
            }

            composable<Route.FireReport> {
                FireReportScreen(
                    onBack = { navController.popBackStack() },
                    onUpdateClick = {
                        navController.navigate(Route.FireReportUpdateOptions) {
                            launchSingleTop = true
                        }
                    }
                )
            }

            composable<Route.FireReportUpdateOptions> {
                UpdateDetailsScreen(
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onFinancialFlowClick = {
                        navController.navigate(Route.FinancialFlowEdit) {
                            launchSingleTop = true
                        }
                    },
                    onLoanClick = {
                        navController.navigate(Route.LoansFlowScreen) {
                            launchSingleTop = true
                        }
                    },
                    onInsuranceClick = {
                        navController.navigate(Route.InsuranceCoverageEdit) {
                            launchSingleTop = true
                        }
                    },
                    onAssetClick={
                        navController.navigate(Route.CurrentAssetEdit){
                            launchSingleTop=true
                        }
                    },
                    onGoalsClick = {
                        navController.navigate(Route.GoalsScreen) {
                            launchSingleTop = true
                        }
                    }
                )
            }

            composable<Route.FinancialFlowEdit> {
                FinancialFlowEditScreen(
                    onBackClick = { navController.popBackStack() },
                )
            }

            composable<Route.InsuranceCoverageEdit> {
                InsuranceCoverageEditScreen(
                    onBackClick = { navController.popBackStack() },
                )
            }

            composable<Route.CurrentAssetEdit> {
                CurrentAssetEditScreen(
                    onBackClick = { navController.popBackStack() },
                )
            }



            composable<Route.CheckKYC> {
                CheckKYCScreen(
                    onBackClick = { navController.popBackStack() },
                    navigateToMutualFundKYC = {
                        navController.navigate(Route.KYCScreen) {
                            launchSingleTop = true
                        }
                    },
                    navigateToTradingAccountKYC = {
                        navController.navigate(Route.TradingAccountNavigation) {
                            launchSingleTop = true
                        }
                    }
                )
            }

            composable<Route.KYCScreen> {
                KYCScreen(
                    onBackClick = { navController.popBackStack() },
                    onKYCInitSuccess = {
                        navController.navigate(Route.KYCFormScreen) {
                            launchSingleTop = true

                        }
                    },
                )
            }
            composable<Route.KYCFormScreen> {
                KYCFormScreen(
                    onBack = { navController.popBackStack() },
                    onNext = {
                        navController.navigate(Route.KYCImageUploadScreen(it)) {
                            launchSingleTop = true

                        }
                    },
                )
            }
            composable<Route.KYCImageUploadScreen> {
                val name = it.toRoute<Route.KYCImageUploadScreen>().name
                FileUploadScreen(
                    onBack = { navController.popBackStack() },
                    onSuccessfulUpload = {
                        navController.navigate(Route.KYCContractScreen(name)) {
                            launchSingleTop = true

                        }
                    },
                )
            }
            composable<Route.KYCContractScreen> {
                val name = it.toRoute<Route.KYCContractScreen>().name
                val date = DateTimeUtils.epochMillisToSlashDate(DateTimeUtils.getCurrentEpochMillis())
                KycContractScreen(
                    onBack = { navController.popBackStack() },
                    onSuccessfulUpload = {
                        navController.navigate(
                            Route.KYCCompleteScreen(
                                name = name,
                                verifiedDate = date
                            )
                        ) {
                            popUpTo(Route.BottomNav) {
                                inclusive = false
                            }
                            launchSingleTop = true
                        }
                    },
                )
            }

            composable<Route.KYCCompleteScreen> {
                val name = it.toRoute<Route.KYCCompleteScreen>().name
                val date = it.toRoute<Route.KYCCompleteScreen>().verifiedDate
                KYCCompletedScreen(
                    name = name, verifiedDate = date,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    buttonClick = {
                        navController.navigate(Route.TradingAccountNavigation) {
                            popUpTo<Route.KYCCompleteScreen> {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    }
                )
            }
            composable<Route.GoalsScreen> {
                GoalScreen(
                    onBack = { navController.popBackStack() },
                    onAddClick = {
                        navController.navigate(Route.SingleGoadAdd) {
                            launchSingleTop = true
                        }
                    },
                    onGoalClick = {id->
                        navController.navigate(Route.GoalProjectionFlow(id))
                    }
                )
            }

            composable<Route.GoalProjectionFlow> {
                val id = it.toRoute<Route.GoalProjectionFlow>().id
                GoalProjectionNavigation(
                    goalId = id,
                    onBack = { navController.popBackStack() },
                    onInvestNow = {
                        navController.navigate(Route.MutualFundSearchResult())
                    },
                    navigateToAllBundles = {
                        navController.navigate(Route.AllBundleScreen)
                    },
                    navigateToSpecificBundle = {
                        navController.navigate(Route.BundleResultScreen(it))
                    }
                )
            }

            composable<Route.LoansFlowScreen> {
                LoanFlowScreen(
                    onBack = { navController.popBackStack() }
                )
            }

            composable<Route.Notifications> {
                NotificationScreen(
                    onBack = { navController.popBackStack() },
                )
            }
            composable<Route.PersonalInformation> {
                PersonalInformationScreen(
                    onBack = { navController.popBackStack() },
                )
            }
            composable<Route.PrivacyPolicy> {
                PrivacyPolicyScreen(
                    onBack = { navController.popBackStack() }
                )
            }
            composable<Route.TermsAndConditions> {
                TermsAndConditionsScreen(
                    onBack = { navController.popBackStack() }
                )
            }
            composable<Route.AboutUs> {
                AboutUsScreen(
                    onBack = { navController.popBackStack() }
                )
            }
            composable<Route.AboutVelvet> {
                AboutVelvetScreen(
                    onBack = { navController.popBackStack() }
                )
            }
            composable<Route.AboutFire> {
                AboutFireScreen(
                    onBack = { navController.popBackStack() }
                )
            }

            composable<Route.CheckKYCAnimation> {
                KYCCheckAnimationScreen(
                    onButtonClick = {
                        navController.navigate(Route.MutualFundTypeSelectionScreen){
                            launchSingleTop= true
                        }
                    },
                    onBack= {navController.popBackStack()},
                    buttonText = "Start Investing"
                )
            }


            composable<Route.FixedDepositCategory> {
//                FDCategoryScreenRoot(
//                    onBackClick = {navController.popBackStack()},
//                    onIconClick = {},
//                    onFundClick = {},
//                    pv = pv,
//                    onSearchClick = {
//                        navController.navigate(Route.FixedDepositSearchResult(id = it))
//                    },
//                    onCategoryClick = {id,name->
//                        navController.navigate(Route.FixedDepositSearchResult(id = id))
//                    }
//                )
            }
            composable<Route.FixedDepositSearchResult> {
                val search= it.toRoute<Route.FixedDepositSearchResult>().search
                FDSearchScreenRoot(
                    search=search,
                    onBackClick = { navController.popBackStack() },
                    heading = "Fixed Deposit",
                    onFDClick = {id->
                        navController.navigate(Route.FixedDepositDetails(id))
                    },
                    onSearchClick = {text->
                        navController.navigate(Route.FixedDepositSearchResult(search = text))
                    }
                )
            }
            composable<Route.FixedDepositDetails> {
                val id = it.toRoute<Route.FixedDepositDetails>().id
                FDDetailsScreenRoot(
                    onBackClick = { navController.popBackStack() },
                    id = id,
                    onPurchaseClick = { navController.navigate(Route.PurchaseFixedDeposit(id)) }
                )
            }
            composable<Route.PurchaseFixedDeposit> {
                val id = it.toRoute<Route.PurchaseFixedDeposit>().id
                FDPurchaseScreenRoot(
                    onBackClick = { navController.popBackStack() },
                    id = id
                )
            }
            composable<Route.CartScreen> {
                CartScreen(
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }
            composable<Route.HealthInsurance> {
                HealthInsuranceScreen { navController.popBackStack() }
            }
            composable<Route.TermInsurance> {
                TermInsuranceScreen { navController.popBackStack() }
            }
            composable<Route.OtherInsurance> {
                OtherInsuranceScreen { navController.popBackStack() }
            }

            composable<Route.TradingAccountNavigation> {
                TradingAccountNavigation(
                    onBackClick = { navController.popBackStack() },
                    onCompletion = { navController.navigate(Route.CheckKYCAnimation){
                        popUpTo(Route.BottomNav) {
                            inclusive = false
                        }
                        launchSingleTop = true
                    } }
                )
            }

            composable<Route.SingleGoadAdd> {
                SingleGoalScreen(
                    onBack = { navController.popBackStack() }
                )
            }
            
            composable<Route.MutualFundTypeSelectionScreen> {
                InvestmentMethodScreen(
                    onStartSipClick = {
                        FundTypeSelector.updateFundTypeToSIP()
                        navController.navigate(Route.CategoryMutualFund) {
                            launchSingleTop = true
                        }},
                    onLumpsumClick = {
                        FundTypeSelector.updateFundTypeToLumpSum()
                        navController.navigate(Route.CategoryMutualFund) {
                            launchSingleTop = true
                        }},
                    onExistingSIPFundClick = {
                        navController.navigate(Route.ExistingFundScreen)
                    },
                    onExistingLumpSumFundClick = {
                        navController.navigate(Route.ExistingFundLumpSumScreen)
                    },
                    onBackClick = { navController.popBackStack() },
                )
            }

            composable<Route.BundleResultScreen> {
                BundleResultScreenRoot(
                    bundleKey = it.toRoute<Route.BundleResultScreen>().bundleKey,
                    heading = "Bundle Funds",
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onCartClick = {
                        navController.navigate(Route.CartScreen)
                    },
                    onFundClick = {
                        navController.navigate(Route.MutualFundDetails(it)) {
                            launchSingleTop = true
                        }
                    }
                )
            }

            composable<Route.AllBundleScreen> {
                AllBundlesScreen(
                    onBackClick = { navController.popBackStack() },
                    onBundleClick = { bundleKey ->
                        navController.navigate(Route.BundleResultScreen(bundleKey)){
                            launchSingleTop=true
                        }
                    },
                    onCartClick = {
                        navController.navigate(Route.CartScreen)
                    },
                )
            }

            composable<Route.InvestmentRateScreen> {
                InvestmentRateScreen(
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }

            composable<Route.ExistingFundScreen> {
                ExistingFundScreenRoot(
                    onBack = { navController.popBackStack() },
                    onFundClick = { id, folio ->
                        navController.navigate(Route.MutualFundDetails(id, folio)) {
                            launchSingleTop = true
                        }
                    },
                )
            }

            composable<Route.ExistingFundLumpSumScreen> {
                ExistingFundLumpSumScreen(
                    onBack = { navController.popBackStack() },
                )
            }

            composable<Route.FolioFundScreen> {
                val id = it.toRoute<Route.FolioFundScreen>().folioId
                FolioFundMFScreen(
                    folioId = id,
                    onBack = {
                        navController.popBackStack()
                    },
                    onFundClick = {
                        navController.navigate(Route.SIPPortfolioDetails(
                            id = it.schemeId,
                            title = it.title,
                            category = it.category,
                            amount = it.amount.toDouble(),
                            isSip = it.isSip,
                            startDate = it.startDate,
                            returnPercentage = it.returnPercentage,
                            returnAmount = it.`return`.toInt(),
                            xirr = it.xirr,
                            currentNav = it.currentNav,
                            avgNav = it.avgNav,
                            folio = it.folio,
                            balanceUnits = it.balanceUnits,
                            img_url = it.imgUrl,
                            orderId = it.orderId,
                            actualFolio = it.actualFolio
                        )) {
                            launchSingleTop = true
                        }
                    },
                    onTopUp = {prod_id->
                        navController.navigate(Route.MutualFundDetails(id=prod_id, folioId = id)){
                            launchSingleTop=true
                        }
                    },
                )
            }

        }
    }
}