package org.sharad.velvetinvestment.presentation.profile.compose.ProfileNew.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.sharad.emify.core.ui.theme.Orange
import org.sharad.emify.core.ui.theme.Primary
import org.sharad.emify.core.ui.theme.appGreen
import org.sharad.emify.core.ui.theme.appRed
import org.sharad.emify.core.ui.theme.titleColor
import org.sharad.velvetinvestment.domain.usecases.LaunchBrowserUseCase
import org.sharad.velvetinvestment.domain.usecases.user.SignOutUseCase
import org.sharad.velvetinvestment.presentation.homescreen.HomeScreenViewModel
import org.sharad.velvetinvestment.presentation.homescreen.uimodels.HomeScreenUiData
import org.sharad.velvetinvestment.presentation.profile.compose.ProfileNew.UImodel.PersonalInfoUiData
import org.sharad.velvetinvestment.presentation.profile.compose.ProfileNew.viewModel.PersonalInfoViewModel
import org.sharad.velvetinvestment.shared.UiStateContainer
import org.sharad.velvetinvestment.shared.compose.BarHeader
import org.sharad.velvetinvestment.shared.compose.ShadowCard
import org.sharad.velvetinvestment.shared.domain.models.OnboardingPrompt
import org.sharad.velvetinvestment.shared.theme.Poppins
import org.sharad.velvetinvestment.utils.SnackBarController
import org.sharad.velvetinvestment.utils.UiState
import org.sharad.velvetinvestment.utils.networking.onError
import org.sharad.velvetinvestment.utils.networking.onSuccess
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.back_icon
import velvet.composeapp.generated.resources.biometric
import velvet.composeapp.generated.resources.clipboard
import velvet.composeapp.generated.resources.fire_icon
import velvet.composeapp.generated.resources.ic_delete
import velvet.composeapp.generated.resources.info_icon
import velvet.composeapp.generated.resources.kyc_icon
import velvet.composeapp.generated.resources.kyc_person_icon
import velvet.composeapp.generated.resources.lock_outlined_icon
import velvet.composeapp.generated.resources.notification_icon
import velvet.composeapp.generated.resources.security
import velvet.composeapp.generated.resources.signupelement
import velvet.composeapp.generated.resources.termcondition

@Composable
fun ProfileScreen(
    navigateToNotification: () -> Unit,
    navigateToChangePin: () -> Unit = {},
    navigateToBiometricLogin: () -> Unit = {},
    /** Opens the KYC onboarding graph (JN `navigateToKYC`). */
    navigateToKYC: () -> Unit = {},
    navigateToPrivacyPolicy: () -> Unit = {},
    navigateToTermsAndConditions: () -> Unit = {},
    navigateToAboutUs: () -> Unit = {},
    navigateToAboutVelvet: () -> Unit = {},
    navigateToAboutFire: () -> Unit = {},
    onSignOut: () -> Unit,
    viewModel: HomeScreenViewModel,
    personalInfoViewModel: PersonalInfoViewModel = koinViewModel()
) {

    val state by viewModel.homeState.collectAsStateWithLifecycle()
    val personalInfoState by personalInfoViewModel.personalInfoState.collectAsStateWithLifecycle()
    val signOutUseCase = koinInject<SignOutUseCase>()
    val scope = rememberCoroutineScope()

    UiStateContainer(
        uiState = state,
        onRetry = { viewModel.loadHomeData() }
    ) { data: HomeScreenUiData ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            contentPadding = PaddingValues(bottom = 60.dp),
        ) {
            item {
                ProfileTopBar()
            }

            (personalInfoState as? UiState.Success)?.data?.let { info ->
                item {
                    ProfileIntroCard(info = info)
                }
            }

            item {
                BarHeader(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    "Preferences"
                )
            }

            item {
                PreferencesCard(
                    onNotificationClick = navigateToNotification
                )
            }

            item {
                BarHeader(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    "Security"
                )
            }

            item {
                SecurityCard(
                    onChangePinClick = navigateToChangePin,
                    onBiometricLoginClick = navigateToBiometricLogin
                )
            }

            item {
                BarHeader(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    "KYC"
                )
            }

            item {
                KYCCard(
                    onboardingPrompt = data.onboardingPrompt,
                    onKYCClick = navigateToKYC
                )
            }

            item {
                BarHeader(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    "Help Center"
                )
            }

            item {
                LegalCard(
                    onPrivacyPolicyClick = navigateToPrivacyPolicy,
                    onTermsAndConditionsClick = navigateToTermsAndConditions,
                    onAboutUsClick = navigateToAboutUs,
                    onAboutVelvetClick = navigateToAboutVelvet,
                    onAboutFireClick = navigateToAboutFire
                )
            }

            item {
                SignOutButton(
                    onClick = {
                        scope.launch {
                            signOutUseCase()
                                .onSuccess {
                                    onSignOut()
                                }
                                .onError {
                                    SnackBarController.showError(it.message)
                                }
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun ProfileTopBar() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top=8.dp),
        contentAlignment = Alignment.Center
    ) {


        Text(
            text = "Profile",
            fontWeight = FontWeight.SemiBold,
            fontFamily = Poppins,
            fontSize = 22.sp,
            color = Color(0xFF273E71)
        )

//        ShadowCard(
//            modifier = Modifier
//                .size(52.dp)
//                .align( Alignment.CenterEnd ),
//            shape = CircleShape,
//            onClick = {}
//        ) {
//            Icon(
//                painter = painterResource(Res.drawable.settings_icon),
//                contentDescription = "Setting Icon",
//                tint = Color(0xFFD8AF6B)
//            )
//        }
    }
}
/** Brief personal-info summary shown at the top of Profile (replaces the Personal Information screen). */
@Composable
fun ProfileIntroCard(info: PersonalInfoUiData) {
    ShadowCard(
        modifier = Modifier.padding(horizontal = 16.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(Primary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = info.fullName.initials(),
                        fontFamily = Poppins,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 20.sp,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = info.fullName.ifBlank { "Not Provided" },
                        fontFamily = Poppins,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 17.sp,
                        color = Primary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = info.email.ifBlank { "Not Provided" },
                        fontFamily = Poppins,
                        fontSize = 13.sp,
                        color = titleColor,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
private fun ProfileIntroField(label: String, value: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(
            text = label,
            fontFamily = Poppins,
            fontSize = 12.sp,
            color = Color.Gray
        )
        Text(
            text = value.ifBlank { "—" },
            fontFamily = Poppins,
            fontWeight = FontWeight.Medium,
            fontSize = 13.sp,
            color = Color.Black,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

private fun String.initials(): String =
    trim().split(Regex("\\s+"))
        .filter { it.isNotEmpty() }
        .take(2)
        .joinToString("") { it.first().uppercase() }
        .ifEmpty { "?" }

@Composable
fun PreferencesCard(onNotificationClick: () -> Unit) {
    ShadowCard(
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        Column(modifier = Modifier) {
            RowItem(
                icon = Res.drawable.notification_icon,
                title = "Notifications",
                subtitle = "Alert, updates & news",
                onCLick = onNotificationClick
            )
        }
    }
}

/** JN settings' "Security" group. */
@Composable
fun SecurityCard(onChangePinClick: () -> Unit, onBiometricLoginClick: () -> Unit) {
    ShadowCard(
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        Column(modifier = Modifier) {
            RowItem(
                icon = Res.drawable.lock_outlined_icon,
                title = "Change PIN",
                onCLick = onChangePinClick
            )

            HorizontalDivider(color = Color.LightGray)

            RowItem(
                icon = Res.drawable.biometric,
                title = "Biometric Login",
                onCLick = onBiometricLoginClick
            )
        }
    }
}

/**
 * JN profile's "KYC Status" row. Same rule as the home KYC card: a KYC under verification is
 * shown but leads nowhere; a pending or failed status opens the KYC onboarding flow.
 */
@Composable
fun KYCCard(onboardingPrompt: OnboardingPrompt, onKYCClick: () -> Unit) {
    ShadowCard(
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        Column(modifier = Modifier) {
            RowItem(
                icon = Res.drawable.kyc_icon,
                title = "KYC Status",
                subtitle = when (onboardingPrompt) {
                    OnboardingPrompt.NONE -> "Complete"
                    OnboardingPrompt.KYC_IN_PROGRESS -> "Under Review"
                    OnboardingPrompt.KYC_FAILED -> "Failed"
                    OnboardingPrompt.COMPLETE_KYC,
                    OnboardingPrompt.COMPLETE_REMAINING -> "Pending"
                },
                subtitleColor = when (onboardingPrompt) {
                    OnboardingPrompt.NONE -> appGreen
                    OnboardingPrompt.KYC_IN_PROGRESS -> Orange
                    OnboardingPrompt.KYC_FAILED,
                    OnboardingPrompt.COMPLETE_KYC,
                    OnboardingPrompt.COMPLETE_REMAINING -> appRed
                },
                onCLick = {
                    if (onboardingPrompt.opensOnboarding) onKYCClick()
                }
            )
        }
    }
}

@Composable
fun LegalCard(
    onPrivacyPolicyClick: () -> Unit = {},
    onTermsAndConditionsClick: () -> Unit = {},
    onAboutUsClick: () -> Unit,
    onAboutVelvetClick: () -> Unit = {},
    onAboutFireClick: () -> Unit = {}
) {
    val openBrowser: LaunchBrowserUseCase = koinInject()
    ShadowCard(
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        Column(modifier = Modifier) {
            RowItem(
                icon = Res.drawable.info_icon,
                title = "About Velvet",
                onCLick = onAboutVelvetClick
            )

            HorizontalDivider(color = Color.LightGray)

            RowItem(
                icon = Res.drawable.fire_icon,
                title = "About F.I.R.E. Report",
                onCLick = onAboutFireClick
            )

            HorizontalDivider(color = Color.LightGray)

            RowItem(
                icon = Res.drawable.termcondition,
                title = "Terms & Conditions",
                onCLick = onTermsAndConditionsClick
            )

            HorizontalDivider(color = Color.LightGray)

            RowItem(
                icon = Res.drawable.security,
                title = "Privacy Policy",
                onCLick = onPrivacyPolicyClick
            )

            HorizontalDivider(color = Color.LightGray)

            RowItem(
                icon = Res.drawable.kyc_person_icon,
                title = "Contact Us",
                onCLick = { openBrowser("https://velvetinvesting.com/contact") }
            )
            HorizontalDivider(color = Color.LightGray)

            RowItem(
                icon = Res.drawable.clipboard,
                title = "FAQs",
                onCLick = { openBrowser("https://velvetinvesting.com/faqs") }
            )

            HorizontalDivider(color = Color.LightGray)

            RowItem(
                icon = Res.drawable.ic_delete,
                title = "Delete Account",
                onCLick = { openBrowser("https://velvetinvesting.com/delete-account") }
            )

//            RowItem(
//                icon = Res.drawable.info_icon,
//                title = "About Us",
//                onCLick = onAboutUsClick
//            )
//
//            HorizontalDivider(color = Color.LightGray)

        }
    }
}

@Composable
fun RowItem(
    icon: DrawableResource,
    title: String,
    subtitle: String? = null,
    subtitleColor: Color = titleColor,
    onCLick: () -> Unit = {}
) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable { onCLick() },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
        )
        {

            Box(
                modifier = Modifier.size(40.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .background(
                        color = Color(0xFFDEE2F6),
                        shape = RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(icon),
                    contentDescription = title,
                    tint = Primary,
                    modifier = Modifier
                        .size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(20.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    fontFamily = Poppins,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )

                subtitle?.let {
                    Text(
                        text = it,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = Poppins,
                        color = subtitleColor
                    )
                }
            }
            Icon(
                painter = painterResource(Res.drawable.back_icon),
                contentDescription = "Forward Icon",
                tint = Color(0xFFD2B077)
            )
        }
    }
}

@Composable
fun SignOutButton(onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 35.dp)
    ) {
        Button(
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFF0600).copy(0.1f),
                contentColor = Color(0xFFFF0600)
            ),
            modifier = Modifier
                .weight(1f)
                .height(50.dp)
        ) {
            Icon(
                painter = painterResource(Res.drawable.signupelement),
                contentDescription = "Signup Button Icon"
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                "Sign Out",
                fontFamily = Poppins,
                fontSize = 20.sp
            )
        }
    }
}