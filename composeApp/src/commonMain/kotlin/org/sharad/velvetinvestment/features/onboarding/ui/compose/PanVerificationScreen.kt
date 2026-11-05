package org.sharad.velvetinvestment.features.onboarding.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import org.jetbrains.compose.resources.stringResource
import org.sharad.emify.core.ui.theme.Black
import org.sharad.emify.core.ui.theme.FilterChipUnselected
import org.sharad.emify.core.ui.theme.GoalIconBg
import org.sharad.emify.core.ui.theme.Gray444
import org.sharad.emify.core.ui.theme.Gray65
import org.sharad.emify.core.ui.theme.Primary
import org.sharad.emify.core.ui.theme.Secondary
import org.sharad.velvetinvestment.features.onboarding.ui.OnboardingInput
import org.sharad.velvetinvestment.features.onboarding.ui.rememberFormFocusController
import org.sharad.velvetinvestment.features.onboarding.ui.viewmodels.PanVerificationEvent
import org.sharad.velvetinvestment.features.onboarding.ui.viewmodels.PanVerificationUiState
import org.sharad.velvetinvestment.shared.compose.AppButton
import org.sharad.velvetinvestment.shared.compose.TitledAppTextField
import org.sharad.velvetinvestment.shared.genericDropShadow
import org.sharad.velvetinvestment.shared.theme.Spacing
import org.sharad.velvetinvestment.shared.theme.VelvetTheme
import org.sharad.velvetinvestment.utils.clearFocusOnTap

@Composable
fun ConfirmYourDetailsScreen(
    state: PanVerificationUiState,
    handleEvent: (PanVerificationEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val form = rememberFormFocusController()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(White)
            .padding(horizontal = Spacing.dp24)
            .clearFocusOnTap()
            .imePadding()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(top = Spacing.dp16),
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = "Skip",
                style = MaterialTheme.typography.bodySmall,
                color = Secondary,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Secondary.copy(alpha = 0.05f))
                    .clickable { handleEvent(PanVerificationEvent.OnSkipClick) }
                    .padding(horizontal = Spacing.dp12, vertical = Spacing.dp6)
            )
        }
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(Spacing.dp18),
            contentPadding = PaddingValues(top = Spacing.dp16)
        ) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(Spacing.dp12)) {
                    Text(
                        text = "Confirm your details",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Black,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = "We need to verify your information to keep your financial journey secure and restorative.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Gray444,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            item {
                TitledAppTextField(
                    title = "PAN",
                    value = state.pan,
                    onValueChange = { handleEvent(PanVerificationEvent.OnPanChange(it)) },
                    placeholder = "ABCDE1234F",
                    mandatory = true,
                    keyboardType = KeyboardType.Text,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Characters,
                        autoCorrectEnabled = false,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onDone = {
                        form.submit(state.canSubmit && !state.isLoading) {
                            handleEvent(PanVerificationEvent.OnProceedClick)
                        }
                    }),
                    isError = state.pan.isNotEmpty() && !OnboardingInput.isValidPan(state.pan)
                )
            }


            item {
                SecureAndSafeCard(
                    isConsentChecked = state.isConsentChecked,
                    onConsentChange = { handleEvent(PanVerificationEvent.OnConsentChange(it)) },
                    onTermsClick = { handleEvent(PanVerificationEvent.OnTermsClick) },
                    onPrivacyClick = { handleEvent(PanVerificationEvent.OnPrivacyClick) },
                    onReadMoreClick = { handleEvent(PanVerificationEvent.OnReadMoreClick) }
                )
            }

            item {
                AppButton(
                    text = "Confirm & Proceed",
                    onClick = { handleEvent(PanVerificationEvent.OnProceedClick) },
                    loading = state.isLoading,
                    enabled = state.canSubmit,
                    modifier = Modifier
                        .fillMaxWidth()
                        .genericDropShadow()
                )
            }
        }
    }
}

@Composable
private fun SecureAndSafeCard(
    isConsentChecked: Boolean,
    onConsentChange: (Boolean) -> Unit,
    onTermsClick: () -> Unit,
    onPrivacyClick: () -> Unit,
    onReadMoreClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(Spacing.dp16))
            .border(Spacing.dp1, FilterChipUnselected, RoundedCornerShape(Spacing.dp16))
            .background(GoalIconBg)
            .padding(Spacing.dp20),
        horizontalArrangement = Arrangement.spacedBy(Spacing.dp12),
        verticalAlignment = Alignment.Top
    ) {
        Checkbox(
            checked = isConsentChecked,
            onCheckedChange = onConsentChange,
            colors = CheckboxDefaults.colors(checkedColor = Primary, checkmarkColor = White),
            modifier = Modifier
                .size(Spacing.dp24)
                .padding(top = Spacing.dp2)
        )

        val annotatedString = buildAnnotatedString {
            withStyle(SpanStyle()) {
                append("By submitting this consent, I authorize Janta Nivesh to call/ SMS/ WhatsApp/ email me about its products & have accepted the ")
            }

            val termsLink = LinkAnnotation.Clickable("TERMS") {
                onTermsClick()
            }
            withLink(termsLink) {
                withStyle(SpanStyle(color = Primary)) {
                    append("Terms of Use")
                }
            }

            withStyle(SpanStyle()) {
                append(" & the ")
            }

            val privacyLink = LinkAnnotation.Clickable("PRIVACY") {
                onPrivacyClick()
            }
            withLink(privacyLink) {
                withStyle(SpanStyle(color = Primary)) {
                    append("Privacy Policy.")
                }
            }

            withStyle(SpanStyle()) {
                append(" I authorize ")
            }
        }
        Text(
            text = annotatedString,
            style = MaterialTheme.typography.titleSmall,
            color = Gray65
        )
    }
}

@Preview(locale = "hi", showBackground = true)
@Composable
fun ConfirmYourDetailsScreenPreview(){
    VelvetTheme {
        ConfirmYourDetailsScreen(
            state = PanVerificationUiState(),
            handleEvent = {}
        )
    }
}