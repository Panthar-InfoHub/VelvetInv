package org.sharad.velvetinvestment.presentation.profile.compose.ProfileNew.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.sharad.velvetinvestment.shared.domain.usecase.GetUserDataUseCase
import org.sharad.velvetinvestment.shared.domain.models.UserDataDomain
import org.sharad.velvetinvestment.utils.isoUtcToDisplayDate
import org.sharad.velvetinvestment.presentation.profile.compose.ProfileNew.UImodel.PersonalInfoUiData
import org.sharad.velvetinvestment.utils.UiState
import org.sharad.velvetinvestment.utils.networking.onError
import org.sharad.velvetinvestment.utils.networking.onSuccess

class PersonalInfoViewModel(
    private val getUserDataUseCase: GetUserDataUseCase
): ViewModel() {

    private val _personalInfoState = MutableStateFlow<UiState<PersonalInfoUiData>>(UiState.Loading)
    val personalInfoState = _personalInfoState.asStateFlow()

    init {
        loadPersonalInfo()
    }

    fun loadPersonalInfo() {
        viewModelScope.launch {
            _personalInfoState.value = UiState.Loading
            getUserDataUseCase()
                .onSuccess {
                    _personalInfoState.value = UiState.Success(it.toPersonalInfoUiData())
                }
                .onError {
                    _personalInfoState.value = UiState.Error(it.message)
                }
        }
    }
}

private fun UserDataDomain.toPersonalInfoUiData(): PersonalInfoUiData = PersonalInfoUiData(
    fullName = name,
    dob = dob.takeIf { it.isNotBlank() }?.isoUtcToDisplayDate().orEmpty(),
    mobileNumber = mobile,
    email = email,
    city = city.orEmpty(),
    kycStatusMf = onboarding.kycStatus.ifBlank { "Unknown" },
    kycStatusTrading = "Unknown",
)
