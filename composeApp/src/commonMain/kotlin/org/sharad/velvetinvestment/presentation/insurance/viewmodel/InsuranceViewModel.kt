package org.sharad.velvetinvestment.presentation.insurance.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import org.sharad.velvetinvestment.domain.usecases.userfinance.RequestConnectionUseCase
import org.sharad.velvetinvestment.utils.SnackBarController
import org.sharad.velvetinvestment.utils.networking.NetworkResponse
import org.sharad.velvetinvestment.utils.networking.onError
import org.sharad.velvetinvestment.utils.networking.onSuccess

class InsuranceViewModel(
    private val requestConnectionUseCase: RequestConnectionUseCase
) : ViewModel() {

    var isLoading by mutableStateOf(false)
        private set

    private val _showThankYouDialog = MutableSharedFlow<Unit>()
    val showThankYouDialog = _showThankYouDialog.asSharedFlow()

    fun requestCallback() {
        if (isLoading) return
        viewModelScope.launch {
            isLoading = true
            requestConnectionUseCase(type = "CALL", message = "Requesting For Insurance Advice.")
                .onSuccess {
                    isLoading = false
                    _showThankYouDialog.emit(Unit)
                }
                .onError {
                    isLoading = false
                    SnackBarController.showError(it.message)
                }
        }
    }

    fun requestWhatsApp() {
        if (isLoading) requestCallback()

        viewModelScope.launch {
            isLoading = true
            requestConnectionUseCase(type = "CHAT", message = "Requesting For Insurance Advice.")
                .onSuccess {
                    isLoading = false
                    _showThankYouDialog.emit(Unit)
                }
                .onError {
                    SnackBarController.showError(it.message)
                }
        }
    }
}
