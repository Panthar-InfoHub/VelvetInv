package org.sharad.velvetinvestment.features.portfolio.ui.screens

import org.sharad.velvetinvestment.shared.theme.VelvetTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import velvet.composeapp.generated.resources.Res
import org.sharad.velvetinvestment.features.core.ui.otp.OtpSentToSubtitle
import org.sharad.velvetinvestment.features.core.ui.otp.OtpUiState
import org.sharad.velvetinvestment.features.core.ui.otp.OtpVerificationScreen
import org.sharad.velvetinvestment.features.portfolio.ui.viewmodel.RedeemOtpEvent
import org.sharad.velvetinvestment.features.portfolio.ui.viewmodel.RedeemOtpUiState

/**
 * The redemption's configuration of the shared [OtpVerificationScreen]. Only the copy and the
 * event mapping differ from the other OTP screens in the app.
 */
@Composable
fun RedeemOtpScreen(
    state: RedeemOtpUiState,
    handleEvent: (RedeemOtpEvent) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    OtpVerificationScreen(
        state = state.otp,
        title = "Confirm your redemption",
        submitText = "Verify",
        onOtpChange = { handleEvent(RedeemOtpEvent.OnOtpChanged(it)) },
        onSubmit = { handleEvent(RedeemOtpEvent.OnVerifyClicked) },
        onResend = { handleEvent(RedeemOtpEvent.OnResendClicked) },
        onBack = onBack,
        modifier = modifier,
        resendText = "Resend Code",
        resendTimerText = "You can resend the code in ${state.otp.resendTimerSeconds} seconds",
        subtitle = {
            OtpSentToSubtitle(
                destination = null,
                text = "A verification code has been sent to"
            )
        }
    )
}

@Preview(showBackground = true, locale = "hi")
@Composable
private fun RedeemOtpScreenPreview() {
    VelvetTheme {
        RedeemOtpScreen(
            state = RedeemOtpUiState(
                otp = OtpUiState(otpValue = "12", resendTimerSeconds = 18, otpLength = 6)
            ),
            handleEvent = {},
            onBack = {}
        )
    }
}
