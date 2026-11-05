package org.sharad.velvetinvestment.features.auth.ui.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import org.sharad.emify.core.ui.theme.Secondary
import org.sharad.emify.core.ui.theme.appRed
import org.sharad.emify.core.ui.theme.lightGray
import org.sharad.emify.core.ui.theme.titleColor
import org.sharad.velvetinvestment.features.auth.ui.viewmodels.ChangePinEvent
import org.sharad.velvetinvestment.features.auth.ui.viewmodels.ChangePinUiState
import org.sharad.velvetinvestment.shared.compose.AppButton
import org.sharad.velvetinvestment.shared.compose.BackHeader
import org.sharad.velvetinvestment.shared.compose.TitledAppTextField
import org.sharad.velvetinvestment.shared.compose.ToggleSwitch
import org.sharad.velvetinvestment.shared.theme.VelvetTheme
import org.sharad.velvetinvestment.shared.theme.titlesStyle
import org.sharad.velvetinvestment.utils.AppBackHandler
import org.sharad.velvetinvestment.utils.clearFocusOnTap
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.icon_eye_closed
import velvet.composeapp.generated.resources.icone_eye_open

/**
 * Sets the PIN the first time, and replaces it after that. When a PIN already exists this is only
 * reached through [org.sharad.velvetinvestment.features.auth.ui.viewmodels.EnterPinPurpose.CHANGE_PIN],
 * so the current PIN has already been checked against the server and is not asked for again here.
 */
@Composable
fun ChangePinScreen(
    state: ChangePinUiState,
    onEvent: (ChangePinEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    AppBackHandler(enabled = true) { onEvent(ChangePinEvent.OnBackClicked) }

    Scaffold(containerColor = Color.White) { padding ->
        Column(modifier = modifier.fillMaxSize().padding(padding)) {
            BackHeader(
                title = if (state.mpinSetup) "Change PIN" else "Setup PIN",
                showBack = true,
                onBack = { onEvent(ChangePinEvent.OnBackClicked) }
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .clearFocusOnTap()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = if (state.mpinSetup) "Update your security PIN"
                            else "Setup your security PIN",
                            style = MaterialTheme.typography.headlineSmall,
                            color = Color.Black
                        )
                        Text(
                            text = if (state.mpinSetup) "Please enter and confirm your new 4-digit PIN."
                            else "Please enter and confirm your 4-digit security PIN.",
                            style = titlesStyle,
                            color = titleColor
                        )
                    }
                }

                item {
                    PinField(
                        title = "New PIN",
                        value = state.newPin,
                        visible = state.newPinVisible,
                        onValueChange = { onEvent(ChangePinEvent.OnNewPinChanged(it)) },
                        onToggleVisibility = { onEvent(ChangePinEvent.OnToggleNewPinVisibility) },
                        supportingText = "Must be exactly 4 digits."
                    )
                }

                item {
                    PinField(
                        title = "Confirm New PIN",
                        value = state.confirmPin,
                        visible = state.confirmPinVisible,
                        onValueChange = { onEvent(ChangePinEvent.OnConfirmPinChanged(it)) },
                        onToggleVisibility = { onEvent(ChangePinEvent.OnToggleConfirmPinVisibility) },
                        supportingText = if (state.pinsMismatch) "PINs do not match" else null,
                        isError = state.pinsMismatch
                    )
                }

                if (state.mpinSetup) {
                    item {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            HorizontalDivider(thickness = 1.dp, color = lightGray)

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Enable MPIN Verification",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color.Black
                                    )
                                    Text(
                                        text = "Ask for PIN every time you open the app",
                                        style = titlesStyle.copy(fontSize = 12.sp),
                                        color = titleColor
                                    )
                                }
                                ToggleSwitch(
                                    checked = state.mpinEnabled,
                                    onCheckedChange = { onEvent(ChangePinEvent.OnMpinToggleChanged(it)) }
                                )
                            }
                        }
                    }
                }

                item {
                    HorizontalDivider(thickness = 1.dp, color = lightGray)
                }

                item {
                    AppButton(
                        text = "Save Changes",
                        onClick = { onEvent(ChangePinEvent.OnSaveClicked) },
                        enabled = state.canSubmit,
                        loading = state.saving,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
private fun PinField(
    title: String,
    value: String,
    visible: Boolean,
    onValueChange: (String) -> Unit,
    onToggleVisibility: () -> Unit,
    supportingText: String? = null,
    isError: Boolean = false
) {
    TitledAppTextField(
        title = title,
        value = value,
        onValueChange = onValueChange,
        placeholder = "",
        isError = isError,
        keyboardType = KeyboardType.NumberPassword,
        textAlign = TextAlign.Center,
        visualTransformation = if (visible) VisualTransformation.None
        else PasswordVisualTransformation(mask = '•'),
        trailingIcon = {
            Icon(
                // Same pairing as Velvet's net-worth card: the open eye means "tap to show".
                painter = painterResource(
                    if (visible) Res.drawable.icon_eye_closed else Res.drawable.icone_eye_open
                ),
                contentDescription = if (visible) "Hide PIN" else "Show PIN",
                tint = if (visible) Secondary else titleColor,
                modifier = Modifier
                    .size(20.dp)
                    .clickable(onClick = onToggleVisibility)
            )
        },
        supportingText = supportingText?.let {
            {
                Text(
                    text = it,
                    style = titlesStyle.copy(fontSize = 12.sp),
                    color = if (isError) appRed else titleColor
                )
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun ChangePinScreenPreview() {
    VelvetTheme {
        ChangePinScreen(
            state = ChangePinUiState(newPin = "1234", confirmPin = "12", mpinSetup = true),
            onEvent = {}
        )
    }
}
