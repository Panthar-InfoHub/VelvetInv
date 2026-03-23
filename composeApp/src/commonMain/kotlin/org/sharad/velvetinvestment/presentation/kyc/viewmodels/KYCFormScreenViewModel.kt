package org.sharad.velvetinvestment.presentation.kyc.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
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
import org.sharad.velvetinvestment.utils.SnackBarType
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse
import org.sharad.velvetinvestment.utils.networking.onError
import org.sharad.velvetinvestment.utils.networking.onSuccess

class KYCFormScreenViewModel(
    private val getDigiDetails: GetDigiLockerDetailsUseCase,
    private val submitKycForm: SubmitKycFormUseCase,
    private val getUserPersonalInfo: GetUserPersonalInfo
) : ViewModel() {

    private val _isLoading = MutableStateFlow(true)
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
        _formState.value = _formState.value.copy(panNumber = value)
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

    fun updateOccupationCode(value: String) {
        _formState.value = _formState.value.copy(occupationCode = value)

    }

    fun updateFatherName(value: String) {
        _formState.value = _formState.value.copy(fatherName = value)
    }

    fun updateMotherName(value: String) {
        _formState.value = _formState.value.copy(motherName = value)
    }

    fun updatePlaceOfBirth(value: String) {
        _formState.value = _formState.value.copy(placeOfBirth = value)
    }

    fun loadData(
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {
        viewModelScope.launch {

            _isLoading.value = true

            val digiDeferred = async { getDigiDetails() }
            val personalDeferred = async { getUserPersonalInfo() }

            val digiResult = digiDeferred.await()
            val personalResult = personalDeferred.await()

            if (digiResult is NetworkResponse.Error) {
                handleError(digiResult.error, onFailure)
                return@launch
            }

            if (personalResult is NetworkResponse.Error) {
                handleError(personalResult.error, onFailure)
                return@launch
            }

            val digiData = (digiResult as NetworkResponse.Success).data
            val personalData = (personalResult as NetworkResponse.Success).data

            setInitialData(digiData)

            _formState.update {
                it.copy(
                    mobileNumber = personalData.phone,
                    emailId = personalData.email,
                )
            }

            _isDataLoaded.value = true
            _isLoading.value = false

            onSuccess()
        }
    }


    fun submitForm(onSuccess: () -> Unit){
        if (!_formState.value.isValid()) return
        _formSubmissionLoading.value=true
        viewModelScope.launch {
            submitKycForm(_formState.value.toDomain())
                .onSuccess {
                    _formSubmissionLoading.value=false
                    onSuccess()
                }
                .onError {
                    _formSubmissionLoading.value=false
                    SnackBarController.showSnackBar(
                        SnackBarType.Error(it.message)
                    )
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

        SnackBarController.showSnackBar(
            SnackBarType.Error(error.message)
        )

        onFailure()
    }
}