package org.sharad.velvetinvestment.features.fireonboarding.presentation.onboarding.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.sharad.velvetinvestment.data.remote.model.casreport.CASResponseDto
import org.sharad.velvetinvestment.data.remote.repository.CASRepo
import org.sharad.velvetinvestment.utils.SharedDocument
import org.sharad.velvetinvestment.utils.SnackBarController
import org.sharad.velvetinvestment.utils.networking.onError
import org.sharad.velvetinvestment.utils.networking.onSuccess

data class CASParserUiState(
    val file: SharedDocument? = null,
    val password: String = "",
    val isUploading: Boolean = false
)

sealed interface CASParserEvent {
    /** A new file clears the password typed for the last one. */
    data class OnFileSelected(val file: SharedDocument?) : CASParserEvent
    data class OnPasswordChanged(val value: String) : CASParserEvent
    data object OnUploadClicked : CASParserEvent
}

sealed interface CASParserEffect {
    data class Parsed(val report: CASResponseDto) : CASParserEffect
}

class CASParserViewModel(
    private val casRepo: CASRepo
) : ViewModel() {

    private val _uiState = MutableStateFlow(CASParserUiState())
    val uiState = _uiState.asStateFlow()

    private val _effect = Channel<CASParserEffect>()
    val effect = _effect.receiveAsFlow()

    fun handleEvent(event: CASParserEvent) {
        when (event) {
            is CASParserEvent.OnFileSelected -> _uiState.update { it.copy(file = event.file, password = "") }
            is CASParserEvent.OnPasswordChanged -> _uiState.update { it.copy(password = event.value) }
            CASParserEvent.OnUploadClicked -> upload()
        }
    }

    private fun upload() {
        val state = _uiState.value
        val bytes = state.file?.toByteArray()
        if (bytes == null) {
            viewModelScope.launch { SnackBarController.showError("No File Selected") }
            return
        }
        if (state.password.isEmpty() || state.isUploading) return
        viewModelScope.launch {
            _uiState.update { it.copy(isUploading = true) }
            casRepo.uploadPdfFile(bytes, state.password.trim())
                .onSuccess { report ->
                    _uiState.value = CASParserUiState()
                    _effect.send(CASParserEffect.Parsed(report))
                    SnackBarController.showSuccess("Data Updated Successfully")
                }
                .onError {
                    _uiState.update { s -> s.copy(isUploading = false) }
                    SnackBarController.showError("Incorrect Password")
                }
        }
    }
}
