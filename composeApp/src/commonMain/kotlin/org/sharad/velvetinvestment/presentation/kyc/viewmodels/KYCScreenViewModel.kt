package org.sharad.velvetinvestment.presentation.kyc.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.sharad.velvetinvestment.domain.usecases.mfkycusecases.InitiateKycUseCase
import org.sharad.velvetinvestment.utils.SnackBarController
import org.sharad.velvetinvestment.utils.networking.onError
import org.sharad.velvetinvestment.utils.networking.onSuccess

class KYCScreenViewModel(
    private val initiateKyc: InitiateKycUseCase
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun startKyc(
        onLaunchWebView: (url: String) -> Unit
    ) = viewModelScope.launch {

        _isLoading.value = true

        initiateKyc()
            .onSuccess { data ->
                _isLoading.value = false
                onLaunchWebView(data.digilocker_url)
            }
            .onError {
                _isLoading.value = false
                SnackBarController.showError(it.message)
            }
    }
}