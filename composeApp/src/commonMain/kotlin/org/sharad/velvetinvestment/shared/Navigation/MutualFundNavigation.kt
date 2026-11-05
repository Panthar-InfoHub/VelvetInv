package org.sharad.velvetinvestment.shared.Navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import org.sharad.velvetinvestment.features.core.domain.models.PurchaseMode
import org.sharad.velvetinvestment.features.mutualfund.domain.models.MutualFundDomain
import org.sharad.velvetinvestment.features.mutualfund.ui.compose.CategoryMutualFundScreenRoot
import org.sharad.velvetinvestment.features.mutualfund.ui.compose.InvestmentMethodScreen
import org.sharad.velvetinvestment.features.mutualfund.ui.compose.MutualFundDetailsScreenRoot
import org.sharad.velvetinvestment.features.mutualfund.ui.compose.MutualFundSearchScreenRoot
import org.sharad.velvetinvestment.features.onboarding.ui.compose.SetupAutopayScreen
import org.sharad.velvetinvestment.features.onboarding.ui.viewmodels.SetupAutopayEffect
import org.sharad.velvetinvestment.features.onboarding.ui.viewmodels.SetupAutopayEvent
import org.sharad.velvetinvestment.features.onboarding.ui.viewmodels.SetupAutopayViewModel
import org.sharad.velvetinvestment.features.plans.ui.compose.FundPurchaseScreen
import org.sharad.velvetinvestment.features.plans.ui.compose.PurchaseSuccessScreen
import org.sharad.velvetinvestment.features.plans.ui.compose.SipPurchaseOtpScreen
import org.sharad.velvetinvestment.features.plans.ui.compose.SipSetupScreen
import org.sharad.velvetinvestment.features.plans.ui.viewmodels.FundPurchaseEffect
import org.sharad.velvetinvestment.features.plans.ui.viewmodels.FundPurchaseViewModel
import org.sharad.velvetinvestment.features.plans.ui.viewmodels.SipMandateHandoff
import org.sharad.velvetinvestment.features.plans.ui.viewmodels.SipPurchaseOtpEffect
import org.sharad.velvetinvestment.features.plans.ui.viewmodels.SipPurchaseOtpViewModel
import org.sharad.velvetinvestment.features.plans.ui.viewmodels.SipSetupEffect
import org.sharad.velvetinvestment.features.plans.ui.viewmodels.SipSetupViewModel
import org.sharad.velvetinvestment.utils.FundTypeSelector
import org.sharad.velvetinvestment.utils.SelectedFundType
import org.sharad.velvetinvestment.utils.WebURLConstants

/*
 * The MF buying flow, after JN's MainAppNavigation:
 *
 *   InvestmentMethod (monthly SIP / one-time → FundTypeSelector)
 *     → CategoryMutualFund (+ search overlay) → MutualFundSearchResult
 *     → MutualFundDetails(id, isin) → "Purchase Fund"
 *     → FundPurchase (opens on the picked mode) → SipSetup → SipPurchaseOtp → PurchaseSuccess
 *                                               ↘ lumpsum payment web view → PurchaseSuccess
 *
 * Velvet differences from JN: fund taps open the details screen rather than the buy screen, and
 * JN's Daily ₹10 / Monthly ₹100 shortcuts don't exist.
 */

// Results handed back to a destination through its savedStateHandle (keys from JN).
private const val MANDATE_ADDED_RESULT = "mandate_added"
private const val ADD_MANDATE_WEBVIEW_RESULT = "add_mandate_webview_completed"
private const val WEBVIEW_COMPLETION_ADD_MANDATE = "add_mandate"
private const val PURCHASE_PAYMENT_RESULT = "purchase_payment_returned"
private const val WEBVIEW_COMPLETION_PURCHASE_PAYMENT = "mf_purchase_payment"
private const val SIP_MANDATE_RESULT = "sip_mandate_returned"
private const val WEBVIEW_COMPLETION_SIP_MANDATE = "sip_mandate"

/** The mode picked on the investment-method screen, as the purchase route carries it. */
private fun selectedPurchaseMode(): PurchaseMode =
    when (FundTypeSelector.fundType.value) {
        SelectedFundType.SIP -> PurchaseMode.MONTHLY
        SelectedFundType.LUMSUM -> PurchaseMode.ONE_TIME
    }

/** Every fund tap in the flow opens its details, carrying the ISIN the purchase needs. */
private fun NavHostController.openFundDetails(fund: MutualFundDomain) {
    navigate(Route.MutualFundDetails(id = fund.id, isin = fund.isin)) {
        launchSingleTop = true
    }
}

/**
 * Finishes a plans web view (`Route.WebViewScreen`) by handing its result back to the screen
 * that opened it. Returns false for a key that isn't a plans one, so the caller handles it.
 */
fun NavHostController.completePlansWebView(completionRouteKey: String?): Boolean {
    val resultKey = when (completionRouteKey) {
        WEBVIEW_COMPLETION_PURCHASE_PAYMENT -> PURCHASE_PAYMENT_RESULT
        WEBVIEW_COMPLETION_SIP_MANDATE -> SIP_MANDATE_RESULT
        WEBVIEW_COMPLETION_ADD_MANDATE -> ADD_MANDATE_WEBVIEW_RESULT
        else -> return false
    }
    previousBackStackEntry?.savedStateHandle?.set(resultKey, true)
    popBackStack()
    return true
}

fun NavGraphBuilder.mutualFundDestinations(
    navController: NavHostController,
    /** Opens the KYC onboarding graph (JN `navigateToKYC`). */
    navigateToKYC: () -> Unit
) {

    composable<Route.MutualFundTypeSelectionScreen> {
        InvestmentMethodScreen(
            onStartSipClick = {
                FundTypeSelector.updateFundTypeToSIP()
                navController.navigate(Route.CategoryMutualFund) {
                    launchSingleTop = true
                }
            },
            onLumpsumClick = {
                FundTypeSelector.updateFundTypeToLumpSum()
                navController.navigate(Route.CategoryMutualFund) {
                    launchSingleTop = true
                }
            },
            // TODO(top-up): onExistingSIPFundClick → Route.ExistingFundScreen,
            //  onExistingLumpSumFundClick → Route.ExistingFundLumpSumScreen (screens in composeApp/later/).
            onBackClick = { navController.popBackStack() },
        )
    }

    composable<Route.CategoryMutualFund> {
        CategoryMutualFundScreenRoot(
            onBackClick = { navController.popBackStack() },
            onIconClick = {
                navController.navigate(Route.CartScreen) {
                    launchSingleTop = true
                }
            },
            onFundClick = { fund -> navController.openFundDetails(fund) },
            onSearchClick = { search ->
                navController.navigate(Route.MutualFundSearchResult(search)) {
                    launchSingleTop = true
                }
            },
            onCategoryClick = { id ->
                navController.navigate(Route.MutualFundSearchResult(tag = id)) {
                    launchSingleTop = true
                }
            },
            onBundledFundClick = { bundleId ->
                navController.navigate(Route.BundleDetails(bundleId)) {
                    launchSingleTop = true
                }
            },
            onBundleClick = {
                navController.navigate(Route.AllBundleScreen)
            },
            onBookFdClick = {
                navController.navigate(Route.FixedDepositSearchResult()) {
                    launchSingleTop = true
                }
            },
            onCreateGoalClick = {
                navController.navigate(Route.SingleGoalAdd) {
                    launchSingleTop = true
                }
            }
        )
    }

    composable<Route.MutualFundSearchResult> {
        val route = it.toRoute<Route.MutualFundSearchResult>()
        MutualFundSearchScreenRoot(
            onBackClick = { navController.popBackStack() },
            onFundClick = { fund -> navController.openFundDetails(fund) },
            searchText = route.search,
            tag = route.tag,
            category = route.category,
            amountType = route.amountType,
            onSearchClick = { search ->
                navController.navigate(Route.MutualFundSearchResult(search = search))
            },
            heading = "Mutual Funds",
            onBundleClick = {
                navController.navigate(Route.AllBundleScreen)
            },
            onBundledFundClick = { bundleId ->
                navController.navigate(Route.BundleDetails(bundleId)) {
                    launchSingleTop = true
                }
            },
            onBookFdClick = {
                navController.navigate(Route.FixedDepositSearchResult()) {
                    launchSingleTop = true
                }
            },
            onCreateGoalClick = {
                navController.navigate(Route.SingleGoalAdd) {
                    launchSingleTop = true
                }
            }
        )
    }

    composable<Route.MutualFundDetails> {
        val route = it.toRoute<Route.MutualFundDetails>()
        MutualFundDetailsScreenRoot(
            id = route.id,
            isin = route.isin,
            onBackClick = { navController.popBackStack() },
            onPurchaseClick = { mfProductId, isin, fundName, fundSubtitle ->
                navController.navigate(
                    Route.FundPurchase(
                        mfProductId = mfProductId,
                        isin = isin,
                        fundName = fundName,
                        fundSubtitle = fundSubtitle,
                        purchaseMode = selectedPurchaseMode().name
                    )
                ) {
                    launchSingleTop = true
                }
            },
            onCartClick = {
                navController.navigate(Route.CartScreen) {
                    launchSingleTop = true
                }
            },
            onKycClick = navigateToKYC
        )
    }

    // ---- Plans (JN `features/plans`) ----

    composable<Route.FundPurchase> { entry ->
        val route = entry.toRoute<Route.FundPurchase>()
        val vm: FundPurchaseViewModel = koinViewModel {
            parametersOf(
                route.mfProductId,
                route.isin,
                route.fundName,
                route.fundSubtitle,
                route.amountType,
                PurchaseMode.fromName(route.purchaseMode)
            )
        }

        val mandateAdded by entry.savedStateHandle
            .getStateFlow(MANDATE_ADDED_RESULT, false)
            .collectAsStateWithLifecycle()

        LaunchedEffect(mandateAdded) {
            if (mandateAdded) {
                entry.savedStateHandle[MANDATE_ADDED_RESULT] = false
                vm.refreshMandates()
            }
        }

        val sipMandateReturned by entry.savedStateHandle
            .getStateFlow(SIP_MANDATE_RESULT, false)
            .collectAsStateWithLifecycle()

        LaunchedEffect(sipMandateReturned) {
            if (sipMandateReturned) {
                entry.savedStateHandle[SIP_MANDATE_RESULT] = false
                // Whether the mandate was actually approved is only knowable from the
                // server, which the setup screen polls for.
                vm.onMandateAuthorizationReturned()
            }
        }

        val paymentReturned by entry.savedStateHandle
            .getStateFlow(PURCHASE_PAYMENT_RESULT, false)
            .collectAsStateWithLifecycle()

        LaunchedEffect(paymentReturned) {
            if (paymentReturned) {
                entry.savedStateHandle[PURCHASE_PAYMENT_RESULT] = false
                // Whether the user actually paid is only knowable from the server.
                vm.onPaymentReturned()
            }
        }

        LaunchedEffect(vm.effect) {
            vm.effect.collect { effect ->
                when (effect) {
                    FundPurchaseEffect.NavigateToKyc -> navigateToKYC()

                    FundPurchaseEffect.AddMandate -> {
                        navController.navigate(Route.AddMandate) {
                            launchSingleTop = true
                        }
                    }

                    is FundPurchaseEffect.OpenMandateAuthorization -> {
                        navController.navigate(
                            Route.WebViewScreen(
                                url = effect.url,
                                title = "UPI Autopay",
                                completionRouteKey = WEBVIEW_COMPLETION_SIP_MANDATE,
                                exitUrlPatterns = listOf(WebURLConstants.mandateExitUrl)
                            )
                        )
                    }

                    is FundPurchaseEffect.StartSipSetup -> {
                        val handoff = effect.handoff
                        navController.navigate(
                            Route.SipSetup(
                                mandateId = handoff.mandateId,
                                mandateRecordId = handoff.mandateRecordId,
                                mfProductId = handoff.mfProductId,
                                schemeName = handoff.schemeName,
                                amount = handoff.amount,
                                mode = handoff.mode.name,
                                installmentDay = handoff.installmentDay
                            )
                        ) {
                            launchSingleTop = true
                        }
                    }

                    is FundPurchaseEffect.OpenPayment -> {
                        navController.navigate(
                            Route.WebViewScreen(
                                url = effect.url,
                                exitUrlPatterns = listOf(WebURLConstants.mandateExitUrl),
                                title = "Complete Payment",
                                completionRouteKey = WEBVIEW_COMPLETION_PURCHASE_PAYMENT
                            )
                        )
                    }

                    is FundPurchaseEffect.PurchaseConfirmed -> {
                        navController.navigate(
                            Route.PurchaseSuccess(
                                mode = effect.mode.name,
                                schemeName = effect.schemeName,
                                amount = effect.amount,
                                installmentDay = effect.installmentDay,
                                startDate = effect.startDate
                            )
                        ) {
                            // The purchase is placed — going back to the form would only
                            // invite a duplicate.
                            popUpTo<Route.FundPurchase> { inclusive = true }
                        }
                    }
                }
            }
        }

        val state by vm.uiState.collectAsStateWithLifecycle()
        FundPurchaseScreen(
            state = state,
            handleEvent = vm::handleEvent,
            onBackClick = { navController.popBackStack() }
        )
    }

    composable<Route.AddMandate> { entry ->
        val vm: SetupAutopayViewModel = koinViewModel()

        val authorizationReturned by entry.savedStateHandle
            .getStateFlow(ADD_MANDATE_WEBVIEW_RESULT, false)
            .collectAsStateWithLifecycle()

        LaunchedEffect(authorizationReturned) {
            if (authorizationReturned) {
                entry.savedStateHandle[ADD_MANDATE_WEBVIEW_RESULT] = false
                vm.handleEvent(SetupAutopayEvent.OnAuthorizationReturned)
            }
        }

        LaunchedEffect(Unit) {
            vm.effect.collect { effect ->
                when (effect) {
                    is SetupAutopayEffect.OpenMandateWebView -> {
                        navController.navigate(
                            Route.WebViewScreen(
                                url = effect.url,
                                title = "UPI Autopay",
                                completionRouteKey = WEBVIEW_COMPLETION_ADD_MANDATE
                            )
                        )
                    }

                    SetupAutopayEffect.AutopayCompleted -> {
                        // Hand the result back to whoever asked for the mandate so it can
                        // pick the new one up without being rebuilt.
                        navController.previousBackStackEntry
                            ?.savedStateHandle
                            ?.set(MANDATE_ADDED_RESULT, true)
                        navController.popBackStack()
                    }
                }
            }
        }

        val state by vm.uiState.collectAsStateWithLifecycle()
        SetupAutopayScreen(
            state = state,
            handleEvent = vm::handleEvent
        )
    }

    composable<Route.SipSetup> { entry ->
        val route = entry.toRoute<Route.SipSetup>()
        val vm: SipSetupViewModel = koinViewModel {
            parametersOf(
                SipMandateHandoff(
                    mandateId = route.mandateId,
                    mandateRecordId = route.mandateRecordId,
                    mfProductId = route.mfProductId,
                    schemeName = route.schemeName,
                    amount = route.amount,
                    mode = PurchaseMode.fromName(route.mode),
                    installmentDay = route.installmentDay
                )
            )
        }

        LaunchedEffect(vm.effect) {
            vm.effect.collect { effect ->
                when (effect) {
                    is SipSetupEffect.OtpRequested -> {
                        navController.navigate(
                            Route.SipPurchaseOtp(
                                planId = effect.planId,
                                schemeName = route.schemeName,
                                amount = route.amount,
                                mode = route.mode,
                                installmentDay = route.installmentDay
                            )
                        ) {
                            // The SIP is registered; there is nothing to come back to here.
                            popUpTo<Route.SipSetup> { inclusive = true }
                        }
                    }

                    SipSetupEffect.Cancelled -> navController.popBackStack()
                }
            }
        }

        val state by vm.uiState.collectAsStateWithLifecycle()
        SipSetupScreen(
            state = state,
            schemeName = route.schemeName,
            onCheckStatusClick = vm::onCheckStatusClick,
            onGoBackClick = vm::onCancelClick
        )
    }

    composable<Route.SipPurchaseOtp> { entry ->
        val route = entry.toRoute<Route.SipPurchaseOtp>()
        val mode = PurchaseMode.fromName(route.mode)
        val vm: SipPurchaseOtpViewModel = koinViewModel {
            parametersOf(
                route.planId,
                route.schemeName,
                route.amount,
                mode,
                route.installmentDay
            )
        }

        LaunchedEffect(vm.effect) {
            vm.effect.collect { effect ->
                when (effect) {
                    is SipPurchaseOtpEffect.PurchaseConfirmed -> {
                        navController.navigate(
                            Route.PurchaseSuccess(
                                mode = effect.mode.name,
                                schemeName = effect.schemeName,
                                amount = effect.amount,
                                installmentDay = effect.installmentDay,
                                startDate = effect.startDate
                            )
                        ) {
                            // The SIP is placed — going back to the form would only invite a
                            // duplicate.
                            popUpTo<Route.FundPurchase> { inclusive = true }
                        }
                    }

                    SipPurchaseOtpEffect.Cancelled -> navController.popBackStack()
                }
            }
        }

        val otpState by vm.otp.state.collectAsStateWithLifecycle()
        SipPurchaseOtpScreen(
            otpState = otpState,
            schemeName = route.schemeName,
            amountLabel = sipAmountLabel(mode = mode, amount = route.amount),
            onOtpChange = vm::onOtpChange,
            onVerifyClick = vm::onVerifyClick,
            onResendClick = vm::onResendClick,
            onBackClick = vm::onBackClick
        )
    }

    composable<Route.PurchaseSuccess> { entry ->
        val route = entry.toRoute<Route.PurchaseSuccess>()

        PurchaseSuccessScreen(
            mode = PurchaseMode.fromName(route.mode),
            schemeName = route.schemeName,
            amount = route.amount,
            installmentDay = route.installmentDay.takeIf { it > 0 },
            startDate = route.startDate.takeIf { it.isNotBlank() },
            onViewHoldingsClick = {
                navController.navigate(Route.MyOrders) {
                    // Reified: naming the data class itself would resolve to its companion,
                    // which has no serializer and blows up when the options are built.
                    popUpTo<Route.PurchaseSuccess> { inclusive = true }
                }
            },
            onDoneClick = { navController.popBackStack() }
        )
    }
}

private fun sipAmountLabel(mode: PurchaseMode, amount: Int): String = when (mode) {
    PurchaseMode.MONTHLY -> "₹$amount/month"
    PurchaseMode.ONE_TIME -> "₹$amount"
}
