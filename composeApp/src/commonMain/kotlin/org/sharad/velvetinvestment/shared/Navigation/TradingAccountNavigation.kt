package org.sharad.velvetinvestment.shared.Navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.koin.compose.viewmodel.koinViewModel
import org.sharad.velvetinvestment.presentation.tradingaccount.compose.GuardianDetail
import org.sharad.velvetinvestment.presentation.tradingaccount.compose.TradingAccountClientInfoScreen
import org.sharad.velvetinvestment.presentation.tradingaccount.compose.TradingAccountBankDetailsScreen
import org.sharad.velvetinvestment.presentation.tradingaccount.compose.TradingAccountAddressScreen
import org.sharad.velvetinvestment.presentation.tradingaccount.compose.TAScreen8
import org.sharad.velvetinvestment.presentation.tradingaccount.compose.TradingAccountBasicDetailsScreen
import org.sharad.velvetinvestment.presentation.tradingaccount.compose.TradingAccountPANDetailsScreen
import org.sharad.velvetinvestment.presentation.tradingaccount.compose.TradingAccountFinancialDetailsScreen
import org.sharad.velvetinvestment.presentation.tradingaccount.viewmodel.TradingAccountViewModel

@Composable
fun TradingAccountNavigation(onBackClick: () -> Unit, onCompletion: () -> Unit) {

    val navController = rememberNavController()
    val viewModel: TradingAccountViewModel= koinViewModel()
    val isMinor by viewModel.isMinor.collectAsStateWithLifecycle()
    val hasLaunchedBrowser by viewModel.launchedBrowser.collectAsStateWithLifecycle()

    val lifecycleOwner = LocalLifecycleOwner.current


    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME && hasLaunchedBrowser) {
                viewModel.confirmAccount {
                    onCompletion()
                }
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

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
                    pv = pv,
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
                    pv = pv,
                    onClick= {navController.navigate(Route.TradingAccountFinancialDetails){
                        launchSingleTop=true
                    } },
                    onBackClick = {navController.popBackStack()},
                    viewModel=viewModel
                )
            }

            composable<Route.TradingAccountFinancialDetails> {
                TradingAccountFinancialDetailsScreen(
                    pv = pv,
                    onClick = {
                        navController.navigate(Route.TradingAccountClientInfo){
                            launchSingleTop=true
                        }
                    },
                    onBackClick = {navController.popBackStack()},
                    viewModel=viewModel
                )
            }

            composable<Route.TradingAccountClientInfo> {
                TradingAccountClientInfoScreen(
                    pv = pv,
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
                    pv = pv,
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
                    pv = pv,
                    onClick = {
                        viewModel.submitForm(){}
                    },
                    onBackClick = {navController.popBackStack()},
                    viewModel=viewModel
                )
            }

            composable<Route.TradingAccountGuardianDetails> {
                GuardianDetail(
                    pv = pv,
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
                    pv = pv,
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