package org.sharad.velvetinvestment.shared.Navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import org.sharad.velvetinvestment.domain.usecases.user.SignOutUseCase
import org.sharad.velvetinvestment.utils.AppEvent
import org.sharad.velvetinvestment.utils.AppEventsController
import org.sharad.velvetinvestment.utils.SnackBarController
import org.sharad.velvetinvestment.utils.WindowSize
import org.sharad.velvetinvestment.utils.isAndroid
import org.sharad.velvetinvestment.utils.storage.AuthPrefs

/**
 * The app's top-level graphs, after JN's `Navigation.kt`: login, the KYC onboarding, and the main
 * app. The FIRE questionnaire is a feature inside the main app, never a gate here.
 */
@Composable
fun BaseNavigation(windowSize: WindowSize, pv: PaddingValues) {

    val navController = rememberNavController()
    val prefs: AuthPrefs = koinInject()
    val signOutUseCase: SignOutUseCase = koinInject()
    // Held above the graph on purpose: the sign-out clean-up has to outlive the destination that
    // asked for it, which is torn down by the navigation that follows.
    val scope = rememberCoroutineScope()
    val isLoggedIn = prefs.isLoggedIn()

    // Set once the user belongs in the main app — either the server finished onboarding, or they
    // deferred the optional part of it and the app chases the rest from inside the main flow.
    val onboardingCompleted = prefs.isOnboardingCompleted()

    val startDestination: Any = when {
        !isLoggedIn -> Route.LoginGraph

        // The onboarding flow reads the stage to resume at from the server itself.
        !onboardingCompleted -> Route.OnboardingGraph()

        else -> Route.MainAppGraph
    }

    val isAndroid = remember { isAndroid() }

    val navigateToLogin: () -> Unit = {
        // Signing out has to forget that this process already cleared the app lock, or the next
        // user to log in on this device walks straight past the PIN screen.
        pinVerifiedThisSession = false
        navController.navigate(Route.LoginGraph) {
            launchSingleTop = true
            popUpTo(0)
        }
    }

    // Listened for here, above every graph, so a refresh token the server rejects signs the user
    // out wherever they are: main app, onboarding, or the KYC flow opened from the main app.
    LaunchedEffect(Unit) {
        AppEventsController.appEvent.collect {
            if (it != AppEvent.LogOut) return@collect
            AppEventsController.clear()
            // Several requests can fail their refresh at once; only the first finds a token to wipe.
            if (prefs.getBearerToken().isNullOrEmpty()) return@collect
            scope.launch {
                // Velvet's sign-out wipes the stored session and the client's cached tokens.
                signOutUseCase()
                navigateToLogin()
            }
            SnackBarController.showInfo("Token Expired. Login Again.")
        }
    }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = Modifier.padding(
            top = pv.calculateTopPadding(),
            bottom = if (isAndroid) pv.calculateBottomPadding() else 8.dp
        )
    ) {

        composable<Route.LoginGraph> {
            LoginNavigation(
                windowSize = windowSize,
                navigateToOnboardingGraph = {
                    navController.navigate(Route.OnboardingGraph()) {
                        launchSingleTop = true

                        popUpTo<Route.LoginGraph> {
                            inclusive = true
                        }
                    }
                },
                navigateToMainAppFlow = {
                    prefs.setLoggedIn(true)
                    prefs.setOnboardingCompleted(true)
                    navController.navigate(Route.MainAppGraph) {
                        launchSingleTop = true

                        popUpTo<Route.LoginGraph> {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable<Route.OnboardingGraph> {
            val route = it.toRoute<Route.OnboardingGraph>()
            OnboardingNavigation(
                // Reached from the nominees, from the investor profile on the KYC branch, from
                // skipping PAN, and when the server already reports onboarding as completed.
                onCompleted = {
                    prefs.setLoggedIn(true)
                    prefs.setOnboardingCompleted(true)
                    if (route.shouldRefresh) {
                        // The held scope, because this destination is popped right below.
                        scope.launch { AppEventsController.sendHomeRefreshEvent() }
                    }
                    navController.navigate(Route.MainAppGraph) {
                        launchSingleTop = true

                        popUpTo<Route.OnboardingGraph> {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable<Route.MainAppGraph> {
            AppNavigation(
                navigateToKYC = {
                    navController.navigate(Route.OnboardingGraph(shouldRefresh = true)) {
                        launchSingleTop = true
                    }
                },
                // The profile's log out button has already signed out by the time this runs.
                onSignOut = navigateToLogin
            )
        }
    }
}
