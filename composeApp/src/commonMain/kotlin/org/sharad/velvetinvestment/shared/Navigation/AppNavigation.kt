package org.sharad.velvetinvestment.shared.Navigation

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import org.sharad.velvetinvestment.features.insurance.ui.compose.HealthInsuranceScreen
import org.sharad.velvetinvestment.features.insurance.ui.compose.OtherInsuranceScreen
import org.sharad.velvetinvestment.features.insurance.ui.compose.TermInsuranceScreen
import org.sharad.velvetinvestment.features.investmentrate.ui.compose.InvestmentRateScreen
import org.sharad.velvetinvestment.features.loans.ui.compose.LoanFlowScreen
import androidx.compose.runtime.remember
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import org.sharad.velvetinvestment.features.bundles.presentation.compose.AllBundlesScreen
import org.sharad.velvetinvestment.features.bundles.presentation.compose.BundleDetailsScreen
import org.sharad.velvetinvestment.features.bundles.presentation.compose.ExploreCategoryFundScreenRoot
import org.sharad.velvetinvestment.features.bundles.presentation.compose.SelectFundScreen
import org.sharad.velvetinvestment.features.bundles.presentation.viewmodel.AllBundlesEffect
import org.sharad.velvetinvestment.features.bundles.presentation.viewmodel.AllBundlesViewModel
import org.sharad.velvetinvestment.features.bundles.presentation.viewmodel.BundleDetailsEffect
import org.sharad.velvetinvestment.features.bundles.presentation.viewmodel.BundleDetailsEvent
import org.sharad.velvetinvestment.features.bundles.presentation.viewmodel.BundleDetailsViewModel
import org.sharad.velvetinvestment.features.bundles.presentation.viewmodel.ExploreFundsEffect
import org.sharad.velvetinvestment.features.bundles.presentation.viewmodel.SelectFundEffect
import org.sharad.velvetinvestment.features.bundles.presentation.viewmodel.exploreUnselectableFundIds
import org.sharad.velvetinvestment.features.bundles.presentation.viewmodel.fundIdsInOtherSlots
import org.sharad.velvetinvestment.features.core.domain.models.PurchaseMode
import org.sharad.velvetinvestment.shared.compose.LoaderScreen
import org.sharad.velvetinvestment.features.notifications.ui.compose.NotificationScreen
import org.sharad.velvetinvestment.features.profile.ui.compose.PersonalInformationScreen
import org.sharad.velvetinvestment.features.profile.ui.compose.AboutUsScreen
import org.sharad.velvetinvestment.features.profile.ui.compose.AboutVelvetScreen
import org.sharad.velvetinvestment.features.profile.ui.compose.AboutFireScreen
import org.sharad.velvetinvestment.features.profile.ui.compose.PrivacyPolicyScreen
import org.sharad.velvetinvestment.features.profile.ui.compose.TermsAndConditionsScreen
import org.sharad.velvetinvestment.domain.webview.WebViewConfig
import org.sharad.velvetinvestment.domain.webview.WebViewUrlMatchType
import org.sharad.velvetinvestment.features.webview.WebViewScreen
import org.sharad.velvetinvestment.features.fireonboarding.navigation.FireOnboardingRoute
import org.sharad.velvetinvestment.features.fireonboarding.navigation.fireReportDestinations
import org.sharad.velvetinvestment.features.fireonboarding.presentation.onboarding.compose.FireOnboardingScreenRoot
import org.koin.compose.koinInject
import org.sharad.velvetinvestment.features.auth.ui.compose.BiometricSettingsScreen
import org.sharad.velvetinvestment.features.auth.ui.compose.ChangePinScreen
import org.sharad.velvetinvestment.features.auth.ui.viewmodels.BiometricLoginEffect
import org.sharad.velvetinvestment.features.auth.ui.viewmodels.BiometricSettingsViewModel
import org.sharad.velvetinvestment.features.auth.ui.compose.EnterPinScreen
import org.sharad.velvetinvestment.features.auth.ui.viewmodels.ChangePinEffect
import org.sharad.velvetinvestment.features.auth.ui.viewmodels.ChangePinViewModel
import org.sharad.velvetinvestment.features.auth.ui.viewmodels.EnterPinEffect
import org.sharad.velvetinvestment.features.auth.ui.viewmodels.EnterPinPurpose
import org.sharad.velvetinvestment.features.auth.ui.viewmodels.EnterPinViewModel
import org.sharad.velvetinvestment.utils.storage.AuthPrefs

// TODO(top-up): EXISTING_FUND_LUMPSUM_WEBVIEW_RESULT = "existing_fund_lumpsum_webview_completed"

/**
 * Whether the app lock has already been cleared since the process started. Deliberately not saved
 * state: it dies with the process, so every cold start asks for the PIN once and every later entry
 * into the main app — a rotation, a return from another graph — goes straight to the bottom nav.
 * Reset on sign-out in [BaseNavigation], so the next user on this device is asked again.
 */
internal var pinVerifiedThisSession = false

@Composable
fun AppNavigation(
    /** Opens the KYC onboarding graph on top of the main app (JN `navigateToKYC`). */
    navigateToKYC: () -> Unit,
    onSignOut: () -> Unit
) {
    // Token expiry (AppEvent.LogOut) is handled in BaseNavigation, above every graph.
    val prefs: AuthPrefs = koinInject()

    val startDestination: Any = remember {
        if (!prefs.isMpinSetup() || !prefs.isMpinEnabled() || pinVerifiedThisSession) {
            Route.BottomNav
        } else {
            Route.EnterPin(EnterPinPurpose.APP_LOCK)
        }
    }

    Scaffold(
        containerColor = Color.White
    ) {
        val navController= rememberNavController()
        NavHost(
            navController = navController,
            startDestination = startDestination,
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

            composable<Route.EnterPin> { entry ->
                val purpose = entry.toRoute<Route.EnterPin>().purpose
                val vm: EnterPinViewModel = koinViewModel(parameters = { parametersOf(purpose) })
                val state by vm.uiState.collectAsStateWithLifecycle()

                LaunchedEffect(vm.effect) {
                    vm.effect.collect { effect ->
                        when (effect) {
                            EnterPinEffect.NavigateBack -> navController.popBackStack()

                            EnterPinEffect.PinVerified -> when (purpose) {
                                EnterPinPurpose.CHANGE_PIN -> {
                                    navController.navigate(Route.ChangePin) {
                                        launchSingleTop = true
                                        // Re-authentication is spent; back from the change screen
                                        // belongs to whatever asked for the change.
                                        popUpTo<Route.EnterPin> { inclusive = true }
                                    }
                                }

                                else -> {
                                    pinVerifiedThisSession = true
                                    navController.navigate(Route.BottomNav) {
                                        launchSingleTop = true
                                        // The lock is cleared for this run — going back to it would
                                        // only strand the user on a screen they already passed.
                                        popUpTo<Route.EnterPin> { inclusive = true }
                                    }
                                }
                            }
                        }
                    }
                }

                EnterPinScreen(
                    state = state,
                    onEvent = vm::handleEvent
                )
            }

            composable<Route.ChangePin> {
                val vm: ChangePinViewModel = koinViewModel()
                val state by vm.uiState.collectAsStateWithLifecycle()

                LaunchedEffect(vm.effect) {
                    vm.effect.collect { effect ->
                        when (effect) {
                            ChangePinEffect.NavigateBack,
                            ChangePinEffect.PinUpdated -> navController.popBackStack()
                        }
                    }
                }

                ChangePinScreen(
                    state = state,
                    onEvent = vm::handleEvent
                )
            }

            composable<Route.BiometricLogin> {
                val vm: BiometricSettingsViewModel = koinViewModel()
                val state by vm.uiState.collectAsStateWithLifecycle()

                LaunchedEffect(vm.effect) {
                    vm.effect.collect { effect ->
                        when (effect) {
                            BiometricLoginEffect.NavigateBack -> navController.popBackStack()
                        }
                    }
                }

                BiometricSettingsScreen(
                    state = state,
                    onEvent = vm::handleEvent
                )
            }

            composable<Route.BottomNav> {
                BottomNavigation(
                    navigateToBiometricLogin = {
                        navController.navigate(Route.BiometricLogin) {
                            launchSingleTop = true
                        }
                    },
                    navigateToChangePin = {
                        // A PIN that exists has to be proven before it is replaced (JN).
                        if (prefs.isMpinSetup()) {
                            navController.navigate(Route.EnterPin(EnterPinPurpose.CHANGE_PIN)) {
                                launchSingleTop = true
                            }
                        } else {
                            navController.navigate(Route.ChangePin) {
                                launchSingleTop = true
                            }
                        }
                    },
                    navigateToBundleDetails = { bundleId: String, purchaseMode: PurchaseMode ->
                        navController.navigate(Route.BundleDetails(bundleId, purchaseMode.name)) {
                            launchSingleTop = true
                        }
                    },
                    navigateToCart = {
                        navController.navigate(Route.CartScreen) {
                            launchSingleTop = true
                        }
                    },
                    // JN: straight to the order details — the portfolio already carries every
                    // figure that screen shows, so the folio listing in between had nothing to add.
                    navigateToSIPDetailsScreen = { holding ->
                        navController.navigate(holding.toOrderDetailsRoute()) {
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
                        navController.navigate(FireOnboardingRoute.FireReport) {
                            launchSingleTop = true
                        }
                    },
                    navigateToFireOnboarding = {
                        navController.navigate(FireOnboardingRoute.FireOnboardingFlow) {
                            launchSingleTop = true
                        }
                    },
                    navigateToOnboarding = navigateToKYC,
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
                    navigateToMutualFundDetailScreen = { fund ->
                        navController.navigate(Route.MutualFundDetails(id = fund.id, isin = fund.isin)) {
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
                        navController.navigate(Route.SingleGoalAdd){
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
                    // Profile's KYC Status row: straight into the KYC onboarding graph (JN).
                    navigateToKYC = navigateToKYC,
                    navigateToInvestmentRateScree={
                        navController.navigate(Route.InvestmentRateScreen){
                            launchSingleTop=true
                        }
                    },
                    navigateToMyOrders = {
                        navController.navigate(Route.MyOrders) {
                            launchSingleTop = true
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


            goalsDestinations(navController = navController)

            // FIRE onboarding: its own nested NavHost; it closes itself once every step in
            // fire_stages is verified or skipped (refresh events already sent).
            composable<FireOnboardingRoute.FireOnboardingFlow> {
                FireOnboardingScreenRoot(
                    onClose = {
                        navController.popBackStack<FireOnboardingRoute.FireOnboardingFlow>(inclusive = true)
                    }
                )
            }

            fireReportDestinations(
                navController = navController,
                onLoansClick = {
                    navController.navigate(Route.LoansFlowScreen) { launchSingleTop = true }
                },
                onGoalsClick = {
                    navController.navigate(Route.GoalsScreen) { launchSingleTop = true }
                }
            )

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
            // FD list, details, purchase (FdNavigation.kt)
            fdDestinations(navController)

            composable<Route.HealthInsurance> {
                HealthInsuranceScreen { navController.popBackStack() }
            }
            composable<Route.TermInsurance> {
                TermInsuranceScreen { navController.popBackStack() }
            }
            composable<Route.OtherInsurance> {
                OtherInsuranceScreen { navController.popBackStack() }
            }


            composable<Route.AllBundleScreen> {
                val vm: AllBundlesViewModel = koinViewModel()
                val state by vm.uiState.collectAsStateWithLifecycle()

                LaunchedEffect(vm.effect) {
                    vm.effect.collect { effect ->
                        when (effect) {
                            AllBundlesEffect.NavigateBack -> navController.popBackStack()
                            AllBundlesEffect.NavigateToCart -> navController.navigate(Route.CartScreen) {
                                launchSingleTop = true
                            }
                            is AllBundlesEffect.NavigateToBundle ->
                                navController.navigate(
                                    Route.BundleDetails(effect.bundleId, effect.purchaseMode.name)
                                ) {
                                    launchSingleTop = true
                                }
                        }
                    }
                }

                AllBundlesScreen(
                    state = state,
                    onEvent = vm::handleEvent
                )
            }

            composable<Route.BundleDetails> { entry ->
                val route = entry.toRoute<Route.BundleDetails>()
                val vm: BundleDetailsViewModel = koinViewModel(
                    parameters = { parametersOf(route.bundleId, PurchaseMode.fromName(route.purchaseMode)) }
                )
                val state by vm.uiState.collectAsStateWithLifecycle()

                LaunchedEffect(vm.effect) {
                    vm.effect.collect { effect ->
                        when (effect) {
                            BundleDetailsEffect.NavigateBack -> navController.popBackStack()
                            is BundleDetailsEffect.NavigateToSelectFund ->
                                navController.navigate(Route.BundleSelectFund(effect.categoryId)) {
                                    launchSingleTop = true
                                }
                            BundleDetailsEffect.NavigateToCart -> navController.navigate(Route.CartScreen) {
                                launchSingleTop = true
                            }
                        }
                    }
                }

                BundleDetailsScreen(
                    state = state,
                    onEvent = vm::handleEvent
                )
            }

            composable<Route.BundleSelectFund> { entry ->
                val route = entry.toRoute<Route.BundleSelectFund>()
                // The details entry's view model, so saved picks land in the bundle it shows.
                val detailsEntry = remember(entry) { navController.getBackStackEntry<Route.BundleDetails>() }
                val detailsRoute = detailsEntry.toRoute<Route.BundleDetails>()
                val vm: BundleDetailsViewModel = koinViewModel(
                    viewModelStoreOwner = detailsEntry,
                    parameters = {
                        parametersOf(detailsRoute.bundleId, PurchaseMode.fromName(detailsRoute.purchaseMode))
                    }
                )
                val state by vm.uiState.collectAsStateWithLifecycle()

                LaunchedEffect(route.categoryId) {
                    vm.handleEvent(BundleDetailsEvent.OnFundSelectionOpened(route.categoryId))
                }

                LaunchedEffect(vm.selectFundEffect) {
                    vm.selectFundEffect.collect { effect ->
                        when (effect) {
                            SelectFundEffect.Close -> navController.popBackStack()
                            is SelectFundEffect.NavigateToExplore ->
                                navController.navigate(Route.BundleExploreFunds(effect.categoryId)) {
                                    launchSingleTop = true
                                }
                        }
                    }
                }

                SelectFundScreen(
                    state = state,
                    onEvent = vm::handleEvent
                )
            }

            composable<Route.BundleExploreFunds> { entry ->
                val route = entry.toRoute<Route.BundleExploreFunds>()
                val detailsEntry = remember(entry) { navController.getBackStackEntry<Route.BundleDetails>() }
                val detailsRoute = detailsEntry.toRoute<Route.BundleDetails>()
                val vm: BundleDetailsViewModel = koinViewModel(
                    viewModelStoreOwner = detailsEntry,
                    parameters = {
                        parametersOf(detailsRoute.bundleId, PurchaseMode.fromName(detailsRoute.purchaseMode))
                    }
                )
                val state by vm.uiState.collectAsStateWithLifecycle()
                val category = state.bundle?.categories?.find { it.id == route.categoryId }

                LaunchedEffect(vm.exploreFundsEffect) {
                    vm.exploreFundsEffect.collect { effect ->
                        when (effect) {
                            ExploreFundsEffect.Close -> navController.popBackStack()
                        }
                    }
                }

                if (category != null) {
                    ExploreCategoryFundScreenRoot(
                        categoryName = category.categoryName,
                        categoryTitle = category.displayName,
                        purchaseMode = state.purchaseMode,
                        unavailableFundIds = state.fundIdsInOtherSlots(),
                        unselectableFundIds = state.exploreUnselectableFundIds(),
                        selectedFundId = state.exploreSelectedFund?.id,
                        isLoadingFund = state.isFetchingExploredFund,
                        onBackClick = { vm.handleEvent(BundleDetailsEvent.OnExploreBackClicked) },
                        onFundSelected = { vm.handleEvent(BundleDetailsEvent.OnExploredFundSelected(it)) },
                        onSaveClick = { vm.handleEvent(BundleDetailsEvent.OnSaveExploredFundClicked) }
                    )
                } else {
                    LoaderScreen()
                }
            }

            composable<Route.InvestmentRateScreen> {
                InvestmentRateScreen(
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }

            composable<Route.WebViewScreen> {
                val route = it.toRoute<Route.WebViewScreen>()

                val onWebViewDone: () -> Unit = {
                    when (route.completionRouteKey) {
                        // TODO(top-up): "existing_fund_lumpsum" handed EXISTING_FUND_LUMPSUM_WEBVIEW_RESULT back.
                        else -> if (
                            !navController.completeCartWebView(route.completionRouteKey) &&
                            !navController.completePlansWebView(route.completionRouteKey) &&
                            !navController.completePortfolioWebView(route.completionRouteKey) &&
                            !navController.completeFdWebView(route.completionRouteKey)
                        ) {
                            navController.popBackStack()
                        }
                    }
                }
                WebViewScreen(
                    config = WebViewConfig(
                        url = route.url,
                        exitUrlPatterns = route.exitUrlPatterns,
                        matchType = WebViewUrlMatchType.valueOf(route.matchType),
                        title = route.title

                    ),
                    onExitUrlReached = { onWebViewDone() },
                    onBackClick = { onWebViewDone() }
                )
            }

            // TODO(top-up): Route.ExistingFundScreen / Route.ExistingFundLumpSumScreen destinations were
            // here; the screens are parked in composeApp/later/ until the top-up flow returns.

            // MF buying flow + plans (MutualFundNavigation.kt)
            mutualFundDestinations(navController = navController, navigateToKYC = navigateToKYC)

            // My Orders + order details (OrdersNavigation.kt)
            ordersDestinations(navController = navController)

            cartDestinations(navController = navController, navigateToKYC = navigateToKYC)

            // Portfolio order details, redeem, FD details, folio funds (PortfolioNavigation.kt)
            portfolioDestinations(navController = navController)


        }
    }
}
