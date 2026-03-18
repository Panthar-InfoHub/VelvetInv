package org.sharad.velvetinvestment.shared.Navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import org.koin.compose.koinInject
import org.sharad.velvetinvestment.presentation.onboarding.compose.OnboardingScreenRoot
import org.sharad.velvetinvestment.utils.WindowSize
import org.sharad.velvetinvestment.utils.storage.AuthPrefs


@Composable
fun BaseNavigation(windowSize: WindowSize, pv: PaddingValues) {

    val navController=rememberNavController()
    val prefs: AuthPrefs = koinInject()
    val isLoggedIn = prefs.isLoggedIn()
    val onboardingCompleted = prefs.isOnboardingCompleted()
    val onboardingStep = prefs.getOnboardingStep()

    val startDestination = when {
        !isLoggedIn -> Route.LoginFlow
        !onboardingCompleted -> Route.OnBoardingFlow(onboardingStep)
        else -> Route.MainAppFlow
    }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = Modifier.padding(top=pv.calculateTopPadding())
    ){
        composable<Route.LoginFlow>{
            LoginNavigation(
                windowSize=windowSize,
                onLoginSuccessNavigation = {navController.navigate(Route.MainAppFlow){
                    popUpTo(Route.MainAppFlow) {inclusive=true  }
                } },
                navigateToOnboarding={it->
                    navController.navigate(Route.OnBoardingFlow(it)){
                        popUpTo(0){inclusive=true}
                    }
                }
            )
        }

        composable<Route.OnBoardingFlow> {
            val index = it.toRoute<Route.OnBoardingFlow>().index
            OnboardingScreenRoot(
                onLoginSuccessNavigation = {
                    navController.navigate(Route.MainAppFlow) {
                        popUpTo(Route.MainAppFlow) { inclusive = true }
                    }
                }
            )
        }

        composable<Route.MainAppFlow> {
            AppNavigation()
        }
    }

}