package org.sharad.velvetinvestment.shared.Navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import org.koin.compose.viewmodel.koinViewModel
import org.sharad.velvetinvestment.domain.models.portfolio.MutualFundPortfolioDomain
import org.sharad.velvetinvestment.presentation.explorefunds.compose.ExploreFundScreen
import org.sharad.velvetinvestment.presentation.homescreen.HomeScreenViewModel
import org.sharad.velvetinvestment.presentation.homescreen.compose.HomeScreenMain
import org.sharad.velvetinvestment.presentation.insurance.InsuranceNavigationScreen
import org.sharad.velvetinvestment.presentation.portfolio.compose.PortfolioScreenMain
import org.sharad.velvetinvestment.presentation.portfolio.viewmodel.PortfolioScreenViewModel
import org.sharad.velvetinvestment.presentation.profile.compose.ProfileNew.compose.ProfileScreen
import org.sharad.velvetinvestment.shared.BottomNavBar
import org.sharad.velvetinvestment.utils.AppEvent
import org.sharad.velvetinvestment.utils.AppEventsController

@Composable
fun BottomNavigation(
    navigateToSIPDetailsScreen: (MutualFundPortfolioDomain) -> Unit,
    navigateToFDDetailsScreen: (String) -> Unit,
    navigateToCategoryMutualFundScreen: () -> Unit,
    navigateToFireReportScreen: () -> Unit,
    navigateToKYCScreen: () -> Unit,
    navigateToGoalScreen: () -> Unit,
    navigateToNotification: () -> Unit,
    navigateToPersonalInfo: () -> Unit,
    navigateToCategoryFDScreen: () -> Unit,
    navigateToMutualFundDetailScreen: (String) -> Unit,
    navigateToHealthInsurance: () -> Unit,
    navigateToTermInsurance: () -> Unit,
    navigateToOtherInsurance: () -> Unit,
    onSignOut: () -> Unit,
    navigateToAddGoal: () -> Unit,
    navigateToSpecificGoalProjection: (String) -> Unit,
    navigateToMutualFundList: () -> Unit,
    navigateToFD: () -> Unit,
    navigateToTradingAccountSetup: () -> Unit,
    navigateToPrivacyPolicy: () -> Unit,
    navigateToTermsAndConditions: () -> Unit,
    navigateToAboutUs: () -> Unit,
    navigateToAboutVelvet: () -> Unit = {},
    navigateToAboutFire: () -> Unit = {},
    navigateToKYC: () -> Unit,
    navigateToInvestmentRateScree: () -> Unit,
    navigateToPortfolioFdDetailsScreen: (String)-> Unit
) {

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val homeViewModel: HomeScreenViewModel = koinViewModel()
    val portfolioViewModel: PortfolioScreenViewModel=koinViewModel()

    LaunchedEffect(Unit){
        AppEventsController.appEvent.collect {
            when(it){
                AppEvent.HomeEventRefresh -> {
                    homeViewModel.loadHomeData()
                    AppEventsController.clear()
                }

                AppEvent.GoalEventRefresh -> {
                    homeViewModel.loadHomeData()
                    AppEventsController.clear()
                }

                AppEvent.FireRefreshEvent -> {
                    homeViewModel.loadHomeData()
                    AppEventsController.clear()
                }
                AppEvent.PortfolioRefreshEvent -> {
                    portfolioViewModel.refresh()
                    AppEventsController.clear()
                }
                else -> {}
            }
        }
    }

    Scaffold(
        bottomBar = {
            BottomNavBar(
                currentDestination = currentDestination,
                onNavigate = { route ->
                    navController.navigate(route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        },
        containerColor = Color.White
    ) {
        NavHost(
            navController = navController,
            modifier=Modifier.fillMaxSize()
                .padding(bottom = it.calculateBottomPadding()),
            startDestination = Route.Home,
            // Dynamic horizontal animations based on bottom nav index
            enterTransition = {
                val initialIndex = getRouteIndex(initialState.destination)
                val targetIndex = getRouteIndex(targetState.destination)
                when {
                    initialIndex == -1 || targetIndex == -1 -> EnterTransition.None
                    initialIndex == targetIndex -> EnterTransition.None
                    targetIndex > initialIndex -> slideInHorizontally(
                        initialOffsetX = { it },
                        animationSpec = tween(350, easing = FastOutSlowInEasing)
                    )
                    else -> slideInHorizontally(
                        initialOffsetX = { -it },
                        animationSpec = tween(350, easing = FastOutSlowInEasing)
                    )
                }
            },
            exitTransition = {
                val initialIndex = getRouteIndex(initialState.destination)
                val targetIndex = getRouteIndex(targetState.destination)
                when {
                    initialIndex == -1 || targetIndex == -1 -> ExitTransition.None
                    initialIndex == targetIndex -> ExitTransition.None
                    targetIndex > initialIndex -> slideOutHorizontally(
                        targetOffsetX = { -it },
                        animationSpec = tween(350, easing = FastOutSlowInEasing)
                    )
                    else -> slideOutHorizontally(
                        targetOffsetX = { it },
                        animationSpec = tween(350, easing = FastOutSlowInEasing)
                    )
                }
            },
            popEnterTransition = {
                val initialIndex = getRouteIndex(initialState.destination)
                val targetIndex = getRouteIndex(targetState.destination)
                when {
                    initialIndex == -1 || targetIndex == -1 -> EnterTransition.None
                    initialIndex == targetIndex -> EnterTransition.None
                    targetIndex > initialIndex -> slideInHorizontally(
                        initialOffsetX = { it },
                        animationSpec = tween(350, easing = FastOutSlowInEasing)
                    )
                    else -> slideInHorizontally(
                        initialOffsetX = { -it },
                        animationSpec = tween(350, easing = FastOutSlowInEasing)
                    )
                }
            },
            popExitTransition = {
                val initialIndex = getRouteIndex(initialState.destination)
                val targetIndex = getRouteIndex(targetState.destination)
                when {
                    initialIndex == -1 || targetIndex == -1 -> ExitTransition.None
                    initialIndex == targetIndex -> ExitTransition.None
                    targetIndex > initialIndex -> slideOutHorizontally(
                        targetOffsetX = { -it },
                        animationSpec = tween(350, easing = FastOutSlowInEasing)
                    )
                    else -> slideOutHorizontally(
                        targetOffsetX = { it },
                        animationSpec = tween(350, easing = FastOutSlowInEasing)
                    )
                }
            }
        ){

            composable<Route.Home> {
                HomeScreenMain(
                    viewModel = homeViewModel,
                    navigateToFireReportScreen=navigateToFireReportScreen,
                    navigateToKYCScreen=navigateToKYCScreen,
                    navigateToGoalScreen=navigateToGoalScreen,
                    navigateToNotification=navigateToNotification,
                    navigateToAddGoal=navigateToAddGoal,
                    navigateToSpecificGoalProjection=navigateToSpecificGoalProjection,
                    navigateToMutualFund= {
                        navigateToMutualFundList()
                    },
                    navigateToFd={
                        navigateToFD()
                    },
                    navigateToInsurance={
                            navController.navigate(Route.Insurance) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                    },
                    navigateToTradingAccountSetup=navigateToTradingAccountSetup,
                    navigateToInvestmentRateScreen=navigateToInvestmentRateScree
                )
            }
            composable<Route.FundScreener> {
                ExploreFundScreen(
                    onMFClick={navigateToCategoryMutualFundScreen()},
                    onFDClick=navigateToCategoryFDScreen,
                    navigateToSpecificMF = {mf->navigateToMutualFundDetailScreen(mf)},
                    navigateToSpecificFD = {fd->
                        navigateToFDDetailsScreen(fd)
                    }
                )
            }
            composable<Route.PortFolio> {
                PortfolioScreenMain(
                    viewModel = portfolioViewModel,
                    onFolioItemClick = {
                        navigateToSIPDetailsScreen(it)
                    },
                    onFDClick = navigateToPortfolioFdDetailsScreen,
                    navigateToCategoryMutualFundScreen=navigateToCategoryMutualFundScreen,
                    navigateToCategoryFDScreen=navigateToCategoryFDScreen
                )
            }
            composable<Route.Profile> {
                ProfileScreen(
                    navigateToNotification=navigateToNotification,
                    navigateToPersonalInfo=navigateToPersonalInfo,
                    navigateToKYC = navigateToKYC,
                    navigateToPrivacyPolicy=navigateToPrivacyPolicy,
                    navigateToTermsAndConditions=navigateToTermsAndConditions,
                    navigateToAboutUs=navigateToAboutUs,
                    navigateToAboutVelvet=navigateToAboutVelvet,
                    navigateToAboutFire=navigateToAboutFire,
                    onSignOut=onSignOut,
                    viewModel=homeViewModel
                )
            }
            composable<Route.Insurance> {
                InsuranceNavigationScreen(
                   navigateToHealthInsurance=navigateToHealthInsurance,
                   navigateToTermInsurance=navigateToTermInsurance,
                   navigateToOtherInsurance=navigateToOtherInsurance
                )
            }

        }

    }
}

private fun getRouteIndex(destination: NavDestination?): Int {
    if (destination == null) return -1
    return when {
        destination.hasRoute<Route.Home>() -> 0
        destination.hasRoute<Route.FundScreener>() -> 1
        destination.hasRoute<Route.PortFolio>() -> 2
        destination.hasRoute<Route.Insurance>() -> 3
        destination.hasRoute<Route.Profile>() -> 4
        else -> -1
    }
}
