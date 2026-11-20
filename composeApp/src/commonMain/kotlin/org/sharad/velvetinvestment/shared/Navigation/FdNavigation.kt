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
import org.sharad.velvetinvestment.features.fd.ui.compose.FDDetailsScreenRoot
import org.sharad.velvetinvestment.features.fd.ui.compose.FDPurchaseScreenRoot
import org.sharad.velvetinvestment.features.fd.ui.compose.FDSearchScreenRoot
import org.sharad.velvetinvestment.features.fd.ui.viewmodels.ExploreFdEffect
import org.sharad.velvetinvestment.features.fd.ui.viewmodels.ExploreFdViewModel
import org.sharad.velvetinvestment.features.fd.ui.viewmodels.FdDetailsEffect
import org.sharad.velvetinvestment.features.fd.ui.viewmodels.FdDetailsEvent
import org.sharad.velvetinvestment.features.fd.ui.viewmodels.FdDetailsViewModel
import org.sharad.velvetinvestment.features.fd.ui.viewmodels.SetInvestmentDetailsEffect
import org.sharad.velvetinvestment.features.fd.ui.viewmodels.SetInvestmentDetailsViewModel
import org.sharad.velvetinvestment.shared.compose.ComingSoonScreen

/** Web-view completion key of the FD purchase payment page (JN). */
private const val FD_PURCHASE_WEBVIEW_KEY = "fd_purchase"

/** Set on the FD details entry when the purchase's payment web view closes back onto it. */
private const val FD_PURCHASE_WEBVIEW_RESULT = "fd_purchase_webview_completed"

/**
 * Closes the FD payment web view. Returns false when [key] isn't the FD purchase's, so the caller
 * can try the other features.
 */
fun NavHostController.completeFdWebView(key: String?): Boolean {
    if (key != FD_PURCHASE_WEBVIEW_KEY) return false
    // Payment is done; drop the purchase form along with the web view (JN).
    popBackStack<Route.PurchaseFixedDeposit>(inclusive = true)
    // The FD details are underneath now; tell them to read the deposit again.
    currentBackStackEntry?.savedStateHandle?.set(FD_PURCHASE_WEBVIEW_RESULT, true)
    return true
}

/** FD list → details → set investment details → payment web view, after JN's MainAppNavigation. */
fun NavGraphBuilder.fdDestinations(navController: NavHostController) {

    // `search` is no longer read: JN's list searches in place instead of opening a new list.
    composable<Route.FixedDepositSearchResult> {
        // FD-HIDDEN: every FD entry point lands here, so the FD list is swapped for a
        // "coming soon" screen. To restore, delete ComingSoonScreen(...) and uncomment the block
        // below. See docs/FD_HIDE_RESTORE.md.
        ComingSoonScreen(
            title = "Fixed Deposit",
            onBack = { navController.popBackStack() }
        )

//        val vm: ExploreFdViewModel = koinViewModel()
//        val uiState by vm.uiState.collectAsStateWithLifecycle()
//
//        LaunchedEffect(vm.effect) {
//            vm.effect.collect { effect ->
//                when (effect) {
//                    ExploreFdEffect.NavigateBack -> navController.popBackStack()
//                    is ExploreFdEffect.NavigateToFdDetails ->
//                        navController.navigate(Route.FixedDepositDetails(effect.id))
//                }
//            }
//        }
//        FDSearchScreenRoot(
//            state = uiState,
//            onEvent = vm::handleEvent
//        )
    }

    composable<Route.FixedDepositDetails> {
        val id = it.toRoute<Route.FixedDepositDetails>().id
        val vm: FdDetailsViewModel = koinViewModel(parameters = { parametersOf(id) })
        val uiState by vm.uiState.collectAsStateWithLifecycle()
        val purchaseWebViewReturned by it.savedStateHandle
            .getStateFlow(FD_PURCHASE_WEBVIEW_RESULT, false)
            .collectAsStateWithLifecycle()

        LaunchedEffect(purchaseWebViewReturned) {
            if (purchaseWebViewReturned) {
                it.savedStateHandle[FD_PURCHASE_WEBVIEW_RESULT] = false
                vm.handleEvent(FdDetailsEvent.OnWebViewReturned)
            }
        }

        LaunchedEffect(vm.effect) {
            vm.effect.collect { effect ->
                when (effect) {
                    FdDetailsEffect.NavigateBack -> navController.popBackStack()
                    is FdDetailsEffect.NavigateToSetInvestmentDetails ->
                        navController.navigate(Route.PurchaseFixedDeposit(effect.id))
                }
            }
        }
        FDDetailsScreenRoot(
            state = uiState,
            onEvent = vm::handleEvent
        )
    }

    composable<Route.PurchaseFixedDeposit> {
        val id = it.toRoute<Route.PurchaseFixedDeposit>().id
        val vm: SetInvestmentDetailsViewModel = koinViewModel(parameters = { parametersOf(id) })
        val uiState by vm.uiState.collectAsStateWithLifecycle()

        LaunchedEffect(vm.effect) {
            vm.effect.collect { effect ->
                when (effect) {
                    SetInvestmentDetailsEffect.NavigateBack -> navController.popBackStack()
                    is SetInvestmentDetailsEffect.LaunchWebView -> navController.navigate(
                        Route.WebViewScreen(
                            url = effect.url,
                            exitUrlPatterns = emptyList(),
                            title = "Complete Payment",
                            completionRouteKey = FD_PURCHASE_WEBVIEW_KEY
                        )
                    )
                }
            }
        }
        FDPurchaseScreenRoot(
            state = uiState,
            onEvent = vm::handleEvent
        )
    }
}
