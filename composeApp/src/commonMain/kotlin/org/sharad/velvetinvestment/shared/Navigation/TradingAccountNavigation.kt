package org.sharad.velvetinvestment.shared.Navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import org.koin.compose.viewmodel.koinViewModel
import org.sharad.velvetinvestment.domain.webview.WebViewConfig
import org.sharad.velvetinvestment.domain.webview.WebViewUrlMatchType
import org.sharad.velvetinvestment.presentation.tradingaccount.compose.GuardianDetail
import org.sharad.velvetinvestment.presentation.tradingaccount.compose.TAScreen8
import org.sharad.velvetinvestment.presentation.tradingaccount.compose.TradingAccountAddressScreen
import org.sharad.velvetinvestment.presentation.tradingaccount.compose.TradingAccountBankDetailsScreen
import org.sharad.velvetinvestment.presentation.tradingaccount.compose.TradingAccountBasicDetailsScreen
import org.sharad.velvetinvestment.presentation.tradingaccount.compose.TradingAccountFinancialDetailsScreen
import org.sharad.velvetinvestment.presentation.tradingaccount.compose.TradingAccountPANDetailsScreen
import org.sharad.velvetinvestment.presentation.tradingaccount.viewmodel.TradingAccountViewModel
import org.sharad.velvetinvestment.presentation.webview.WebViewScreen

@Composable
fun TradingAccountNavigation(onBackClick: () -> Unit, onCompletion: () -> Unit) {

    val navController = rememberNavController()
    val viewModel: TradingAccountViewModel= koinViewModel()
    val isMinor by viewModel.isMinor.collectAsStateWithLifecycle()

    Scaffold(
        modifier=Modifier.fillMaxSize(),
        containerColor = Color.White
    ){pv->
        NavHost(
            navController = navController,
            startDestination = Route.TradingAccountBasicDetails
        ) {

            composable<Route.TradingAccountBasicDetails> {
                TradingAccountBasicDetailsScreen(
                    onClick = {
                        if (isMinor) {
                            navController.navigate(
                                Route.TradingAccountGuardianDetails
                            ) {
                                launchSingleTop = true
                            }
                        } else {
                            navController.navigate(Route.TradingAccountPANDetails) {
                                launchSingleTop = true
                            }
                        }
                    },
                    onBackClick = onBackClick,
                    viewModel = viewModel
                )
            }

            composable<Route.TradingAccountPANDetails> {
                TradingAccountPANDetailsScreen(
                    onClick= {navController.navigate(Route.TradingAccountFinancialDetails){
                        launchSingleTop=true
                    } },
                    onBackClick = {navController.popBackStack()},
                    viewModel=viewModel
                )
            }

            composable<Route.TradingAccountFinancialDetails> {
                TradingAccountFinancialDetailsScreen(
                    onClick = {
                        navController.navigate(Route.TradingAccountBankDetails){
                            launchSingleTop=true
                        }
                    },
                    onBackClick = {navController.popBackStack()},
                    viewModel=viewModel
                )
            }
            composable<Route.TradingAccountBankDetails> {
                TradingAccountBankDetailsScreen(
                    onClick = {
                        navController.navigate(Route.TradingAccountAddressDetails){
                            launchSingleTop=true
                        }
                    },
                    onBackClick = {navController.popBackStack()},
                    viewModel=viewModel
                )
            }

            composable<Route.TradingAccountAddressDetails> {
                TradingAccountAddressScreen(
                    onClick = {
                        viewModel.submitForm { url ->
                            navController.navigate(
                                Route.WebViewScreen(
                                    url = url,
                                    exitUrlPatterns = emptyList(),
                                    title = "Trading Account"
                                )
                            ) {
                                launchSingleTop = true
                            }
                        }
                    },
                    onBackClick = {navController.popBackStack()},
                    viewModel=viewModel
                )
            }

            composable<Route.WebViewScreen> {
                val route = it.toRoute<Route.WebViewScreen>()

                // Returning from the in-app webview (exit url reached or back pressed)
                // is the trigger to confirm the trading account, same as before.
                val onWebViewDone: () -> Unit = {
                    navController.popBackStack()
                    viewModel.confirmAccount { onCompletion() }
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

            composable<Route.TradingAccountGuardianDetails> {
                GuardianDetail(
                    onClick = {
                        navController.navigate(Route.TradingAccountGuardiansPANDetails){
                            launchSingleTop=true
                        }
                    },
                    onBackClick = {navController.popBackStack()},
                    viewModel=viewModel
                )
            }

            composable<Route.TradingAccountGuardiansPANDetails> {
                TAScreen8(
                    onClick = {
                        navController.navigate(Route.TradingAccountFinancialDetails){
                            launchSingleTop=true
                        }
                    },
                    onBackClick = {navController.popBackStack()},
                    viewModel=viewModel
                )
            }
        }
    }

}