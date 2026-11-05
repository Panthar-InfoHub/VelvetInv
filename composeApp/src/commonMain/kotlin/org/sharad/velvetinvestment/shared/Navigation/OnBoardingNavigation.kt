package org.sharad.velvetinvestment.shared.Navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import org.sharad.velvetinvestment.domain.webview.WebViewConfig
import org.sharad.velvetinvestment.domain.webview.WebViewUrlMatchType
import org.sharad.velvetinvestment.features.core.domain.models.OnboardingStage
import org.sharad.velvetinvestment.features.onboarding.ui.compose.AddNomineeScreen
import org.sharad.velvetinvestment.features.onboarding.ui.compose.BasicDetailsScreen
import org.sharad.velvetinvestment.features.onboarding.ui.compose.ConfirmYourDetailsScreen
import org.sharad.velvetinvestment.features.onboarding.ui.compose.DigiLockerSplashScreen
import org.sharad.velvetinvestment.features.onboarding.ui.compose.EmailIdScreen
import org.sharad.velvetinvestment.features.onboarding.ui.compose.EmailOtpScreen
import org.sharad.velvetinvestment.features.onboarding.ui.compose.KycSplashScreen
import org.sharad.velvetinvestment.features.onboarding.ui.compose.NomineeOptOutTermsScreen
import org.sharad.velvetinvestment.features.onboarding.ui.compose.OnboardingStageLoaderScreen
import org.sharad.velvetinvestment.features.onboarding.ui.compose.ReviewProfileScreen
import org.sharad.velvetinvestment.features.onboarding.ui.compose.UploadSignatureScreen
import org.sharad.velvetinvestment.features.onboarding.ui.viewmodels.AddNomineeEffect
import org.sharad.velvetinvestment.features.onboarding.ui.viewmodels.AddNomineeViewModel
import org.sharad.velvetinvestment.features.onboarding.ui.viewmodels.BasicDetailsEffect
import org.sharad.velvetinvestment.features.onboarding.ui.viewmodels.BasicDetailsViewModel
import org.sharad.velvetinvestment.features.onboarding.ui.viewmodels.EmailIdEffect
import org.sharad.velvetinvestment.features.onboarding.ui.viewmodels.EmailIdViewModel
import org.sharad.velvetinvestment.features.onboarding.ui.viewmodels.EmailOtpEffect
import org.sharad.velvetinvestment.features.onboarding.ui.viewmodels.EmailOtpViewModel
import org.sharad.velvetinvestment.features.onboarding.ui.viewmodels.KycSplashEffect
import org.sharad.velvetinvestment.features.onboarding.ui.viewmodels.KycSplashViewModel
import org.sharad.velvetinvestment.features.onboarding.ui.viewmodels.OnboardingStageLoaderEffect
import org.sharad.velvetinvestment.features.onboarding.ui.viewmodels.OnboardingStageLoaderViewModel
import org.sharad.velvetinvestment.features.onboarding.ui.viewmodels.PanVerificationEffect
import org.sharad.velvetinvestment.features.onboarding.ui.viewmodels.PanVerificationViewModel
import org.sharad.velvetinvestment.features.onboarding.ui.viewmodels.ReviewProfileEffect
import org.sharad.velvetinvestment.features.onboarding.ui.viewmodels.ReviewProfileEvent
import org.sharad.velvetinvestment.features.onboarding.ui.viewmodels.ReviewProfileViewModel
import org.sharad.velvetinvestment.features.onboarding.ui.viewmodels.UploadSignatureEffect
import org.sharad.velvetinvestment.features.onboarding.ui.viewmodels.UploadSignatureViewModel
import org.sharad.velvetinvestment.features.onboarding.ui.viewmodels.VerifyBankAccountEffect
import org.sharad.velvetinvestment.features.onboarding.ui.viewmodels.VerifyBankAccountEvent
import org.sharad.velvetinvestment.features.onboarding.ui.viewmodels.VerifyBankAccountViewModel
import org.sharad.velvetinvestment.presentation.webview.UpiPaymentWebViewScreen
import org.sharad.velvetinvestment.presentation.webview.WebViewScreen
import org.sharad.velvetinvestment.utils.SnackBarController
import org.sharad.velvetinvestment.features.onboarding.ui.compose.VerifyBankAccountScreen
import org.sharad.velvetinvestment.features.onboarding.ui.viewmodels.VerifyWithDigilockerEffect
import org.sharad.velvetinvestment.features.onboarding.ui.viewmodels.VerifyWithDigilockerEvent
import org.sharad.velvetinvestment.features.onboarding.ui.viewmodels.VerifyWithDigilockerViewModel

/**
 * Set on whichever entry launched a web view once the user comes back, so that screen can
 * re-check the server and decide what happens next: DigiLocker (from KYC initiation), eSign
 * (from the investor profile) and the ₹1 bank verification payment all use it.
 */
private const val KYC_STEP_RESULT = "onboarding_kyc_step_completed"

/** Marks a web view that should pop back to its caller and flag [KYC_STEP_RESULT]. */
private const val WEBVIEW_COMPLETION_KYC_STEP = "onboarding_kyc_step"

/**
 * The redirects FP sends the KYC web views to when they finish — the backend's
 * `proof_details_callback_url` (DigiLocker) and `esign_callback_url` (eSign). Matched from the
 * start of the URL, as FP appends its result as a query string. Either callback closes the page
 * whatever its status; the screen behind then reads the real form status from the server.
 */
private const val DIGILOCKER_PROOF_CALLBACK = "https://myapp.com/proof_details_callback"
private const val ESIGN_CALLBACK = "https://myapp.com/esign_callback"

/**
 * Set on the bank step to the UPI app link its payment page led to. The page closes on it without
 * opening it; the bank step opens the app itself and watches for the user's return.
 */
private const val UPI_APP_LINK_RESULT = "onboarding_upi_app_link"

/**
 * Every session starts on [Route.OnboardingStageLoader], which reads `current_stage` from the
 * server and moves on to that stage's first screen. Callers pass no stage, and nothing local is
 * consulted.
 */
@Composable
fun OnboardingNavigation(
    onCompleted: () -> Unit
) {

    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { pv ->
        NavHost(
            navController = navController,
            startDestination = Route.OnboardingStageLoader,
            modifier = Modifier.fillMaxSize()
        ) {
            // ── 0. Entry: load current_stage from the server ───────────────────────────────────
            composable<Route.OnboardingStageLoader> {
                val vm: OnboardingStageLoaderViewModel = koinViewModel()
                LaunchedEffect(Unit) {
                    vm.effect.collect { effect ->
                        when (effect) {
                            is OnboardingStageLoaderEffect.StageResolved -> {
                                val destination = effect.stage.firstRoute(effect.isKycRequired)
                                if (destination == null) {
                                    onCompleted()
                                } else {
                                    navController.navigate(destination) {
                                        // Only a loader; back from the first screen leaves.
                                        popUpTo<Route.OnboardingStageLoader> { inclusive = true }
                                        launchSingleTop = true
                                    }
                                }
                            }
                        }
                    }
                }

                val state by vm.uiState.collectAsStateWithLifecycle()
                OnboardingStageLoaderScreen(
                    state = state,
                    handleEvent = vm::handleEvent
                )
            }

            // ── 1. Basic details ─────────────────────────────────────────────────────────────────
            composable<Route.OnboardingBasicDetails> {
                val vm: BasicDetailsViewModel = koinViewModel()
                LaunchedEffect(Unit) {
                    vm.effect.collect { effect ->
                        when (effect) {
                            BasicDetailsEffect.NavigateToPanInitiate -> {
                                navController.navigate(Route.OnboardingPANVerification){
                                    launchSingleTop=true
                                }
                            }
                        }
                    }
                }

                val state by vm.uiState.collectAsStateWithLifecycle()
                BasicDetailsScreen(
                    state = state,
                    handleEvent = vm::handleEvent
                )
            }

            // ── 2. PAN — verified → 4. Email · KYC required → 3. KYC · skip → main app ───────────
            composable<Route.OnboardingPANVerification> {
                val vm: PanVerificationViewModel = koinViewModel()
                LaunchedEffect(Unit) {
                    vm.effect.collect { effect ->
                        when (effect) {
                            // A verified PAN needs no KYC: email, profile, bank, then nominees.
                            PanVerificationEffect.PanVerified -> {
                                navController.navigate(Route.OnboardingEmail(isKycRequired = false)) {
                                    launchSingleTop = true
                                }
                            }
                            PanVerificationEffect.NavigateToKycInitiate -> {
                                navController.navigate(Route.OnboardingKYCInitiation){
                                    launchSingleTop= true
                                }
                            }
                            PanVerificationEffect.OpenPrivacyUrl -> {}
                            PanVerificationEffect.OpenReadMoreUrl -> {}
                            PanVerificationEffect.OpenTermsUrl -> {}
                            PanVerificationEffect.SkipPanVerification -> {
                                SnackBarController.showSuccess("KYC Skipped. Please complete the remaining steps before making fund purchases.")
                                onCompleted()
                            }
                        }
                    }
                }

                val state by vm.uiState.collectAsStateWithLifecycle()
                ConfirmYourDetailsScreen(
                    state = state,
                    handleEvent = vm::handleEvent
                )
            }

            // ── 3. KYC (PAN not verified only): DigiLocker → signature → 4. Email ────────────────
            composable<Route.OnboardingKYCInitiation> { entry ->
                val vm: VerifyWithDigilockerViewModel = koinViewModel()

                val stepReturned by entry.savedStateHandle
                    .getStateFlow(KYC_STEP_RESULT, false)
                    .collectAsStateWithLifecycle()

                LaunchedEffect(stepReturned) {
                    if (stepReturned) {
                        entry.savedStateHandle[KYC_STEP_RESULT] = false
                        vm.handleEvent(VerifyWithDigilockerEvent.OnStepReturned)
                    }
                }

                LaunchedEffect(Unit){
                    vm.effect.collect { effect ->
                        when(effect){
                            is VerifyWithDigilockerEffect.OpenWebView -> {
                                navController.navigate(
                                    Route.WebViewScreen(
                                        url = effect.url,
                                        // FP redirects here once DigiLocker is done, e.g.
                                        // .../proof_details_callback?identity_document=…&status=…;
                                        // OnStepReturned then re-reads the real form status.
                                        exitUrlPatterns = listOf(DIGILOCKER_PROOF_CALLBACK),
                                        title = effect.title,
                                        completionRouteKey = WEBVIEW_COMPLETION_KYC_STEP
                                    )
                                )
                            }

                            VerifyWithDigilockerEffect.ProofVerified -> {
                                navController.navigate(Route.OnboardingSignatureUpload) {
                                    launchSingleTop = true
                                }
                            }
                        }
                    }
                }

                val state by vm.uiState.collectAsStateWithLifecycle()
                DigiLockerSplashScreen(
                    state = state,
                    handleEvent = vm::handleEvent
                )
            }

            composable<Route.OnboardingSignatureUpload> {
                val vm: UploadSignatureViewModel = koinViewModel()

                LaunchedEffect(Unit) {
                    vm.effect.collect { effect ->
                        when (effect) {
                            // The last of the KYC branch's own steps; email and profile follow.
                            UploadSignatureEffect.SignatureUploaded -> {
                                navController.navigate(Route.OnboardingEmail(isKycRequired = true)) {
                                    launchSingleTop = true
                                }
                            }
                        }
                    }
                }

                val state by vm.uiState.collectAsStateWithLifecycle()
                UploadSignatureScreen(
                    state = state,
                    onEvent = vm::handleEvent,
                    onBack = { navController.popBackStack() }
                )
            }

            // ── 4. Email: address → OTP → 5. Investor profile ────────────────────────────────────
            composable<Route.OnboardingEmail> { entry ->
                val route = entry.toRoute<Route.OnboardingEmail>()
                val vm: EmailIdViewModel = koinViewModel()
                LaunchedEffect(Unit) {
                    vm.effect.collect { effect ->
                        when (effect) {
                            is EmailIdEffect.OtpRequested -> {
                                navController.navigate(
                                    Route.OnboardingEmailOtp(
                                        email = effect.email,
                                        isKycRequired = route.isKycRequired
                                    )
                                ) {
                                    launchSingleTop = true
                                }
                            }
                        }
                    }
                }

                val state by vm.uiState.collectAsStateWithLifecycle()
                EmailIdScreen(
                    state = state,
                    handleEvent = vm::handleEvent
                )
            }

            composable<Route.OnboardingEmailOtp> { entry ->
                val route = entry.toRoute<Route.OnboardingEmailOtp>()
                val vm: EmailOtpViewModel = koinViewModel(
                    parameters = { parametersOf(route.email) }
                )

                LaunchedEffect(Unit) {
                    vm.effect.collect { effect ->
                        when (effect) {
                            EmailOtpEffect.EmailVerified -> {
                                navController.navigate(
                                    Route.OnboardingProfile(isKycRequired = route.isKycRequired)
                                ) {
                                    // The address is confirmed — going back to re-enter the code
                                    // would only hit an already-consumed OTP.
                                    popUpTo<Route.OnboardingEmail> { inclusive = true }
                                    launchSingleTop = true
                                }
                            }

                            EmailOtpEffect.NavigateBack -> navController.popBackStack()
                        }
                    }
                }

                val state by vm.uiState.collectAsStateWithLifecycle()
                EmailOtpScreen(
                    state = state,
                    handleEvent = vm::handleEvent
                )
            }

            // ── 5. Investor profile — KYC branch → main app · verified PAN → 6. Bank ─────────────
            composable<Route.OnboardingProfile> { entry ->
                val route = entry.toRoute<Route.OnboardingProfile>()
                val vm: ReviewProfileViewModel = koinViewModel()

                val eSignReturned by entry.savedStateHandle
                    .getStateFlow(KYC_STEP_RESULT, false)
                    .collectAsStateWithLifecycle()

                LaunchedEffect(eSignReturned) {
                    if (eSignReturned) {
                        entry.savedStateHandle[KYC_STEP_RESULT] = false
                        vm.handleEvent(ReviewProfileEvent.OnESignReturned)
                    }
                }

                LaunchedEffect(Unit) {
                    vm.effect.collect { effect ->
                        when (effect) {
                            is ReviewProfileEffect.OpenESignWebView -> {
                                navController.navigate(
                                    Route.WebViewScreen(
                                        url = effect.url,
                                        // FP redirects here once signing is done; OnESignReturned
                                        // then re-reads the real form status.
                                        exitUrlPatterns = listOf(ESIGN_CALLBACK),
                                        title = "eSign Verification",
                                        completionRouteKey = WEBVIEW_COMPLETION_KYC_STEP
                                    )
                                )
                            }

                            // The KYC branch leaves for the main app here; its bank and nominee
                            // steps are picked up from there later.
                            ReviewProfileEffect.ProfileCompleted -> {
                                if (route.isKycRequired) {
                                    onCompleted()
                                } else {
                                    navController.navigate(Route.OnboardingBankVerification) {
                                        launchSingleTop = true
                                    }
                                }
                            }

                            ReviewProfileEffect.NavigateBack -> {
                                navController.popBackStack()
                            }
                        }
                    }
                }

                val state by vm.uiState.collectAsStateWithLifecycle()
                ReviewProfileScreen(
                    state = state,
                    handleEvent = vm::handleEvent
                )
            }

            // ── 6. Bank verification (verified PAN only) → 7. Nominee ────────────────────────────
            // The whole bank stage — prefill check, ₹1 reverse penny drop, payment page and the
            // bank form — lives under this one destination and its view model. Every way in
            // starts the flow from the top; the only way out is the form's PennyDropCompleted.
            // Only the verified-PAN branch comes through here, after the investor profile.
            composable<Route.OnboardingBankVerification> { entry ->
                val vm: VerifyBankAccountViewModel = koinViewModel()

                // Back from the ₹1 payment page: only the server knows whether it went through.
                val paymentPageReturned by entry.savedStateHandle
                    .getStateFlow(KYC_STEP_RESULT, false)
                    .collectAsStateWithLifecycle()

                LaunchedEffect(paymentPageReturned) {
                    if (paymentPageReturned) {
                        entry.savedStateHandle[KYC_STEP_RESULT] = false
                        vm.handleEvent(VerifyBankAccountEvent.OnPaymentPageReturned)
                    }
                }

                // The payment page closed on a UPI app link; the app picker opens it.
                val upiAppLink by entry.savedStateHandle
                    .getStateFlow<String?>(UPI_APP_LINK_RESULT, null)
                    .collectAsStateWithLifecycle()

                LaunchedEffect(upiAppLink) {
                    upiAppLink?.let { url ->
                        entry.savedStateHandle[UPI_APP_LINK_RESULT] = null
                        vm.handleEvent(VerifyBankAccountEvent.OnUpiAppLinkCaught(url))
                    }
                }

                LaunchedEffect(Unit) {
                    vm.effect.collect { effect ->
                        when (effect) {
                            VerifyBankAccountEffect.PennyDropCompleted -> {
                                navController.navigate(Route.OnboardingNominee) {
                                    launchSingleTop = true
                                }
                            }

                            VerifyBankAccountEffect.NavigateToChangeBankAccount -> {}

                            VerifyBankAccountEffect.NavigateBack -> navController.popBackStack()

                            is VerifyBankAccountEffect.OpenPaymentPage -> {
                                navController.navigate(
                                    Route.UpiPaymentWebView(
                                        url = effect.url,
                                        title = "Bank Verification",
                                        completionRouteKey = WEBVIEW_COMPLETION_KYC_STEP
                                    )
                                )
                            }
                        }
                    }
                }

                val state by vm.uiState.collectAsStateWithLifecycle()
                VerifyBankAccountScreen(
                    state = state,
                    handleEvent = vm::handleEvent
                )
            }

            // ── 7. Nominee → main app ────────────────────────────────────────────────────────────
            composable<Route.OnboardingNominee> {
                val vm: AddNomineeViewModel = koinViewModel()

                LaunchedEffect(Unit) {
                    vm.effect.collect { effect ->
                        when (effect) {
                            // The nominees are the last step; autopay is raised per SIP now.
                            AddNomineeEffect.NomineesSubmitted -> onCompleted()
                        }
                    }
                }

                val state by vm.uiState.collectAsStateWithLifecycle()
                AddNomineeScreen(
                    state = state,
                    handleEvent = vm::handleEvent,
                    onOptOutTermsClick = {
                        navController.navigate(Route.NomineeOptOutTerms) {
                            launchSingleTop = true
                        }
                    }
                )
            }

            composable<Route.NomineeOptOutTerms> {
                NomineeOptOutTermsScreen(
                    onBackClick = { navController.popBackStack() }
                )
            }

            // ── Shared web views (DigiLocker, eSign, ₹1 bank payment) ────────────────────────────
            composable<Route.WebViewScreen> { entry ->
                val route = entry.toRoute<Route.WebViewScreen>()

                val onWebViewDone: () -> Unit = {
                    when (route.completionRouteKey) {
                        // Each hand-off returns to the screen that launched it, which then asks
                        // the server what actually changed.
                        WEBVIEW_COMPLETION_KYC_STEP -> navController.returnToCallerWithStepResult()
                        else -> navController.popBackStack()
                    }
                }

                WebViewScreen(
                    config = WebViewConfig(
                        url = route.url,
                        exitUrlPatterns = route.exitUrlPatterns,
                        matchType = WebViewUrlMatchType.valueOf(route.matchType),
                        title = route.title
                    ),
                    onExitUrlReached = { onWebViewDone() },
                    onBackClick = { onWebViewDone() },
                    interceptExternalAppUrls = route.interceptExternalAppUrls
                )
            }

            composable<Route.UpiPaymentWebView> { entry ->
                val route = entry.toRoute<Route.UpiPaymentWebView>()

                val onWebViewDone: () -> Unit = {
                    when (route.completionRouteKey) {
                        WEBVIEW_COMPLETION_KYC_STEP -> navController.returnToCallerWithStepResult()
                        else -> navController.popBackStack()
                    }
                }

                UpiPaymentWebViewScreen(
                    config = WebViewConfig(
                        url = route.url,
                        title = route.title
                    ),
                    // Nothing has been paid yet — only the app to pay in is known — so this hands
                    // the link back rather than flagging the step for a status check.
                    onAppLinkCaught = { url ->
                        navController.previousBackStackEntry
                            ?.savedStateHandle
                            ?.set(UPI_APP_LINK_RESULT, url)
                        navController.popBackStack()
                    },
                    onExitUrlReached = { onWebViewDone() },
                    onBackClick = { onWebViewDone() }
                )
            }

            // ── Legacy: not reached from the current flow ────────────────────────────────────────
            composable<Route.OnboardingKYCSplash> {
                val vm: KycSplashViewModel = koinViewModel()
                LaunchedEffect(Unit){
                    vm.effect.collect { effect ->
                        when(effect){
                            KycSplashEffect.OnProceedClick -> {
                                navController.navigate(Route.OnboardingPANVerification){
                                    launchSingleTop=true
                                }
                            }
                        }
                    }
                }

                KycSplashScreen(
                    handleEvent = vm::handleEvent
                )
            }
        }
    }
}

/**
 * The first screen of each stage's step, in flow order. Null for [OnboardingStage.Completed],
 * where onboarding simply finishes. Only Email and Profile use [isKycRequired]: up to PAN the PAN
 * screen picks the branch, and the KYC step passes it on itself.
 */
private fun OnboardingStage.firstRoute(isKycRequired: Boolean) = when (this) {
    OnboardingStage.BasicDetails -> Route.OnboardingBasicDetails
    OnboardingStage.PanVerification -> Route.OnboardingPANVerification
    OnboardingStage.KycVerification -> Route.OnboardingKYCInitiation
    OnboardingStage.EmailVerification -> Route.OnboardingEmail(isKycRequired)
    OnboardingStage.InvestorProfile -> Route.OnboardingProfile(isKycRequired)
    OnboardingStage.PennyDropVerification -> Route.OnboardingBankVerification
    OnboardingStage.NomineeAddition -> Route.OnboardingNominee
    OnboardingStage.Completed -> null
}

/** Pops back to whichever screen launched the web view and flags that its step is done. */
private fun NavHostController.returnToCallerWithStepResult() {
    previousBackStackEntry?.savedStateHandle?.set(KYC_STEP_RESULT, true)
    popBackStack()
}
