package org.sharad.velvetinvestment.shared.Navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.koin.compose.viewmodel.koinViewModel
import org.sharad.velvetinvestment.presentation.tradingaccount.compose.GuardianDetail
import org.sharad.velvetinvestment.presentation.tradingaccount.compose.TAScreen4
import org.sharad.velvetinvestment.presentation.tradingaccount.compose.TAScreen5
import org.sharad.velvetinvestment.presentation.tradingaccount.compose.TAScreen6
import org.sharad.velvetinvestment.presentation.tradingaccount.compose.TAScreen8
import org.sharad.velvetinvestment.presentation.tradingaccount.compose.TradingAccountScreen1
import org.sharad.velvetinvestment.presentation.tradingaccount.compose.TradingScreen2
import org.sharad.velvetinvestment.presentation.tradingaccount.compose.TradingScreen3
import org.sharad.velvetinvestment.presentation.tradingaccount.viewmodel.TradingAccountViewModel

@Composable
fun TradingAccountNavigation(onBackClick: () -> Boolean) {

    val navController = rememberNavController()
    val viewModel: TradingAccountViewModel= koinViewModel()
    Scaffold(
        modifier=Modifier.fillMaxSize(),
        containerColor = Color.White
    ){pv->
        NavHost(
            navController = navController,
            startDestination = Route.TradingAccountBasicDetails
        ) {

            composable<Route.TradingAccountBasicDetails> {
                TradingAccountScreen1(
                    pv = pv,
                    onClick= {navController.navigate(Route.TradingAccountPANDetails)},
                    onBackClick=onBackClick,
                    viewModel=viewModel
                )
            }

            composable<Route.TradingAccountPANDetails> {
                TradingScreen2(
                    pv = pv,
                    onClick= {navController.navigate(Route.TradingAccountFinancialDetails)},
                    onBackClick = {navController.popBackStack()},
                    viewModel=viewModel
                )
            }

            composable<Route.TradingAccountFinancialDetails> {
                TradingScreen3(
                    pv = pv,
                    onClick = {
                        navController.navigate(Route.TradingAccountClientInfo)
                    },
                    onBackClick = {navController.popBackStack()},
                    viewModel=viewModel
                )
            }

            composable<Route.TradingAccountClientInfo> {
                TAScreen4(
                    pv = pv,
                    onClick = {
                        navController.navigate(Route.TradingAccountBankDetails)
                    },
                    onBackClick = {navController.popBackStack()},
                    viewModel=viewModel
                )
            }

            composable<Route.TradingAccountBankDetails> {
                TAScreen5(
                    pv = pv,
                    onClick = {
                        navController.navigate(Route.TradingAccountAddressDetails)
                    },
                    onBackClick = {navController.popBackStack()},
                    viewModel=viewModel
                )
            }

            composable<Route.TradingAccountAddressDetails> {
                TAScreen6(
                    pv = pv,
                    onClick = {
                        navController.navigate(Route.TradingAccountGuardianDetails)
                    },
                    onBackClick = {navController.popBackStack()},
                    viewModel=viewModel
                )
            }

            composable<Route.TradingAccountGuardianDetails> {
                GuardianDetail(
                    pv = pv,
                    onClick = {
                        navController.navigate(Route.TradingAccountGuardiansPANDetails)
                    },
                    onBackClick = {navController.popBackStack()},
                    viewModel=viewModel
                )
            }

            composable<Route.TradingAccountGuardiansPANDetails> {
                TAScreen8(
                    pv = pv,
                    onClick = {

                    },
                    onBackClick = {navController.popBackStack()},
                    viewModel=viewModel
                )
            }
        }
    }

}