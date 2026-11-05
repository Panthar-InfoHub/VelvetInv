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
import org.sharad.velvetinvestment.features.portfolio.domain.models.MutualFundPortfolioDomain
import org.sharad.velvetinvestment.features.portfolio.ui.screens.FDPortfolioDetailsScreen
import org.sharad.velvetinvestment.features.portfolio.ui.screens.FolioFundMFScreen
import org.sharad.velvetinvestment.features.portfolio.ui.screens.MFPortfolioDetailsScreen
import org.sharad.velvetinvestment.features.portfolio.ui.screens.RedeemOtpScreen
import org.sharad.velvetinvestment.features.portfolio.ui.screens.RedeemScreen
import org.sharad.velvetinvestment.features.portfolio.ui.viewmodel.RedeemEffect
import org.sharad.velvetinvestment.features.portfolio.ui.viewmodel.RedeemHolding
import org.sharad.velvetinvestment.features.portfolio.ui.viewmodel.RedeemOtpEffect
import org.sharad.velvetinvestment.features.portfolio.ui.viewmodel.RedeemOtpViewModel
import org.sharad.velvetinvestment.features.portfolio.ui.viewmodel.RedeemViewModel
import org.sharad.velvetinvestment.utils.AppEventsController
import org.sharad.velvetinvestment.utils.trimDoubleTo

/*
 * Portfolio flows, after JN's MainAppNavigation:
 *
 *   Portfolio row → SIPPortfolioDetails (order details) → Redeem → RedeemOtp → back to the tabs
 *                                                       ↘ cancel SIP (reason sheet on the screen)
 *   FD row → FDPortfolioDetailsScreen (↔ FD web view)
 *
 * The folio listing (FolioFundScreen) is still registered but, as in JN, nothing opens it: a
 * portfolio row goes straight to the order details.
 */

private const val FD_DETAILS_WEBVIEW_RESULT = "fd_details_webview_completed"
private const val WEBVIEW_COMPLETION_FD_DETAILS = "fd_details"

/**
 * Finishes a portfolio web view (`Route.WebViewScreen`) by handing its result back. Returns false
 * for a key that isn't a portfolio one, so the caller handles it.
 */
fun NavHostController.completePortfolioWebView(completionRouteKey: String?): Boolean {
    val resultKey = when (completionRouteKey) {
        WEBVIEW_COMPLETION_FD_DETAILS -> FD_DETAILS_WEBVIEW_RESULT
        else -> return false
    }
    previousBackStackEntry?.savedStateHandle?.set(resultKey, true)
    popBackStack()
    return true
}

/**
 * A portfolio holding as the order-details route. Every figure the screen shows comes from here,
 * which is why that screen has no call of its own.
 */
fun MutualFundPortfolioDomain.toOrderDetailsRoute() = Route.SIPPortfolioDetails(
    id = schemeId,
    holdingId = id,
    title = title,
    category = category,
    amount = amount,
    isSip = isSip,
    // Not on the portfolio payload; the screen no longer shows it.
    startDate = "",
    returnPercentage = returnPercentage,
    returnAmount = returnAmount,
    xirr = xirr.toString(),
    currentNav = currentNav.trimDoubleTo(3),
    avgNav = avgNav.trimDoubleTo(3),
    folio = folio,
    balanceUnits = balanceUnits,
    img_url = icon,
    orderId = "",
    actualFolio = actualFolio,
    currentValue = currentValue,
    status = if (isSip) "ACTIVE" else "COMPLETED"
)

fun NavGraphBuilder.portfolioDestinations(navController: NavHostController) {

    composable<Route.SIPPortfolioDetails> {
        val data = it.toRoute<Route.SIPPortfolioDetails>()
        MFPortfolioDetailsScreen(
            onBackClick = { navController.popBackStack() },
            data = data,
            onRedeemClick = {
                navController.navigate(
                    Route.Redeem(
                        holdingId = data.holdingId,
                        scheme = data.title,
                        folioNumber = data.actualFolio.ifBlank { data.folio },
                        availableUnits = data.balanceUnits,
                        currentValue = data.currentValue
                            .takeIf { value -> value != 0.0 }
                            ?: (data.amount + data.returnAmount),
                        isSip = data.isSip
                    )
                ) {
                    launchSingleTop = true
                }
            }
        )
    }

    composable<Route.Redeem> { entry ->
        val route = entry.toRoute<Route.Redeem>()
        val vm: RedeemViewModel = koinViewModel {
            parametersOf(
                RedeemHolding(
                    holdingId = route.holdingId,
                    scheme = route.scheme,
                    folioNumber = route.folioNumber,
                    availableUnits = route.availableUnits,
                    currentValue = route.currentValue,
                    isSip = route.isSip
                )
            )
        }
        val state by vm.uiState.collectAsStateWithLifecycle()

        LaunchedEffect(vm.effect) {
            vm.effect.collect { effect ->
                when (effect) {
                    is RedeemEffect.NavigateToOtp -> navController.navigate(
                        Route.RedeemOtp(effect.redemptionId)
                    ) {
                        launchSingleTop = true
                    }
                }
            }
        }

        RedeemScreen(
            state = state,
            handleEvent = vm::handleEvent,
            onBack = { navController.popBackStack() }
        )
    }

    composable<Route.RedeemOtp> { entry ->
        val route = entry.toRoute<Route.RedeemOtp>()
        val vm: RedeemOtpViewModel = koinViewModel {
            parametersOf(route.redemptionId)
        }
        val otpState by vm.uiState.collectAsStateWithLifecycle()

        LaunchedEffect(vm.effect) {
            vm.effect.collect { effect ->
                when (effect) {
                    RedeemOtpEffect.RedemptionConfirmed -> {
                        // The holding the user just redeemed from is stale, so the portfolio
                        // reloads as they land back on it.
                        AppEventsController.sendPortfolioRefreshEvent()
                        navController.popBackStack(Route.BottomNav, inclusive = false)
                    }
                }
            }
        }

        RedeemOtpScreen(
            state = otpState,
            handleEvent = vm::handleEvent,
            onBack = { navController.popBackStack() }
        )
    }

    composable<Route.FDPortfolioDetailsScreen> {
        val id = it.toRoute<Route.FDPortfolioDetailsScreen>().id
        val fdWebViewReturned by it.savedStateHandle
            .getStateFlow(FD_DETAILS_WEBVIEW_RESULT, false)
            .collectAsStateWithLifecycle()
        FDPortfolioDetailsScreen(
            id = id,
            onBackClick = { navController.popBackStack() },
            onLaunchWebView = { url ->
                navController.navigate(
                    Route.WebViewScreen(
                        url = url,
                        exitUrlPatterns = emptyList(),
                        title = "Fixed Deposit",
                        completionRouteKey = WEBVIEW_COMPLETION_FD_DETAILS
                    )
                )
            },
            webViewReturned = fdWebViewReturned,
            onWebViewConsumed = {
                it.savedStateHandle[FD_DETAILS_WEBVIEW_RESULT] = false
            },
        )
    }

    composable<Route.FolioFundScreen> {
        val route = it.toRoute<Route.FolioFundScreen>()
        FolioFundMFScreen(
            folioId = route.folioId,
            actualFolio = route.actualFolio,
            onBack = { navController.popBackStack() },
            onFundClick = { fund ->
                navController.navigate(
                    Route.SIPPortfolioDetails(
                        id = fund.schemeId,
                        title = fund.title,
                        category = fund.category,
                        amount = fund.amount.toDouble(),
                        isSip = fund.isSip,
                        startDate = fund.startDate,
                        returnPercentage = fund.returnPercentage,
                        returnAmount = fund.`return`,
                        xirr = fund.xirr,
                        currentNav = fund.currentNav,
                        avgNav = fund.avgNav,
                        folio = fund.folio,
                        balanceUnits = fund.balanceUnits,
                        img_url = fund.imgUrl,
                        orderId = fund.orderId,
                        actualFolio = fund.actualFolio
                    )
                ) {
                    launchSingleTop = true
                }
            },
            // TODO(top-up): onTopUp — JN gives it no destination either.
        )
    }
}
