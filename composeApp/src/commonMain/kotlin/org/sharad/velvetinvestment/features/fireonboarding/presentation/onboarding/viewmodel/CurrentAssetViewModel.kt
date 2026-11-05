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
import org.sharad.velvetinvestment.features.fireonboarding.data.mapper.toStepBody
import org.sharad.velvetinvestment.features.fireonboarding.domain.repository.FireOnboardingRepository
import org.sharad.velvetinvestment.features.fireonboarding.presentation.firereport.viewmodel.AssetField
import org.sharad.velvetinvestment.features.fireonboarding.presentation.firereport.viewmodel.with
import org.sharad.velvetinvestment.features.fireonboarding.presentation.onboarding.models.AssetFlowDetails
import org.sharad.velvetinvestment.features.fireonboarding.presentation.onboarding.models.total
import org.sharad.velvetinvestment.utils.SnackBarController
import org.sharad.velvetinvestment.utils.networking.onError
import org.sharad.velvetinvestment.utils.networking.onSuccess

data class CurrentAssetUiState(
    val assets: AssetFlowDetails = AssetFlowDetails(),
    val showCasDialog: Boolean = false,
    val isSubmitting: Boolean = false
) {
    val totalAssets: Long get() = assets.total()

    /** Nothing entered means nothing to save; the user should skip instead. */
    val canSubmit: Boolean get() = totalAssets > 0L
}

sealed interface CurrentAssetEvent {
    data class OnAssetChanged(val field: AssetField, val value: String) : CurrentAssetEvent
    data object OnImportCasClicked : CurrentAssetEvent
    data object OnCasDialogDismissed : CurrentAssetEvent
    /** A parsed CAS statement fills in demat holdings and mutual funds. */
    data class OnCasImported(val report: CASResponseDto) : CurrentAssetEvent
    /** `POST /onboarding/assets` with the holdings. */
    data object OnSubmitClicked : CurrentAssetEvent
}

class CurrentAssetViewModel(
    private val repository: FireOnboardingRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CurrentAssetUiState())
    val uiState = _uiState.asStateFlow()

    private val _effect = Channel<FireStepEffect>()
    val effect = _effect.receiveAsFlow()

    fun handleEvent(event: CurrentAssetEvent) {
        when (event) {
            is CurrentAssetEvent.OnAssetChanged ->
                _uiState.update { it.copy(assets = it.assets.with(event.field, event.value)) }
            CurrentAssetEvent.OnImportCasClicked -> _uiState.update { it.copy(showCasDialog = true) }
            CurrentAssetEvent.OnCasDialogDismissed -> _uiState.update { it.copy(showCasDialog = false) }
            is CurrentAssetEvent.OnCasImported -> {
                val accounts = event.report.summary.accounts
                _uiState.update {
                    it.copy(
                        assets = it.assets
                            .with(AssetField.STOCKS_AND_SHARES, accounts.demat.total_value.toLong().toString())
                            .with(AssetField.MUTUAL_FUNDS, accounts.mutual_funds.total_value.toLong().toString())
                    )
                }
            }
            CurrentAssetEvent.OnSubmitClicked -> submit()
        }
    }

    private fun submit() {
        val state = _uiState.value
        if (state.isSubmitting || !state.canSubmit) return
        viewModelScope.launch {
            _uiState.update { it.copy(isSubmitting = true) }
            repository.submitAssets(state.assets.toStepBody())
                .onSuccess {
                    _uiState.update { s -> s.copy(isSubmitting = false) }
                    _effect.send(FireStepEffect.Submitted)
                }
                .onError {
                    _uiState.update { s -> s.copy(isSubmitting = false) }
                    SnackBarController.showError(it.message)
                }
        }
    }
}
