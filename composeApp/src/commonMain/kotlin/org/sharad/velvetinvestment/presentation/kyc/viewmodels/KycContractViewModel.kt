package org.sharad.velvetinvestment.presentation.kyc.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.sharad.velvetinvestment.domain.models.mfkyc.GetContractPdfUseCase
import org.sharad.velvetinvestment.domain.usecases.LaunchBrowserUseCase
import org.sharad.velvetinvestment.domain.usecases.mfkycusecases.FinalizeKycUseCase
import org.sharad.velvetinvestment.domain.usecases.mfkycusecases.GetESingKycUseCase
import org.sharad.velvetinvestment.utils.AppEvents
import org.sharad.velvetinvestment.utils.SnackBarController
import org.sharad.velvetinvestment.utils.SnackBarType
import org.sharad.velvetinvestment.utils.UiState
import org.sharad.velvetinvestment.utils.networking.onError
import org.sharad.velvetinvestment.utils.networking.onSuccess

class KycContractViewModel(
    private val getContractPdfUseCase: GetContractPdfUseCase,
    private val getESignKycUseCase: GetESingKycUseCase,
    private val finalizeKycUseCase: FinalizeKycUseCase,
    private val launchBrowserUseCase: LaunchBrowserUseCase
): ViewModel() {

    private val _contractPdfUrl = MutableStateFlow<UiState<String>>(UiState.Loading)
    val contractPdfUrl = _contractPdfUrl.asStateFlow()

    private val _submitLoading = MutableStateFlow(false)
    val submitLoading: StateFlow<Boolean> = _submitLoading.asStateFlow()

    private val _successState = MutableStateFlow(false)
    val successState: StateFlow<Boolean> = _successState.asStateFlow()

    private val _markedAsRead = MutableStateFlow(false)
    val markedAsRead: StateFlow<Boolean> = _markedAsRead

    init {
        getContractPdf()
    }

    fun getContractPdf() {
        viewModelScope.launch {
            _contractPdfUrl.value=UiState.Loading
            getContractPdfUseCase()
                .onSuccess {
                    _contractPdfUrl.value = UiState.Success(it)
                }
                .onError {
                    _contractPdfUrl.value = UiState.Error(it.message)
                }
        }
    }

    fun getESignUrl(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _submitLoading.value = true
            getESignKycUseCase()
                .onSuccess {
                    _submitLoading.value = false
                    onSuccess()
                    launchBrowserUseCase(it)
                }
                .onError {
                    _submitLoading.value = false
                    SnackBarController.showSnackBar(SnackBarType.Error(it.message))
                }
        }
    }

    fun finalizeKyc(onSuccess: () -> Unit) {
        val data= _contractPdfUrl.value
        viewModelScope.launch {
            _contractPdfUrl.value = UiState.Loading
            finalizeKycUseCase()
                .onSuccess {
                    AppEvents.sendHomeRefreshEvent()
                    onSuccess()
                    _contractPdfUrl.value = data
                    _successState.value = true
                }
                .onError {
                    _contractPdfUrl.value = data
                    SnackBarController.showSnackBar(SnackBarType.Error(it.message))
                }
        }
    }

    fun toggleMark(){
        _markedAsRead.value = !_markedAsRead.value
    }
}