package org.sharad.velvetinvestment.features.onboarding.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import org.sharad.emify.core.ui.theme.Gray444
import org.sharad.emify.core.ui.theme.Secondary
import org.sharad.velvetinvestment.features.onboarding.ui.OnboardingInput
import org.sharad.velvetinvestment.features.onboarding.ui.rememberFormFocusController
import org.sharad.velvetinvestment.features.onboarding.ui.viewmodels.EmailIdEvent
import org.sharad.velvetinvestment.features.onboarding.ui.viewmodels.EmailIdUiState
import org.sharad.velvetinvestment.shared.compose.AgreementCheckBoxCard
import org.sharad.velvetinvestment.shared.compose.AppButton
import org.sharad.velvetinvestment.shared.compose.TitledAppTextField
import org.sharad.velvetinvestment.shared.genericDropShadow
import org.sharad.velvetinvestment.shared.theme.Spacing
import org.sharad.velvetinvestment.shared.theme.VelvetTheme
import org.sharad.velvetinvestment.utils.clearFocusOnTap

@Composable
fun EmailIdScreen(
    state: EmailIdUiState,
    handleEvent: (EmailIdEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val form = rememberFormFocusController()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = White)
            .padding(horizontal= Spacing.dp20)
            .clearFocusOnTap()
            .imePadding()

    ) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(Spacing.dp20),
            contentPadding = PaddingValues(top = Spacing.dp24)
        ) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(Spacing.dp12)) {
                    Text(
                        text = "What is your Email ID?",
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    )
                    Text(
                        text = "We'll use this to keep your account secure and send important updates.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Gray444
                    )
                }
            }

            item {
                TitledAppTextField(
                    title = "Email ID",
                    value = state.email,
                    onValueChange = { handleEvent(EmailIdEvent.OnEmailChange(it)) },
                    placeholder = "you@example.com",
                    mandatory = true,
                    keyboardType = KeyboardType.Email,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.None,
                        autoCorrectEnabled = false,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onDone = {
                        form.submit(state.canSubmit && !state.isLoading) {
                            handleEvent(EmailIdEvent.OnSubmitClick)
                        }
                    }),
                    isError = state.email.isNotEmpty() &&
                            !OnboardingInput.isValidEmail(state.email),
                    trailingIcon = {
                        Box(
                            modifier = Modifier
                                .padding(end = Spacing.dp8)
                                .clip(CircleShape)
                                .background(Secondary)
                                .clickable { handleEvent(EmailIdEvent.OnGmailSuffixClick) }
                        ) {
                            Text(
                                "@gmail.com",
                                style = MaterialTheme.typography.bodySmall,
                                color = White,
                                modifier = Modifier.padding(
                                    horizontal = Spacing.dp12,
                                    vertical = Spacing.dp8
                                )
                            )
                        }
                    }
                )
            }

            item {
                AgreementCheckBoxCard(
                    isConsentChecked = state.isConsentChecked,
                    onConsentChange = { handleEvent(EmailIdEvent.OnConsentChange(it)) },
                    text = "I Declare that this email ID belongs to me and is operated by me. I will receive account statements, transection confirmations and regulatory communication on this mail",
                )
            }

            item {
                AppButton(
                    text = "Submit",
                    onClick = { handleEvent(EmailIdEvent.OnSubmitClick) },
                    enabled = state.canSubmit,
                    loading = state.isLoading,
                    modifier = Modifier.fillMaxWidth().genericDropShadow()
                )
            }
        }

    }
}

@Preview(locale = "hi")
@Composable
fun EmailIdScreenPreview() {
    VelvetTheme {
        EmailIdScreen(
            state = EmailIdUiState(),
            handleEvent = {}
        )
    }
}