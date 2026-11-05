package org.sharad.velvetinvestment.features.plans.ui.compose

import org.sharad.velvetinvestment.shared.theme.titlesStyle
import org.sharad.emify.core.ui.theme.titleColor
import org.sharad.emify.core.ui.theme.lightGray
import androidx.compose.ui.unit.dp
import org.sharad.velvetinvestment.shared.theme.subHeading
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import org.sharad.emify.core.ui.theme.Black
import org.sharad.emify.core.ui.theme.Gray444
import org.sharad.emify.core.ui.theme.Secondary
import org.sharad.velvetinvestment.features.plans.ui.viewmodels.FundPurchaseEvent
import org.sharad.velvetinvestment.features.plans.ui.viewmodels.FundPurchaseUiState
import org.sharad.velvetinvestment.features.plans.ui.viewmodels.OTP_LENGTH
import org.sharad.velvetinvestment.shared.compose.AppButton
import org.sharad.velvetinvestment.shared.compose.AppButtonDefaults
import org.sharad.velvetinvestment.shared.theme.Spacing
import org.sharad.velvetinvestment.shared.theme.VelvetTheme
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.arrow_front_icon

/**
 * Confirms the purchase with the OTP the gateway just sent.
 *
 * Entering the OTP *is* the user's authorisation for the transaction, so the sheet is bilingual
 * throughout — the consent has to be understood, not just tapped through.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PurchaseOtpSheet(
    state: FundPurchaseUiState,
    handleEvent: (FundPurchaseEvent) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = { handleEvent(FundPurchaseEvent.OnOtpSheetDismiss) },
        sheetState = sheetState,
        containerColor = White,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 24.dp,
                    end = 24.dp,
                    bottom = 32.dp
                )
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                text = "Verify Your Mutual Fund Purchase",
                style = MaterialTheme.typography.headlineSmall,
                color = Black
            )

            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "A verification code has been sent to",
                    style = titlesStyle,
                    color = Gray444
                )
                if (state.otpDestination.isNotBlank()) {
                    Text(
                        text = state.otpDestination,
                        style = subHeading,
                        color = Black
                    )
                }
            }

            OtpBoxes(
                otp = state.otp,
                onOtpChange = { handleEvent(FundPurchaseEvent.OnOtpChange(it)) }
            )

            ResendRow(
                secondsLeft = state.resendSecondsLeft,
                onResendClick = { handleEvent(FundPurchaseEvent.OnResendOtpClick) }
            )

            AppButton(
                text = "Confirm",
                onClick = { handleEvent(FundPurchaseEvent.OnConfirmOtpClick) },
                loading = state.isVerifyingOtp,
                enabled = state.isOtpComplete,
                trailingIcon = Res.drawable.arrow_front_icon,
                style = AppButtonDefaults.style(height = 58.dp),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

/**
 * [OTP_LENGTH] circles fed by a single hidden text field — the same approach as the login OTP
 * screen, which keeps IME and autofill behaviour on one real input.
 */
@Composable
private fun OtpBoxes(
    otp: String,
    onOtpChange: (String) -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    BasicTextField(
        value = otp,
        onValueChange = onOtpChange,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = Modifier.focusRequester(focusRequester),
        decorationBox = {}
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        repeat(OTP_LENGTH) { index ->
            val char = otp.getOrNull(index)

            Box(
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f)
                    .clip(CircleShape)
                    .border(
                        width = 1.dp,
                        color = if (char != null) Secondary else lightGray,
                        shape = CircleShape
                    )
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        focusRequester.requestFocus()
                        keyboardController?.show()
                    },
                contentAlignment = Alignment.Center
            ) {
                if (char != null) {
                    Text(
                        text = char.toString(),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        color = Black
                    )
                }
            }
        }
    }
}

/** Counts down before the resend link becomes live, so the gateway is not hammered. */
@Composable
private fun ResendRow(
    secondsLeft: Int,
    onResendClick: () -> Unit
) {
    if (secondsLeft > 0) {
        Text(
            text = "You can resend the code in $secondsLeft seconds",
            style = titlesStyle,
            color = titleColor
        )
    } else {
        Text(
            text = "Resend code",
            style = MaterialTheme.typography.displayLarge.copy(
                fontWeight = FontWeight.SemiBold
            ),
            color = Secondary,
            modifier = Modifier.clickable(
                onClick = onResendClick,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PurchaseOtpSheetPreview() {
    VelvetTheme {
        Column(
            modifier = Modifier.background(White).padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            OtpBoxes(otp = "12", onOtpChange = {})
            ResendRow(secondsLeft = 24, onResendClick = {})
            ResendRow(secondsLeft = 0, onResendClick = {})
        }
    }
}
