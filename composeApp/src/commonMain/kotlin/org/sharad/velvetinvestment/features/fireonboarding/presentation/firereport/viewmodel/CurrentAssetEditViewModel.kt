package org.sharad.velvetinvestment.features.fireonboarding.presentation.firereport.viewmodel

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
import org.sharad.velvetinvestment.features.fireonboarding.presentation.onboarding.models.AssetFlowDetails
import org.sharad.velvetinvestment.features.fireonboarding.presentation.onboarding.models.total
import org.sharad.velvetinvestment.shared.domain.models.UserAssetsDomain
import org.sharad.velvetinvestment.utils.AppEventsController
import org.sharad.velvetinvestment.utils.SnackBarController
import org.sharad.velvetinvestment.utils.UiState
import org.sharad.velvetinvestment.utils.networking.onError
import org.sharad.velvetinvestment.utils.networking.onSuccess
import org.sharad.velvetinvestment.utils.parseSafeLong
import org.sharad.velvetinvestment.shared.domain.usecase.GetUserDataUseCase

data class CurrentAssetEditUiState(
    val assetInfo: UiState<AssetFlowDetails> = UiState.Loading,
    val isSubmitting: Boolean = false,
    val showCasDialog: Boolean = false
) {
    val totalAssets: Long
        get() = (assetInfo as? UiState.Success)?.data?.total() ?: 0L
}

/** Which asset class a [CurrentAssetEditEvent.OnAssetChanged] edits. */
enum class AssetField { MUTUAL_FUNDS, STOCKS_AND_SHARES, FIXED_DEPOSITS, REAL_ESTATE, GOLD_AND_COMMODITIES, CASH }

sealed interface CurrentAssetEditEvent {
    data object Retry : CurrentAssetEditEvent
    data object OnBackClicked : CurrentAssetEditEvent
    data class OnAssetChanged(val field: AssetField, val value: String) : CurrentAssetEditEvent
    data object OnImportCasClicked : CurrentAssetEditEvent
    data object OnCasDialogDismissed : CurrentAssetEditEvent
    /** A CAS statement was parsed: it replaces what was entered by hand. */
    data class OnCasImported(val report: CASResponseDto) : CurrentAssetEditEvent
    data object OnSubmitClicked : CurrentAssetEditEvent
}

sealed interface CurrentAssetEditEffect {
    /** Back out, whether by the back button or after the changes saved. */
    data object NavigateBack : CurrentAssetEditEffect
}

class CurrentAssetEditViewModel(
    private val getUserDataUseCase: GetUserDataUseCase,
    private val repository: FireOnboardingRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CurrentAssetEditUiState())
    val uiState = _uiState.asStateFlow()

    private val _effect = Channel<CurrentAssetEditEffect>()
    val effect = _effect.receiveAsFlow()

    init {
        loadData()
    }

    fun handleEvent(event: CurrentAssetEditEvent) {
        when (event) {
            CurrentAssetEditEvent.Retry -> loadData()
            CurrentAssetEditEvent.OnBackClicked -> sendEffect(CurrentAssetEditEffect.NavigateBack)
            is CurrentAssetEditEvent.OnAssetChanged -> edit { it.with(event.field, event.value) }
            CurrentAssetEditEvent.OnImportCasClicked -> _uiState.update { it.copy(showCasDialog = true) }
            CurrentAssetEditEvent.OnCasDialogDismissed -> _uiState.update { it.copy(showCasDialog = false) }
            is CurrentAssetEditEvent.OnCasImported -> _uiState.update {
                val accounts = event.report.summary.accounts
                it.copy(
                    assetInfo = UiState.Success(
                        AssetFlowDetails()
                            .with(AssetField.STOCKS_AND_SHARES, accounts.demat.total_value.toLong().toString())
                            .with(AssetField.MUTUAL_FUNDS, accounts.mutual_funds.total_value.toLong().toString())
                    )
                )
            }
            CurrentAssetEditEvent.OnSubmitClicked -> submit()
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(assetInfo = UiState.Loading) }
            getUserDataUseCase()
                .onError { error -> _uiState.update { it.copy(assetInfo = UiState.Error(error.message)) } }
                .onSuccess { data ->
                    val assets = data.assets ?: UserAssetsDomain()
                    _uiState.update {
                        it.copy(
                            assetInfo = UiState.Success(
                                AssetFlowDetails(
                                    mutualFunds = assets.mutualFunds,
                                    stocksAndShares = assets.stocks,
                                    fixedDeposits = assets.fixedDeposits,
                                    realEstate = assets.realEstate,
                                    goldAndCommodities = assets.gold,
                                    cash = assets.cash
                                )
                            )
                        )
                    }
                }
        }
    }

    private fun edit(change: (AssetFlowDetails) -> AssetFlowDetails) {
        _uiState.update { state ->
            val current = state.assetInfo as? UiState.Success ?: return@update state
            state.copy(assetInfo = UiState.Success(change(current.data)))
        }
    }

    private fun submit() {
        val state = _uiState.value
        val details = (state.assetInfo as? UiState.Success)?.data ?: return
        if (state.isSubmitting) return
        viewModelScope.launch {
            _uiState.update { it.copy(isSubmitting = true) }
            repository.submitAssets(details.toStepBody())
                .onError {
                    _uiState.update { s -> s.copy(isSubmitting = false) }
                    SnackBarController.showError(it.message)
                }
                .onSuccess {
                    _uiState.update { s -> s.copy(isSubmitting = false) }
                    AppEventsController.sendFireRefreshEvent()
                    _effect.send(CurrentAssetEditEffect.NavigateBack)
                }
        }
    }

    private fun sendEffect(effect: CurrentAssetEditEffect) {
        viewModelScope.launch { _effect.send(effect) }
    }
}

/** [field] set from typed text; text that isn't a number leaves it as it was. */
internal fun AssetFlowDetails.with(field: AssetField, value: String): AssetFlowDetails = when (field) {
    AssetField.MUTUAL_FUNDS -> copy(mutualFunds = parseSafeLong(value, mutualFunds))
    AssetField.STOCKS_AND_SHARES -> copy(stocksAndShares = parseSafeLong(value, stocksAndShares))
    AssetField.FIXED_DEPOSITS -> copy(fixedDeposits = parseSafeLong(value, fixedDeposits))
    AssetField.REAL_ESTATE -> copy(realEstate = parseSafeLong(value, realEstate))
    AssetField.GOLD_AND_COMMODITIES -> copy(goldAndCommodities = parseSafeLong(value, goldAndCommodities))
    AssetField.CASH -> copy(cash = parseSafeLong(value, cash))
}

