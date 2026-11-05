package org.sharad.velvetinvestment.features.onboarding.ui.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.tooling.preview.Preview
import org.sharad.emify.core.ui.theme.Black
import org.sharad.emify.core.ui.theme.Gray444
import org.sharad.velvetinvestment.features.onboarding.ui.formField
import org.sharad.velvetinvestment.features.onboarding.ui.rememberFormFocusController
import org.sharad.velvetinvestment.features.onboarding.ui.viewmodels.BasicDetailsEvent
import org.sharad.velvetinvestment.features.onboarding.ui.viewmodels.BasicDetailsUiState
import org.sharad.velvetinvestment.shared.compose.AppButton
import org.sharad.velvetinvestment.shared.compose.AppDatePicker
import org.sharad.velvetinvestment.shared.compose.TitledAppTextField
import org.sharad.velvetinvestment.shared.genericDropShadow
import org.sharad.velvetinvestment.shared.theme.Spacing
import org.sharad.velvetinvestment.shared.theme.VelvetTheme
import org.sharad.velvetinvestment.utils.clearFocusOnTap

private const val DOB_FIELD = "dob"

@Composable
fun BasicDetailsScreen(
    state: BasicDetailsUiState,
    handleEvent: (BasicDetailsEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    var showDatePicker by remember { mutableStateOf(false) }
    val form = rememberFormFocusController()

    AppDatePicker(
        show = showDatePicker,
        selectedDate = null,
        onDismiss = { showDatePicker = false },
        onDateSelected = { millis ->
            handleEvent(BasicDetailsEvent.OnDobChange(millis))
            form.clearHighlight()
        }
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = Spacing.dp24)
            .clearFocusOnTap()
            .imePadding()
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(Spacing.dp18),
            contentPadding = PaddingValues(top = Spacing.dp24)
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
                    title = "Name (as per PAN)",
                    value = state.name,
                    onValueChange = { handleEvent(BasicDetailsEvent.OnNameChange(it.toUpperCase(Locale.current))) },
                    placeholder = "",
                    mandatory = true,
                    keyboardType = KeyboardType.Text,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words,
                        imeAction = ImeAction.Next
                    ),
                    // The date of birth is a picker, so Next closes the keyboard and points at it.
                    keyboardActions = KeyboardActions(onNext = { form.highlight(DOB_FIELD) }),
                    modifier = Modifier.formField(form)
                )
            }

            item {
                TitledAppTextField(
                    title = "Name (as per PAN)",
                    value = state.name,
                    onValueChange = { handleEvent(BasicDetailsEvent.OnNameChange(it.toUpperCase(Locale.current))) },
                    placeholder = "",
                    mandatory = true,
                    keyboardType = KeyboardType.Text,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words,
                        imeAction = ImeAction.Next
                    ),
                    // The date of birth is a picker, so Next closes the keyboard and points at it.
                    keyboardActions = KeyboardActions(onNext = { form.highlight(DOB_FIELD) }),
                    modifier = Modifier.formField(form)
                )
            }

            item {
                AppButton(
                    text = "Confirm & Proceed",
                    onClick = { handleEvent(BasicDetailsEvent.OnProceedClick) },
                    loading = state.isLoading,
                    enabled = state.canSubmit,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = Spacing.dp16)
                        .genericDropShadow()
                )
            }
        }

    }
}


@Preview(locale = "hi", showBackground = true)
@Composable
fun BasicDetailsScreenPreview(){
    VelvetTheme {
        BasicDetailsScreen(
            state = BasicDetailsUiState(),
            handleEvent = {}
        )
    }
}