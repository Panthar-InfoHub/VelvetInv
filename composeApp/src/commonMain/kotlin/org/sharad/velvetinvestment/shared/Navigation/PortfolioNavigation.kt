package org.sharad.velvetinvestment.shared.Navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import kotlinx.serialization.json.Json
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import org.sharad.velvetinvestment.features.portfolio.domain.models.HoldingFolioDomain
import org.sharad.velvetinvestment.features.portfolio.domain.models.MutualFundPortfolioDomain
import org.sharad.velvetinvestment.features.portfolio.ui.screens.FDPortfolioDetailsScreen
import org.sharad.velvetinvestment.features.portfolio.ui.screens.FundFoliosScreen
import org.sharad.velvetinvestment.features.portfolio.ui.viewmodel.FundFolioItem
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
 *   Portfolio row → FundFolios (one card per folio; skipped for a single folio) → SIPPortfolioDetails
 *                                 → Redeem → RedeemOtp → back to the tabs
 *                                 ↘ cancel SIP (reason sheet on the screen)
 *   Active SIP row → SIPPortfolioDetails for the SIP's own folio
 *   FD row → FDPortfolioDetailsScreen (↔ FD web view)
 *
 * The order details load their figures from `GET /user/portfolio/{folio}`; the route only carries
 * the folio, the holding id and what to show if that call has nothing.
 */

private val routeJson = Json { ignoreUnknownKeys = true }

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
    status = if (isSip) "ACTIVE" else "COMPLETED",
    planId = planId,
    redeemableUnits = redeemableUnits
)

/** A portfolio fund as its folio listing: the fund's name, icon and type, plus every folio. */
fun MutualFundPortfolioDomain.toFundFoliosRoute() = Route.FundFolios(
    baseId = id,
    title = title,
    category = category,
    isSip = isSip,
    imgUrl = icon,
    // A row the server sent without its folio breakdown is still one folio: itself.
    foliosJson = routeJson.encodeToString(
        folios.ifEmpty {
            listOf(
                HoldingFolioDomain(
                    holdingId = id,
                    folioNumber = actualFolio.ifBlank { folio },
                    units = balanceUnits,
                    redeemableUnits = balanceUnits,
                    amount = amount,
                    currentValue = currentValue
                )
            )
        }
    )
)

fun Route.FundFolios.folios(): List<HoldingFolioDomain> =
    runCatching { routeJson.decodeFromString<List<HoldingFolioDomain>>(foliosJson) }
        .getOrDefault(emptyList())

/**
 * One folio of a fund as the order-details route. The screen reads the folio itself; these are
 * only the figures it falls back on if the folio call has none.
 */
fun HoldingFolioDomain.toOrderDetailsRoute(fund: Route.FundFolios) = Route.SIPPortfolioDetails(
    id = 0,
    holdingId = holdingId,
    title = fund.title,
    category = fund.category,
    amount = amount,
    isSip = fund.isSip,
    startDate = "",
    returnPercentage = "",
    returnAmount = currentValue - amount,
    xirr = "",
    currentNav = 0.0,
    avgNav = 0.0,
    folio = folioNumber,
    balanceUnits = units,
    img_url = fund.imgUrl,
    orderId = "",
    actualFolio = folioNumber,
    currentValue = currentValue,
    status = if (fund.isSip) "ACTIVE" else "COMPLETED",
    redeemableUnits = redeemableUnits
)

/** A loaded folio as the order-details route; the screen reads the folio again for itself. */
fun FundFolioItem.toOrderDetailsRoute() = Route.SIPPortfolioDetails(
    id = 0,
    holdingId = fund.mfHoldingId,
    title = fund.title,
    category = fund.category,
    amount = fund.amount,
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
    actualFolio = fund.actualFolio,
    currentValue = fund.currentValue,
    status = fund.state.ifBlank { if (fund.isSip) "ACTIVE" else "COMPLETED" },
    planId = fund.fpId,
    redeemableUnits = redeemableUnits
)

/**
 * Where a portfolio fund row leads: its folio listing, or — with only one folio, where a listing
 * would be a single card — that folio's order details directly.
 */
fun MutualFundPortfolioDomain.toFundRowRoute(): Any {
    val foliosRoute = toFundFoliosRoute()
    return foliosRoute.folios().singleOrNull()?.toOrderDetailsRoute(foliosRoute) ?: foliosRoute
}

/**
 * The redeem screen for what the order details show: only the redeemable units, and the value in
 * proportion to them, so locked-in units are neither offered nor counted in the amount.
 */
private fun Route.SIPPortfolioDetails.toRedeemRoute(): Route.Redeem {
    val totalValue = currentValue.takeIf { it != 0.0 } ?: (amount + returnAmount)
    val units = redeemableUnits.coerceIn(0.0, balanceUnits.coerceAtLeast(0.0))
    val redeemableValue = if (balanceUnits > 0.0) totalValue * (units / balanceUnits) else 0.0
    return Route.Redeem(
        holdingId = holdingId,
        scheme = title,
        folioNumber = actualFolio.ifBlank { folio },
        availableUnits = units,
        currentValue = redeemableValue,
        isSip = isSip
    )
}

fun NavGraphBuilder.portfolioDestinations(navController: NavHostController) {

    composable<Route.SIPPortfolioDetails> {
        val data = it.toRoute<Route.SIPPortfolioDetails>()
        MFPortfolioDetailsScreen(
            onBackClick = { navController.popBackStack() },
            data = data,
            // The folio listing and these details are both stale now; the portfolio reloads on
            // the refresh event the screen has already sent.
            onOrderCancelled = { navController.popBackStack(Route.BottomNav, inclusive = false) },
            onRedeemClick = { shown ->
                navController.navigate(shown.toRedeemRoute()) {
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

    composable<Route.FundFolios> {
        val route = it.toRoute<Route.FundFolios>()
        val folios = remember(route) { route.folios() }
        FundFoliosScreen(
            title = route.title,
            folios = folios,
            onBack = { navController.popBackStack() },
            onFolioClick = { item ->
                navController.navigate(item.toOrderDetailsRoute()) {
                    launchSingleTop = true
                }
            }
        )
    }
}
