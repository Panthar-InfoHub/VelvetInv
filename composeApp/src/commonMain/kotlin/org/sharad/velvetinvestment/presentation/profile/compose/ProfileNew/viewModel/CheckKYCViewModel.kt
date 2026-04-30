package org.sharad.velvetinvestment.presentation.profile.compose.ProfileNew.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.sharad.velvetinvestment.domain.usecases.user.GetUserDataUseCase
import org.sharad.velvetinvestment.presentation.profile.compose.ProfileNew.UImodel.CheckKYCModel
import org.sharad.velvetinvestment.utils.UiState
import org.sharad.velvetinvestment.utils.networking.onError
import org.sharad.velvetinvestment.utils.networking.onSuccess

import org.sharad.velvetinvestment.data.remote.mapper.toKycCompletion
import org.sharad.velvetinvestment.data.remote.mapper.toTradingCompletion

class CheckKYCViewModel(
    private val getUserDataUseCase: GetUserDataUseCase
) : ViewModel() {
    private val _kycState = MutableStateFlow<UiState<CheckKYCModel>>(UiState.Loading)
    val kycState = _kycState.asStateFlow()

    init {
        loadKycStatus()
    }

    fun loadKycStatus() {
        viewModelScope.launch {
            _kycState.value = UiState.Loading
            getUserDataUseCase()
                .onSuccess {
                    _kycState.value = UiState.Success(
                        CheckKYCModel(
                            mutualFundKYC = it.data.toKycCompletion(),
                            tradingAccountKYC = it.data.toTradingCompletion()
                        )
                    )
                }
                .onError {
                    _kycState.value = UiState.Error(it.message)
                }
        }
    }
}
