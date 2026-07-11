package org.sharad.velvetinvestment.presentation.kyc.viewmodels

import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toUpperCase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.sharad.velvetinvestment.domain.models.mfkyc.DigiLockerDetailsDomain
import org.sharad.velvetinvestment.domain.usecases.mfkycusecases.GetDigiLockerDetailsUseCase
import org.sharad.velvetinvestment.domain.usecases.mfkycusecases.SubmitKycFormUseCase
import org.sharad.velvetinvestment.domain.usecases.user.GetUserPersonalInfo
import org.sharad.velvetinvestment.presentation.kyc.uistate.KycFormUiState
import org.sharad.velvetinvestment.presentation.kyc.uistate.toDomain
import org.sharad.velvetinvestment.presentation.kyc.uistate.toFormState
import org.sharad.velvetinvestment.utils.SnackBarController
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse
import org.sharad.velvetinvestment.utils.networking.onError
import org.sharad.velvetinvestment.utils.networking.onSuccess
import org.sharad.velvetinvestment.utils.tradingaccount.OccupationType
import kotlin.time.Duration.Companion.milliseconds

class KYCFormScreenViewModel(
    private val getDigiDetails: GetDigiLockerDetailsUseCase,
    private val submitKycForm: SubmitKycFormUseCase,
    private val getUserPersonalInfo: GetUserPersonalInfo
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _isDataLoaded=MutableStateFlow(false)
    val isDataLoaded=_isDataLoaded.asStateFlow()

    private val _formSubmissionLoading=MutableStateFlow(false)
    val formSubmissionLoading=_formSubmissionLoading.asStateFlow()


    private val _formState = MutableStateFlow(KycFormUiState())
    val formState = _formState.asStateFlow()

    val buttonEnabled=combine(_formState){form->
        val data= form[0]
        data.isValid()
    }.stateIn(
        scope = viewModelScope,
        started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )



    fun setInitialData(data: DigiLockerDetailsDomain) {
        _formState.value = data.toFormState()
    }

    fun updateGender(value: String) {
        _formState.value = _formState.value.copy(gender = value)
    }

    fun updateMaritalStatus(value: String) {
        _formState.value = _formState.value.copy(maritalStatus = value)
    }

    fun updatePANNumber(value: String) {
        _formState.value = _formState.value.copy(panNumber = value.trim().toUpperCase(Locale.current))
    }

    fun updateFatherTitle(value: String) {
        _formState.value = _formState.value.copy(fatherTitle = value)
    }

    fun updateMotherTitle(value: String) {
        _formState.value = _formState.value.copy(motherTitle = value)
    }

    fun updateOccupationDesciption(value: String) {
        _formState.value = _formState.value.copy(occupationDescription = value)
    }

    fun updateOccupationCode(value: OccupationType) {
        _formState.value = _formState.value.copy(occupationCode = value.code)
        _formState.value = _formState.value.copy(occupationDescription = value.name)

    }

    fun updateFatherName(value: String) {
        _formState.value = _formState.value.copy(fatherName = value.toUpperCase(Locale.current))
    }

    fun updateMotherName(value: String) {
        _formState.value = _formState.value.copy(motherName = value.toUpperCase(Locale.current))
    }

    fun updatePlaceOfBirth(value: String) {
        _formState.value = _formState.value.copy(placeOfBirth = value.toUpperCase(Locale.current))
    }

    fun updateName(value: String){
        _formState.value= _formState.value.copy(name = value.toUpperCase(Locale.current))
    }

    fun loadData(
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {
        viewModelScope.launch {

            if (_isLoading.value) return@launch

            _isLoading.value = true
            _isDataLoaded.value = false

            kotlinx.coroutines.supervisorScope {

                val digiDeferred = async {
                    retryRequest {
                        getDigiDetails()
                    }
                }

                val userDeferred = async {
                    retryRequest {
                        getUserPersonalInfo()
                    }
                }

                val digiResult = digiDeferred.await()
                val userResult = userDeferred.await()

                when {
                    digiResult is NetworkResponse.Error -> {
                        handleError(digiResult.error, onFailure)
                    }

                    userResult is NetworkResponse.Error -> {
                        handleError(userResult.error, onFailure)
                    }

                    digiResult is NetworkResponse.Success &&
                            userResult is NetworkResponse.Success -> {

                        setInitialData(digiResult.data)

                        _formState.update {
                            it.copy(
                                mobileNumber = userResult.data.phone,
                                emailId = userResult.data.email
                            )
                        }

                        _isDataLoaded.value = true
                        _isLoading.value = false

                        onSuccess()
                    }
                }
            }
        }
    }


    fun submitForm(onSuccess: () -> Unit){
        if (!_formState.value.isValid()) return
        _formSubmissionLoading.value=true
        viewModelScope.launch {
            var retryCount = 0
            val maxRetries = 2

            while (retryCount <= maxRetries) {
                var success = false
                submitKycForm(_formState.value.toDomain())
                    .onSuccess {
                        _formSubmissionLoading.value=false
                        onSuccess()
                        success = true
                    }
                    .onError {
                        if (retryCount == maxRetries) {
                            _formSubmissionLoading.value=false
                            SnackBarController.showError(it.message)
                        }
                    }

                if (success) break

                retryCount++
                if (retryCount <= maxRetries) {
                    delay(5000.milliseconds)
                }
            }
        }
    }

    fun KycFormUiState.isValid(): Boolean {
        return name.isNotBlank() &&
                dob.isNotBlank() &&
                gender.isNotBlank() &&
                maritalStatus.isNotBlank() &&
                fatherName.isNotBlank() &&
                motherName.isNotBlank() &&
                panNumber.isNotBlank() &&
                aadhaarNumber.isNotBlank() &&
                emailId.isNotBlank() &&
                mobileNumber.isNotBlank() &&
                placeOfBirth.isNotBlank() &&
                occupationCode.isNotBlank() &&
                occupationDescription.isNotBlank()
    }

    private suspend fun handleError(
        error: ErrorDomain,
        onFailure: () -> Unit
    ) {
        _isLoading.value = false

        SnackBarController.showError(error.message)

        onFailure()
    }
}

private suspend fun <T> retryRequest(
    maxRetries: Int = 2,
    delayMillis: Long = 5000,
    request: suspend () -> NetworkResponse<T, ErrorDomain>
): NetworkResponse<T, ErrorDomain> {

    repeat(maxRetries + 1) { attempt ->

        when (val result = request()) {

            is NetworkResponse.Success -> return result

            is NetworkResponse.Error -> {
                if (attempt == maxRetries) {
                    return result
                }

                delay(delayMillis.milliseconds)
            }
        }
    }

    error("Unreachable")
}