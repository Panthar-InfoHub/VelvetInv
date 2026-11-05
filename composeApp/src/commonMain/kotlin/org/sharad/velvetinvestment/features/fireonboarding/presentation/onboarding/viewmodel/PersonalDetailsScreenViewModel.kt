package org.sharad.velvetinvestment.features.fireonboarding.presentation.onboarding.viewmodel

import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toUpperCase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.sharad.velvetinvestment.domain.usecases.user.SignOutUseCase
import org.sharad.velvetinvestment.features.fireonboarding.presentation.onboarding.models.PersonalDetails
import org.sharad.velvetinvestment.utils.isValidEmail
import org.sharad.velvetinvestment.utils.isValidPhoneNumberInput
import org.sharad.velvetinvestment.utils.storage.AuthPrefs

data class PersonalDetailsUiState(
    /** The FIRE questionnaire step on screen (1-based); the header and skip read it. */
    val currentStep: Int = 1,
    val details: PersonalDetails = PersonalDetails(),
    val showDateSelector: Boolean = false
) {
    val isNextEnabled: Boolean
        get() = details.fullName.isNotBlank() &&
                details.email.isNotBlank() && isValidEmail(details.email) &&
                details.phoneNumber.length == 10 &&
                details.city.isNotBlank() &&
                details.dob != null
}

sealed interface PersonalDetailsEvent {
    data class OnNameChanged(val value: String) : PersonalDetailsEvent
    data class OnEmailChanged(val value: String) : PersonalDetailsEvent
    data class OnPhoneChanged(val value: String) : PersonalDetailsEvent
    data class OnCityChanged(val value: String) : PersonalDetailsEvent
    data class OnDobChanged(val dob: Long?) : PersonalDetailsEvent
    data class OnRetirementYearChanged(val year: Int) : PersonalDetailsEvent
    data object OnDobFieldClicked : PersonalDetailsEvent
    data object OnDatePickerDismissed : PersonalDetailsEvent
    /** The questionnaire moved forward or back a step; the header follows [PersonalDetailsUiState.currentStep]. */
    data object OnStepForward : PersonalDetailsEvent
    data object OnStepBack : PersonalDetailsEvent
    data object OnSignOut : PersonalDetailsEvent
}

/**
 * Personal details, and the step counter of the whole FIRE questionnaire: this view model lives
 * for the length of the flow, so the header reads the step from here.
 */
class PersonalDetailsScreenViewModel(
    step: Int,
    authPrefs: AuthPrefs,
    private val signOutUseCase: SignOutUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        PersonalDetailsUiState(
            currentStep = step,
            details = PersonalDetails(phoneNumber = authPrefs.getPhoneNumber() ?: "")
        )
    )
    val uiState = _uiState.asStateFlow()

    fun handleEvent(event: PersonalDetailsEvent) {
        when (event) {
            is PersonalDetailsEvent.OnNameChanged ->
                edit { it.copy(fullName = event.value.toUpperCase(Locale.current)) }
            is PersonalDetailsEvent.OnEmailChanged -> edit { it.copy(email = event.value) }
            is PersonalDetailsEvent.OnPhoneChanged ->
                if (event.value.isValidPhoneNumberInput()) edit { it.copy(phoneNumber = event.value) }
            is PersonalDetailsEvent.OnCityChanged ->
                edit { it.copy(city = event.value.toUpperCase(Locale.current)) }
            is PersonalDetailsEvent.OnDobChanged -> event.dob?.let { dob -> edit { it.copy(dob = dob) } }
            is PersonalDetailsEvent.OnRetirementYearChanged -> edit { it.copy(retirementYear = event.year) }
            PersonalDetailsEvent.OnDobFieldClicked -> _uiState.update { it.copy(showDateSelector = true) }
            PersonalDetailsEvent.OnDatePickerDismissed -> _uiState.update { it.copy(showDateSelector = false) }
            PersonalDetailsEvent.OnStepForward -> _uiState.update { it.copy(currentStep = it.currentStep + 1) }
            PersonalDetailsEvent.OnStepBack -> _uiState.update { it.copy(currentStep = it.currentStep - 1) }
            PersonalDetailsEvent.OnSignOut -> viewModelScope.launch { signOutUseCase() }
        }
    }

    private fun edit(change: (PersonalDetails) -> PersonalDetails) {
        _uiState.update { it.copy(details = change(it.details)) }
    }
}
