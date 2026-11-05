package org.sharad.velvetinvestment.features.core.ui.otp

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.sharad.emify.core.ui.theme.Primary
import org.sharad.emify.core.ui.theme.TextGray
import org.sharad.velvetinvestment.shared.compose.AppBackButton
import org.sharad.velvetinvestment.shared.compose.AppButton
import org.sharad.velvetinvestment.utils.clearFocusOnTap

@Composable
fun OtpVerificationScreen(
    state: OtpUiState,
    title: String,
    submitText: String,
    onOtpChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onResend: () -> Unit,
    modifier: Modifier = Modifier,
    onBack: (() -> Unit)? = null,
    resendText: String = "Resend Code",
    resendTimerText: String = "Resend code in ${state.resendTimerSeconds}s",
    subtitle: @Composable ColumnScope.() -> Unit = {},
    footer: @Composable ColumnScope.() -> Unit = {}
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Scaffold(modifier = modifier.clearFocusOnTap()) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                if (onBack != null) {
                    AppBackButton(onClick = onBack)
                }

                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                subtitle()

                OtpInputField(
                    otpValue = state.otpValue,
                    otpLength = state.otpLength,
                    onValueChange = onOtpChange,
                    focusRequester = focusRequester,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                if (state.resendTimerSeconds == 0) {
                    Text(
                        text = resendText,
                        color = Primary,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.clickable(
                            onClick = onResend,
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        )
                    )
                } else {
                    Text(
                        text = resendTimerText,
                        color = TextGray,
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.padding(bottom = 24.dp)
                    )
                }

                AppButton(
                    text = submitText,
                    onClick = onSubmit,
                    enabled = state.isSubmitEnabled,
                    loading = state.isLoading,
                    modifier = Modifier.fillMaxWidth().padding(top = 28.dp)
                )
            }

            footer()
        }
    }
}

@Composable
fun ColumnScope.OtpSentToSubtitle(
    destination: String? = null,
    text: String = "A verification code has been sent to"
) {
    Text(
        text = text,
        color = TextGray,
        style = MaterialTheme.typography.titleSmall
    )
    destination?.let {
        Text(
            text = destination,
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
        )
    }
    Spacer(
        modifier = Modifier.padding(bottom = 24.dp, top = 4.dp)
    )
}
