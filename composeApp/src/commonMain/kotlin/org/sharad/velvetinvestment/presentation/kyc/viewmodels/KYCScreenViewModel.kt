package org.sharad.velvetinvestment.presentation.kyc.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.sharad.velvetinvestment.domain.usecases.LaunchBrowserUseCase
import org.sharad.velvetinvestment.domain.usecases.mfkycusecases.GetDigiLockerDetailsUseCase
import org.sharad.velvetinvestment.domain.usecases.mfkycusecases.InitiateKycUseCase
import org.sharad.velvetinvestment.utils.SnackBarController
import org.sharad.velvetinvestment.utils.SnackBarType
import org.sharad.velvetinvestment.utils.networking.onError
import org.sharad.velvetinvestment.utils.networking.onSuccess

class KYCScreenViewModel(
    private val initiateKyc: InitiateKycUseCase,
    private val launchBrowser: LaunchBrowserUseCase
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun startKyc(
        onNavigate: () -> Unit
    ) = viewModelScope.launch {

        _isLoading.value = true

        initiateKyc()
            .onSuccess { data ->
                launchBrowser(data.digilocker_url)
                _isLoading.value = false
                onNavigate()
            }
            .onError {
                _isLoading.value = false
                SnackBarController.showSnackBar(
                    SnackBarType.Error(it.message)
                )
            }
    }
}