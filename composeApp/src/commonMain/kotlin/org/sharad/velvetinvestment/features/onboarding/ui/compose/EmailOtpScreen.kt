package org.sharad.velvetinvestment.features.onboarding.ui.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.sharad.velvetinvestment.features.core.ui.otp.OtpSentToSubtitle
import org.sharad.velvetinvestment.features.core.ui.otp.OtpUiState
import org.sharad.velvetinvestment.features.core.ui.otp.OtpVerificationScreen
import org.sharad.velvetinvestment.features.onboarding.ui.viewmodels.EmailOtpEvent
import org.sharad.velvetinvestment.features.onboarding.ui.viewmodels.EmailOtpUiState
import org.sharad.velvetinvestment.shared.theme.VelvetTheme

/**
 * Onboarding's configuration of the shared [OtpVerificationScreen]. Only the copy and the event
 * mapping differ from login's `EnterOtpScreen`.
 */
@Composable
fun EmailOtpScreen(
    state: EmailOtpUiState,
    handleEvent: (EmailOtpEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    OtpVerificationScreen(
        state = state.otp,
        title = "Verify your email ID",
        submitText = "Verify",
        onOtpChange = { handleEvent(EmailOtpEvent.OnOtpChanged(it)) },
        onSubmit = { handleEvent(EmailOtpEvent.OnVerifyClicked) },
        onResend = { handleEvent(EmailOtpEvent.OnResendClicked) },
        onBack = { handleEvent(EmailOtpEvent.OnBackClicked) },
        modifier = modifier,
        resendText = "Resend Code",
        resendTimerText = "You can resend the code in ${state.otp.resendTimerSeconds} seconds",
        subtitle = {
            OtpSentToSubtitle(
                destination = state.email,
                text = "A verification code has been sent to"
            )
        }
    )
}

@Preview(showBackground = true, locale = "hi")
@Composable
fun EmailOtpScreenPreview() {
    VelvetTheme {
        EmailOtpScreen(
            state = EmailOtpUiState(
                email = "khiwatari12@gmail.com",
                otp = OtpUiState(otpValue = "746", resendTimerSeconds = 0)
            ),
            handleEvent = {}
        )
    }
}
