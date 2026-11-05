package org.sharad.velvetinvestment.features.auth.ui.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import org.sharad.emify.core.ui.theme.Secondary
import org.sharad.emify.core.ui.theme.appRed
import org.sharad.emify.core.ui.theme.titleColor
import org.sharad.velvetinvestment.features.auth.ui.viewmodels.EnterPinEvent
import org.sharad.velvetinvestment.features.auth.ui.viewmodels.EnterPinUiState
import org.sharad.velvetinvestment.features.core.biometric.rememberBiometricAuthenticator
import org.sharad.velvetinvestment.features.core.ui.otp.OtpUiState
import org.sharad.velvetinvestment.shared.compose.BackHeader
import org.sharad.velvetinvestment.shared.compose.OtpGrid
import org.sharad.velvetinvestment.shared.theme.VelvetTheme
import org.sharad.velvetinvestment.shared.theme.titlesStyle
import org.sharad.velvetinvestment.utils.AppBackHandler
import org.sharad.velvetinvestment.utils.clearFocusOnTap
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.logo_app

/**
 * The app lock on a cold start, and the current-PIN gate in front of Change PIN. Verification
 * fires as soon as the fourth digit is typed — there is no submit button (JN).
 */
@Composable
fun EnterPinScreen(
    state: EnterPinUiState,
    onEvent: (EnterPinEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    AppBackHandler(enabled = state.showBack) { onEvent(EnterPinEvent.OnBackClicked) }

    val biometricAuthenticator = rememberBiometricAuthenticator()
    val currentOnEvent by rememberUpdatedState(onEvent)

    LaunchedEffect(state.biometricPromptRequested) {
        if (!state.biometricPromptRequested) return@LaunchedEffect
        currentOnEvent(EnterPinEvent.OnBiometricPromptShown)
        if (biometricAuthenticator.isBiometricAvailable()) {
            biometricAuthenticator.authenticate(
                title = "Authentication Required",
                subtitle = "Authenticate to access Velvet",
                description = "Use your biometrics to continue",
                onSuccess = { currentOnEvent(EnterPinEvent.OnBiometricSuccess) },
                onError = { message -> currentOnEvent(EnterPinEvent.OnBiometricError(message)) }
            )
        }
    }

    Scaffold(containerColor = Color.White) { padding ->
        Column(modifier = modifier.fillMaxSize().padding(padding)) {
            if (state.showBack) {
                BackHeader(
                    title = "",
                    showBack = true,
                    onBack = { onEvent(EnterPinEvent.OnBackClicked) }
                )
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .clearFocusOnTap()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(32.dp),
                contentPadding = PaddingValues(top = if (state.showBack) 16.dp else 64.dp)
            ) {
                item {
                    Image(
                        painter = painterResource(Res.drawable.logo_app),
                        contentDescription = null,
                        modifier = Modifier.size(80.dp)
                    )
                }

                item {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = if (state.userName.isBlank()) "Hi" else "Hi, ${state.userName}",
                            style = MaterialTheme.typography.headlineSmall,
                            color = Color.Black,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = state.subtitle,
                            style = titlesStyle,
                            color = titleColor,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                item {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        OtpGrid(
                            modifier = Modifier,
                            otpLength = state.otp.otpLength,
                            otp = state.otp.otpValue,
                            onOtpChange = { onEvent(EnterPinEvent.OnPinChanged(it)) },
                            autoFocus = true,
                            horizontalArrangement = Arrangement.spacedBy(
                                12.dp,
                                Alignment.CenterHorizontally
                            )
                        )
                        if (state.verifying) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = Secondary,
                                strokeWidth = 2.dp
                            )
                        }
                        state.errorMessage?.let { message ->
                            Text(
                                text = message,
                                style = titlesStyle.copy(fontSize = 12.sp),
                                color = appRed,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                if (state.biometricsAllowed) item {
                    Text(
                        text = "Use Biometrics",
                        style = titlesStyle.copy(fontWeight = FontWeight.Medium),
                        color = Secondary,
                        modifier = Modifier.clickable {
                            onEvent(EnterPinEvent.OnUseBiometricsClicked)
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EnterPinScreenPreview() {
    VelvetTheme {
        EnterPinScreen(
            state = EnterPinUiState(
                userName = "Ankit Bose",
                otp = OtpUiState(otpValue = "12", otpLength = 4),
                subtitle = "Enter your Velvet PIN",
                errorMessage = "Incorrect PIN",
                // The preview has no platform prompt to drive.
                biometricPromptRequested = false
            ),
            onEvent = {}
        )
    }
}
