package org.sharad.velvetinvestment.presentation.profile.compose.ProfileNew.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.sharad.velvetinvestment.data.remote.mapper.toPersonalInfoUiData
import org.sharad.velvetinvestment.domain.usecases.user.GetUserDataUseCase
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