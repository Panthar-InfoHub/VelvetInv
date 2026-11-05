package org.sharad.velvetinvestment.features.onboarding.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.tooling.preview.Preview
import org.sharad.emify.core.ui.theme.Gray444
import org.sharad.velvetinvestment.features.onboarding.domain.model.Gender
import org.sharad.velvetinvestment.features.onboarding.domain.model.MaritalStatus
import org.sharad.velvetinvestment.features.onboarding.domain.model.Occupation
import org.sharad.velvetinvestment.features.onboarding.domain.model.SourceOfFund
import org.sharad.velvetinvestment.features.onboarding.ui.OnboardingInput
import org.sharad.velvetinvestment.features.onboarding.ui.formField
import org.sharad.velvetinvestment.features.onboarding.ui.rememberFormFocusController
import org.sharad.velvetinvestment.features.onboarding.ui.viewmodels.ReviewProfileEvent
import org.sharad.velvetinvestment.features.onboarding.ui.viewmodels.ReviewProfileUiState
import org.sharad.velvetinvestment.shared.compose.AgreementCheckBoxCard
import org.sharad.velvetinvestment.shared.compose.AppBackButton
import org.sharad.velvetinvestment.shared.compose.AppButton
import org.sharad.velvetinvestment.shared.compose.AppDatePicker
import org.sharad.velvetinvestment.shared.compose.DropDownSelector
import org.sharad.velvetinvestment.shared.compose.ErrorScreen
import org.sharad.velvetinvestment.shared.compose.LoaderScreen
import org.sharad.velvetinvestment.shared.compose.TitledAppTextField
import org.sharad.velvetinvestment.shared.genericDropShadow
import org.sharad.velvetinvestment.utils.location.rememberLocationPermissionRequester
import org.sharad.velvetinvestment.shared.theme.Spacing
import org.sharad.velvetinvestment.shared.theme.VelvetTheme
import org.sharad.velvetinvestment.utils.clearFocusOnTap
import org.sharad.velvetinvestment.utils.formatMillisToIsoDate
import velvet.composeapp.generated.resources.Res

private val nameKeyboardOptions = KeyboardOptions(
    capitalization = KeyboardCapitalization.Characters,
    imeAction = ImeAction.Next
)

/** Keys of the pickers the keyboard flow can point at. */
private const val DOB_FIELD = "dob"
private const val MARITAL_STATUS_FIELD = "marital_status"
private const val OCCUPATION_FIELD = "occupation"

@Composable
fun ReviewProfileScreen(
    state: ReviewProfileUiState,
    handleEvent: (ReviewProfileEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    // The form is prefilled from `GET /user/`, so there is nothing to show until that read lands.
    Box(modifier = Modifier.fillMaxSize().background(White)) {
        when {
            state.isScreenLoading -> LoaderScreen(modifier = modifier.fillMaxSize())

            state.showError -> ErrorScreen(
                modifier = modifier.fillMaxSize(),
                errorMessage = state.error,
                onRetryClick = { handleEvent(ReviewProfileEvent.OnRetryLoad) }
            )

            else -> ReviewProfileContent(
                state = state,
                handleEvent = handleEvent,
                modifier = modifier
            )
        }
    }
}

@Composable
private fun ReviewProfileContent(
    state: ReviewProfileUiState,
    handleEvent: (ReviewProfileEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    // The prompt has to be raised from the UI layer, so the screen owns the requester and the
    // view model only ever hears the answer — which is also what starts the submission.
    val requestLocationPermission = rememberLocationPermissionRequester { granted ->
        handleEvent(ReviewProfileEvent.OnLocationPermissionResult(granted))
    }

    var showDatePicker by remember {
        mutableStateOf(false)
    }

    val form = rememberFormFocusController()
    val addressFocus = remember { FocusRequester() }
    val pincodeFocus = remember { FocusRequester() }
    val cityFocus = remember { FocusRequester() }
    val fatherNameFocus = remember { FocusRequester() }
    val placeOfBirthFocus = remember { FocusRequester() }

    // Next on the pincode lets the city lookup it started finish first: the keyboard closes while
    // the lookup runs, then a filled-in city is skipped for marital status, and an empty one (no
    // match, or the lookup failed) takes the keyboard to be typed by hand. Any tap in between
    // hands control back to the user and drops the pending move.
    var awaitingCity by remember { mutableStateOf(false) }
    val leavePincode = {
        if (state.city.isBlank()) form.focus(cityFocus) else form.highlight(MARITAL_STATUS_FIELD)
    }
    LaunchedEffect(awaitingCity, state.isFetchingCity) {
        if (awaitingCity && !state.isFetchingCity) {
            awaitingCity = false
            leavePincode()
        }
    }

    AppDatePicker(
        show = showDatePicker,
        selectedDate = null,
        onDismiss = { showDatePicker = false },
        onDateSelected = { millis ->
            handleEvent(ReviewProfileEvent.OnDobChange(formatMillisToIsoDate(millis)))
            form.clearHighlight()
        },
    )
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = White)
            .padding(horizontal= Spacing.dp20)
            .clearFocusOnTap()
            .pointerInput(Unit) {
                awaitEachGesture {
                    awaitFirstDown(requireUnconsumed = false)
                    awaitingCity = false
                }
            }
            .imePadding()
    ) {
        AppBackButton(
            onClick = { }
        )
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(Spacing.dp20),
            contentPadding = PaddingValues(top = Spacing.dp4)

        ) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(Spacing.dp12)) {
                    Text(
                        text = "Review Profile",
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    )
                    Text(
                        text = "We'll use this to keep your account secure and send important updates.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Gray444
                    )
                }
            }

            item {
                TitledAppTextField(
                    title = "Full Name",
                    value = state.fullName,
                    onValueChange = { handleEvent(ReviewProfileEvent.OnFullNameChange(it.toUpperCase(Locale.current))) },
                    placeholder = "",
                    mandatory = true,
                    keyboardOptions = nameKeyboardOptions,
                    // Date of birth is a picker, so Next closes the keyboard and points at it.
                    keyboardActions = KeyboardActions(onNext = { form.highlight(DOB_FIELD) }),
                    modifier = Modifier.formField(form)
                )
            }
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(Spacing.dp16), verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()){
                    TitledDateField(
                        title = "Date of Birth",
                        value = state.dob,
                        onClick = { showDatePicker = true },
                        highlighted = form.isHighlighted(DOB_FIELD),
                        modifier = Modifier.weight(1f)
                    )
                    DropDownSelector(
                        title = "Gender",
                        value = state.gender?.displayName ?: "",
                        onValueChange = { selectedValue -> handleEvent(ReviewProfileEvent.OnGenderChange(selectedValue)) },
                        list = Gender.entries,
                        modifier = Modifier.fillMaxWidth().weight(1f),
                        placeholder = "Select gender",
                        mandatory = true,
                        textConvertor = { it.displayName },
                    )
                }
            }
            // A verified address is final: it is neither editable nor shown here.
            if (!state.isEmailLocked) item {
                TitledAppTextField(
                    title = "Email",
                    value = state.email,
                    onValueChange = { handleEvent(ReviewProfileEvent.OnEmailChange(it)) },
                    placeholder = "name@example.com",
                    mandatory = true,
                    keyboardType = KeyboardType.Email,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.None,
                        autoCorrectEnabled = false,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(onNext = { form.focus(addressFocus) }),
                    isError = state.email.isNotEmpty() && !OnboardingInput.isValidEmail(state.email),
                    modifier = Modifier.formField(form)
                )
            }
            item {
                TitledAppTextField(
                    title = "Address",
                    value = state.address,
                    onValueChange = { handleEvent(ReviewProfileEvent.OnAddressChange(it)) },
                    placeholder = "\n\n\n",
                    mandatory = true,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Characters,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(onNext = { form.focus(pincodeFocus) }),
                    modifier = Modifier.formField(form, addressFocus)
                )
            }
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(Spacing.dp16), verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()){
                    TitledAppTextField(
                        title = "Pincode",
                        value = state.pincode,
                        onValueChange = { handleEvent(ReviewProfileEvent.OnPincodeChange(it)) },
                        placeholder = "XXXXXX",
                        mandatory = true,
                        keyboardType = KeyboardType.Number,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(onNext = {
                            if (state.isFetchingCity) {
                                form.dismissKeyboard()
                                awaitingCity = true
                            } else {
                                leavePincode()
                            }
                        }),
                        isError = state.pincode.isNotEmpty() &&
                                !OnboardingInput.isValidPincode(state.pincode),
                        modifier = Modifier
                            .weight(1f)
                            .formField(form, pincodeFocus)
                    )
                    TitledAppTextField(
                        title = "City",
                        value = state.city,
                        onValueChange = { handleEvent(ReviewProfileEvent.OnCityChange(it.toUpperCase(Locale.current))) },
                        // Filled from the pincode; locked only while that lookup runs.
                        enabled = !state.isFetchingCity,
                        placeholder = if (state.isFetchingCity) "Fetching city…" else "",
                        mandatory = true,
                        keyboardOptions = nameKeyboardOptions,
                        // Marital status is a dropdown, so Next closes the keyboard and points at it.
                        keyboardActions = KeyboardActions(onNext = { form.highlight(MARITAL_STATUS_FIELD) }),
                        modifier = Modifier
                            .weight(1f)
                            .formField(form, cityFocus)
                    )
                }
            }
            item {
                DropDownSelector(
                    title = "Marital Status",
                    value = state.maritalStatus?.displayName ?: "",
                    onValueChange = {
                        handleEvent(ReviewProfileEvent.OnMaritalStatusChange(it))
                        form.clearHighlight()
                    },
                    list = MaritalStatus.entries,
                    placeholder = "Select marital status",
                    mandatory = true,
                    textConvertor = { it.displayName },
                    highlighted = form.isHighlighted(MARITAL_STATUS_FIELD),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            if (state.maritalStatus == MaritalStatus.MARRIED){
                item{
                    TitledAppTextField(
                        title = "Spouse's Name",
                        value = state.spouseName,
                        onValueChange = { handleEvent(ReviewProfileEvent.OnSpouseNameChange(it.toUpperCase(Locale.current))) },
                        placeholder = "",
                        mandatory = true,
                        keyboardOptions = nameKeyboardOptions,
                        keyboardActions = KeyboardActions(onNext = { form.focus(fatherNameFocus) }),
                        modifier = Modifier.formField(form)
                    )
                }
            }
            item {
                TitledAppTextField(
                    title = "Father's Name",
                    value = state.fatherName,
                    onValueChange = { handleEvent(ReviewProfileEvent.OnFatherNameChange(it.toUpperCase(Locale.current))) },
                    placeholder = "",
                    mandatory = true,
                    keyboardOptions = nameKeyboardOptions,
                    keyboardActions = KeyboardActions(onNext = { form.focus(placeOfBirthFocus) }),
                    modifier = Modifier.formField(form, fatherNameFocus)
                )
            }
            item {
                TitledAppTextField(
                    title = "Place of Birth",
                    value = state.placeOfBirth,
                    onValueChange = { handleEvent(ReviewProfileEvent.OnPlaceOfBirthChange(it.toUpperCase(Locale.current))) },
                    placeholder = "",
                    mandatory = true,
                    keyboardOptions = nameKeyboardOptions,
                    // Occupation is a dropdown, so Next closes the keyboard and points at it.
                    keyboardActions = KeyboardActions(onNext = { form.highlight(OCCUPATION_FIELD) }),
                    modifier = Modifier.formField(form, placeOfBirthFocus)
                )
            }
            item {
                DropDownSelector(
                    title = "Occupation",
                    value = state.occupation?.displayName ?: "",
                    onValueChange = {
                        handleEvent(ReviewProfileEvent.OnOccupationChange(it))
                        form.clearHighlight()
                    },
                    list = Occupation.entries,
                    placeholder = "Select occupation",
                    mandatory = true,
                    textConvertor = { it.displayName },
                    highlighted = form.isHighlighted(OCCUPATION_FIELD),
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item {
                DropDownSelector(
                    title = "Source of Fund",
                    value = state.sourceOfFund?.displayName ?: "",
                    onValueChange = { handleEvent(ReviewProfileEvent.OnSourceOfFundChange(it)) },
                    list = SourceOfFund.entries,
                    placeholder = "Select source of fund",
                    mandatory = true,
                    textConvertor = { it.displayName },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item {
                // The user enters a plain amount; the slab the API wants is derived from it and
                // echoed back below the field so the mapping stays visible.
                TitledAppTextField(
                    title = "Annual Income",
                    value = state.annualIncome,
                    onValueChange = { handleEvent(ReviewProfileEvent.OnAnnualIncomeChange(it)) },
                    placeholder = "e.g. 600000",
                    mandatory = true,
                    keyboardType = KeyboardType.Number,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    // The last field: Done submits under the button's own enabled and loading rules.
                    keyboardActions = KeyboardActions(onDone = {
                        form.submit(
                            state.canSubmit && !state.isFetchingCity &&
                                    !state.isFetchingLocation && !state.isLoading
                        ) { requestLocationPermission.request() }
                    }),
                    prefix = { Text("₹") },
                    supportingText = state.annualIncomeSlab?.let { slab ->
                        { Text(slab.displayName, style = MaterialTheme.typography.labelSmall) }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .formField(form)
                )
            }
            item {
                AgreementCheckBoxCard(
                    text = "I confirm that I am not a Politically Exposed Person (PEP) or related to any PEP as defined under PMLA guidelines",
                    isConsentChecked = state.isPepConfirmed,
                    onConsentChange = { handleEvent(ReviewProfileEvent.OnPepConfirmChange(it)) }
                )
            }
            item {
                AgreementCheckBoxCard(
                    text = "I confirm that I am a resident, citizen, born in, a national and a tax resident of India and all the information provided on this page is accurate to the best of my knowledge.",
                    isConsentChecked = state.isResidentConfirmed,
                    onConsentChange = { handleEvent(ReviewProfileEvent.OnResidentConfirmChange(it)) }
                )
            }
        }
        AppButton(
            // The location fix is taken on this tap, before the profile call, so the label says
            // which of the two the user is waiting on.
            text = when {
                state.isFetchingLocation -> "Fetching location…"
                state.isLoading -> "Submitting…"
                else -> "Confirm & Proceed"
            },
            onClick = { requestLocationPermission.request() },
            loading = state.isFetchingLocation || state.isLoading,
            enabled = state.canSubmit && !state.isFetchingCity,
            modifier = Modifier.fillMaxWidth().padding(top = Spacing.dp24).genericDropShadow()
        )
    }
}

@Preview(locale = "hi", heightDp = 2000)
@Composable
private fun ReviewProfileScreenPreview() {
    VelvetTheme {
        ReviewProfileScreen(
            state = ReviewProfileUiState(isScreenLoading = false),
            handleEvent = {}
        )
    }
}