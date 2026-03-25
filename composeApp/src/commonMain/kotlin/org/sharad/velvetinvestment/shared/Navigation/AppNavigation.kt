package org.sharad.velvetinvestment.shared.Navigation

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import org.sharad.velvetinvestment.presentation.firereport.compose.FireReportScreen
import org.sharad.velvetinvestment.presentation.fixeddeposits.compose.FDCategoryScreenRoot
import org.sharad.velvetinvestment.presentation.fixeddeposits.compose.FDSearchScreenRoot
import org.sharad.velvetinvestment.presentation.goals.compose.GoalScreen
import org.sharad.velvetinvestment.presentation.kyc.compose.FileUploadScreen
import org.sharad.velvetinvestment.presentation.kyc.compose.KYCFormScreen
import org.sharad.velvetinvestment.presentation.mutualfund.compose.CategoryMutualFundScreenRoot
import org.sharad.velvetinvestment.presentation.mutualfund.compose.MutualFundDetailsScreenRoot
import org.sharad.velvetinvestment.presentation.mutualfund.compose.MutualFundSearchScreenRoot
import org.sharad.velvetinvestment.presentation.portfolio.compose.CancelSIPConfirmationScreen
import org.sharad.velvetinvestment.presentation.portfolio.compose.FDDetailsScreen
import org.sharad.velvetinvestment.presentation.portfolio.compose.SIPCancellationReasonScreen
import org.sharad.velvetinvestment.presentation.portfolio.compose.SIPDetailsScreen
import org.sharad.velvetinvestment.presentation.kyc.compose.KYCScreen
import org.sharad.velvetinvestment.presentation.profile.compose.NotificationScreen
import org.sharad.velvetinvestment.presentation.profile.compose.PersonalInformationScreen

@Composable
fun AppNavigation(onSignOut: () -> Unit) {

    Scaffold(
        containerColor = Color.White
    ) {
        val pv=it
        val navController= rememberNavController()
        NavHost(
            navController = navController,
            startDestination = Route.BottomNav,
            // Forward navigation animation
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it }, // From Right
                    animationSpec = tween(
                        durationMillis = 350,
                        easing = FastOutSlowInEasing
                    )
                )
            },

            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -it }, // To Left
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

            composable<Route.BottomNav> {
                BottomNavigation(
                    navigateToSIPDetailsScreen = {id->navController.navigate(Route.SIPDetails(id))},
                    navigateToFDDetailsScreen = {id->navController.navigate(Route.FDDetailsScreen(id))},
                    navigateToCategoryMutualFundScreen = {navController.navigate(Route.MutualFundSearchResult())},
                    navigateToCategoryFDScreen = {navController.navigate(Route.FixedDepositCategory)},
                    navigateToFireReportScreen = {navController.navigate(Route.FireReport)},
                    navigateToKYCScreen={navController.navigate(Route.KYCScreen)},
                    navigateToGoalScreen={navController.navigate(Route.GoalsScreen)},
                    navigateToNotification={navController.navigate(Route.Notifications )},
                    navigateToPersonalInfo={navController.navigate(Route.PersonalInformation)},
                    navigateToMutualFundDetailScreen = {
                        navController.navigate(Route.MutualFundDetails(it))
                    },
                    onSignOut=onSignOut
                )
            }

            composable<Route.SIPDetails> {
                val id= it.toRoute<Route.SIPDetails>().id
                SIPDetailsScreen(
                    onBackClick = {navController.popBackStack()},
                    onCancelClick={navController.navigate(Route.SIPCancellationScreen(it))},
                    id=id,
                    pv=pv
                )
            }
            composable<Route.SIPCancellationScreen> {
                val id= it.toRoute<Route.SIPCancellationScreen>().id
                CancelSIPConfirmationScreen(
                    id=id,
                    onConfirmClick = {id->navController.navigate(Route.CancelSIPReason(id))},
                    onCancelClick = {navController.popBackStack()},
                    pv=pv
                )
            }
            composable<Route.CancelSIPReason> {
                val id= it.toRoute<Route.CancelSIPReason>().id
                SIPCancellationReasonScreen(
                    id=id,
                    onConfirmClick = {},
                    onBackClick = {navController.popBackStack()},
                    pv=pv
                )
            }

            composable<Route.FDDetailsScreen> {
                val id= it.toRoute<Route.FDDetailsScreen>().id
                FDDetailsScreen(
                    id = id,
                    onBackClick = { navController.popBackStack() },
                    pv = pv,
                )
            }

            composable<Route.CategoryMutualFund> {
                CategoryMutualFundScreenRoot(
                    onBackClick = { navController.popBackStack() },
                    pv = pv,
                    onIconClick = {},
                    onFundClick = {id->
                        navController.navigate(Route.MutualFundDetails(id))
                    },
                    onSearchClick = {search->
                        navController.navigate(Route.MutualFundSearchResult())
                    },
                    onCategoryClick = {id,name->
                        navController.navigate(Route.MutualFundSearchResult(heading = name))
                    },
                )
            }
            composable<Route.MutualFundSearchResult> {
                MutualFundSearchScreenRoot(
                    onBackClick = { navController.popBackStack() },
                    pv = pv,
                    heading = "Mutual Funds",
                    onFundClick = {
                        navController.navigate(Route.MutualFundDetails(it))
                    }
                )
            }
            composable<Route.MutualFundDetails> {
                val id= it.toRoute<Route.MutualFundDetails>().id
                MutualFundDetailsScreenRoot(
                    onBackClick = { navController.popBackStack() },
                    pv = pv,
                    id = id,
                    onTopFundClick = {
                        navController.navigate(Route.MutualFundSearchResult())
                    },
                    onFundClick = {
                        navController.navigate(Route.MutualFundDetails(it))
                    },
                    onMonthlySipClick = {},
                    onOneTimeSipClick = {}
                )
            }

            composable<Route.FireReport> {
                FireReportScreen(
                    onBack = {navController.popBackStack()}
                )
            }
            composable<Route.KYCScreen> {
                KYCScreen(
                    onBackClick = {navController.popBackStack()},
                    onKYCInitSuccess = {navController.navigate(Route.KYCFormScreen)},
                    pv=pv
                )
            }
            composable<Route.KYCFormScreen> {
                KYCFormScreen(
                    onBack = {navController.popBackStack()},
                    onNext = {
                        navController.navigate(Route.KYCImageUplaodScreen)
                    },
                    pv=pv
                )
            }
            composable<Route.KYCImageUplaodScreen> {
                FileUploadScreen(
                    onBack = {navController.popBackStack()},
                    onSuccessfulUpload = {

                    },
                    pv=pv
                )
            }
            composable<Route.GoalsScreen> {
                GoalScreen(
                    onBack = {navController.popBackStack()},
                    onAddClick = {},
                    pv = pv,
                    onGoalClick = {}
                )
            }
            composable<Route.Notifications> {
                NotificationScreen(
                    onBack = {navController.popBackStack()},
                    pv = pv,
                )
            }
            composable<Route.PersonalInformation> {
                PersonalInformationScreen(
                    onBack = {navController.popBackStack()},
                    pv = pv,
                )
            }


            composable<Route.FixedDepositCategory> {
                FDCategoryScreenRoot(
                    onBackClick = {navController.popBackStack()},
                    onIconClick = {},
                    onFundClick = {},
                    pv = pv,
                    onSearchClick = {
                        navController.navigate(Route.FixedDepositSearchResult(id = it))
                    },
                    onCategoryClick = {id,name->
                        navController.navigate(Route.FixedDepositSearchResult(id = id))
                    }
                )
            }
            composable<Route.FixedDepositSearchResult> {
                val category= it.toRoute<Route.FixedDepositSearchResult>().heading
                val id= it.toRoute<Route.FixedDepositSearchResult>().id
                FDSearchScreenRoot(
                    onBackClick = { navController.popBackStack() },
                    pv = pv,
                    heading = category,
                    searchId = id,
                    onFDClick = {

                    }
                )
            }
        }
    }
}