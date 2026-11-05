package org.sharad.velvetinvestment.shared.Navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import org.sharad.velvetinvestment.features.core.domain.models.PurchaseMode
import org.sharad.velvetinvestment.features.orders.domain.model.toDetailsRoute
import org.sharad.velvetinvestment.features.orders.domain.model.toOrderDomain
import org.sharad.velvetinvestment.features.orders.ui.compose.MyOrdersScreen
import org.sharad.velvetinvestment.features.orders.ui.compose.OrderDetailsScreen
import org.sharad.velvetinvestment.features.orders.ui.viewmodel.MyOrdersEffect
import org.sharad.velvetinvestment.features.orders.ui.viewmodel.MyOrdersViewModel
import org.sharad.velvetinvestment.utils.SnackBarController

/** My Orders + order details, after JN's MainAppNavigation. */
fun NavGraphBuilder.ordersDestinations(navController: NavHostController) {

    composable<Route.MyOrders> {
        val vm: MyOrdersViewModel = koinViewModel()
        val state by vm.uiState.collectAsStateWithLifecycle()

        LaunchedEffect(vm.effect) {
            vm.effect.collect { effect ->
                when (effect) {
                    MyOrdersEffect.NavigateBack -> navController.popBackStack()
                    is MyOrdersEffect.NavigateToDetails ->
                        navController.navigate(effect.order.toDetailsRoute()) {
                            launchSingleTop = true
                        }
                }
            }
        }

        MyOrdersScreen(
            state = state,
            onEvent = vm::handleEvent
        )
    }

    composable<Route.OrderDetails> { entry ->
        val order = entry.toRoute<Route.OrderDetails>()
        val scope = rememberCoroutineScope()

        OrderDetailsScreen(
            order = order.toOrderDomain(),
            payoutAccount = order.payoutAccount,
            onBack = { navController.popBackStack() },
            // The footer that offers these is commented out in JN too; kept wired for when it returns.
            onRetryOrder = {
                navController.navigate(
                    Route.FundPurchase(
                        mfProductId = order.mfProductId,
                        isin = order.isin,
                        fundName = order.fundName,
                        fundSubtitle = order.fundCategory,
                        purchaseMode = (if (order.systematic) PurchaseMode.MONTHLY else PurchaseMode.ONE_TIME).name
                    )
                ) { launchSingleTop = true }
            },
            // TODO: point at the receipt endpoint once the backend exposes one (JN has the same TODO).
            onDownloadReceipt = {
                scope.launch {
                    SnackBarController.showInfo("Receipts will be available here soon.")
                }
            },
            // JN opens Request Callback here; Velvet has no such screen yet.
            onNeedHelp = {}
        )
    }
}
