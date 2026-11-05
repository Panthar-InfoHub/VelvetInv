package org.sharad.velvetinvestment.shared.Navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import org.koin.compose.viewmodel.koinViewModel
import org.sharad.velvetinvestment.features.cart.presentation.compose.CartCheckoutOtpScreen
import org.sharad.velvetinvestment.features.cart.presentation.compose.CartScreen
import org.sharad.velvetinvestment.features.cart.presentation.viewmodel.CartCheckoutOtpEffect
import org.sharad.velvetinvestment.features.cart.presentation.viewmodel.CartEffect
import org.sharad.velvetinvestment.features.cart.presentation.viewmodel.CartEvent
import org.sharad.velvetinvestment.features.cart.presentation.viewmodel.CartViewModel

/** Set on the cart's entry when a web view opened with completion key "cart" closes. */
internal const val CART_WEBVIEW_RESULT = "cart_webview_completed"

/**
 * Hands a closed web view back to the cart. Returns false when [key] isn't the cart's, so the
 * caller can try the other features.
 */
fun NavHostController.completeCartWebView(key: String?): Boolean {
    if (key != "cart") return false
    // Come back to the cart and run the queued payment follow-up.
    previousBackStackEntry?.savedStateHandle?.set(CART_WEBVIEW_RESULT, true)
    popBackStack()
    return true
}

/** Cart + its checkout OTP, after JN's MainAppNavigation. */
fun NavGraphBuilder.cartDestinations(
    navController: NavHostController,
    navigateToKYC: () -> Unit
) {

    composable<Route.CartScreen> {
        val vm: CartViewModel = koinViewModel()
        val state by vm.uiState.collectAsStateWithLifecycle()
        val cartWebViewReturned by it.savedStateHandle
            .getStateFlow(CART_WEBVIEW_RESULT, false)
            .collectAsStateWithLifecycle()

        LaunchedEffect(vm.effect) {
            vm.effect.collect { effect ->
                when (effect) {
                    CartEffect.NavigateBack -> navController.popBackStack()
                    // Returns to the cart through the same "cart" key, which resumes the SIP
                    // purchase once the user is back from the bank's page.
                    is CartEffect.OpenMandateAuthorization -> navController.navigate(
                        Route.WebViewScreen(
                            url = effect.url,
                            exitUrlPatterns = listOf(effect.exitUrl),
                            title = "UPI Autopay",
                            completionRouteKey = "cart"
                        )
                    )
                    CartEffect.NavigateToCheckoutOtp -> navController.navigate(Route.CartCheckoutOtp) {
                        launchSingleTop = true
                    }
                    CartEffect.NavigateToKyc -> navigateToKYC()
                    CartEffect.NavigateToOrders -> navController.navigate(Route.MyOrders) {
                        launchSingleTop = true
                    }
                }
            }
        }

        LaunchedEffect(Unit) {
            vm.handleEvent(CartEvent.OnScreenResumed)
        }

        LaunchedEffect(cartWebViewReturned) {
            if (cartWebViewReturned) {
                vm.handleEvent(CartEvent.OnWebViewReturned)
                it.savedStateHandle[CART_WEBVIEW_RESULT] = false
            }
        }

        CartScreen(
            state = state,
            onEvent = vm::handleEvent
        )
    }

    composable<Route.CartCheckoutOtp> { entry ->
        // The cart entry's view model, which placed the checkout and holds its batch.
        val cartEntry = remember(entry) { navController.getBackStackEntry<Route.CartScreen>() }
        val vm: CartViewModel = koinViewModel(viewModelStoreOwner = cartEntry)
        val state by vm.uiState.collectAsStateWithLifecycle()
        val otpState by vm.otp.state.collectAsStateWithLifecycle()

        LaunchedEffect(vm.checkoutOtpEffect) {
            vm.checkoutOtpEffect.collect { effect ->
                when (effect) {
                    CartCheckoutOtpEffect.Close ->
                        navController.popBackStack<Route.CartScreen>(inclusive = false)

                    // Replaces the OTP screen, so the "cart" key returns to the cart, which
                    // then reads the payment back.
                    is CartCheckoutOtpEffect.OpenPayment -> navController.navigate(
                        Route.WebViewScreen(
                            url = effect.url,
                            exitUrlPatterns = listOf(effect.exitUrl),
                            title = "Complete Payment",
                            completionRouteKey = "cart"
                        )
                    ) {
                        popUpTo<Route.CartCheckoutOtp> { inclusive = true }
                    }

                    // Replaces the OTP screen, so back from the orders lands on the cart.
                    CartCheckoutOtpEffect.OpenOrders -> navController.navigate(Route.MyOrders) {
                        popUpTo<Route.CartCheckoutOtp> { inclusive = true }
                        launchSingleTop = true
                    }
                }
            }
        }

        CartCheckoutOtpScreen(
            otpState = otpState,
            checkout = state.checkout,
            onEvent = vm::handleEvent
        )
    }
}
