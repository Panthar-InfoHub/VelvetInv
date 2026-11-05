package org.sharad.velvetinvestment.features.fireonboarding.presentation.firereport.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.sharad.velvetinvestment.domain.models.fire.FireReportDomain
import org.sharad.velvetinvestment.domain.usecases.userfinance.DownloadFirePdfUseCase
import org.sharad.velvetinvestment.domain.usecases.userfinance.GetFireReportUseCase
import org.sharad.velvetinvestment.features.fireonboarding.presentation.firereport.uimodels.FireReportUiModel
import org.sharad.velvetinvestment.features.fireonboarding.presentation.firereport.uimodels.toUiModel
import org.sharad.velvetinvestment.utils.AppEvent
import org.sharad.velvetinvestment.utils.AppEventsController
import org.sharad.velvetinvestment.utils.DateTimeUtils
import org.sharad.velvetinvestment.utils.SnackBarController
import org.sharad.velvetinvestment.utils.UiState
import org.sharad.velvetinvestment.utils.networking.onError
import org.sharad.velvetinvestment.utils.networking.onSuccess
import kotlin.time.Clock

data class FireReportUiState(
    /** The report as loaded; [report] is it projected through the toggles below. */
    val source: UiState<FireReportDomain> = UiState.Loading,
    val report: UiState<FireReportUiModel> = UiState.Loading,
    val selectedYear: SelectedYear = SelectedYear.TWENTY_YEARS,
    val emiIncluded: Boolean = false,
    val showProjected: Boolean = false,
    val isDownloading: Boolean = false
)

sealed interface FireReportEvent {
    data object Retry : FireReportEvent
    data object OnBackClicked : FireReportEvent
    data object OnUpdateClicked : FireReportEvent
    data object OnEmiToggled : FireReportEvent
    data object OnProjectedToggled : FireReportEvent
    data class OnYearSelected(val year: SelectedYear) : FireReportEvent
    data object OnDownloadClicked : FireReportEvent
}

sealed interface FireReportEffect {
    data object NavigateBack : FireReportEffect
    data object NavigateToUpdateOptions : FireReportEffect
}

class FireReportViewModel(
    private val getFireReportUseCase: GetFireReportUseCase,
    private val downloadFirePdfUseCase: DownloadFirePdfUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(FireReportUiState())
    val uiState = _uiState.asStateFlow()

    private val _effect = Channel<FireReportEffect>()
    val effect = _effect.receiveAsFlow()

    init {
        loadData()
        // An edit to the inputs or a goal changes the report, and home shows a summary of it.
        viewModelScope.launch {
            AppEventsController.appEvent.collect {
                if (it == AppEvent.FireRefreshEvent || it == AppEvent.GoalEventRefresh) {
                    loadData()
                    AppEventsController.sendHomeRefreshEvent()
                }
            }
        }
    }

    fun handleEvent(event: FireReportEvent) {
        when (event) {
            FireReportEvent.Retry -> loadData()
            FireReportEvent.OnBackClicked -> sendEffect(FireReportEffect.NavigateBack)
            FireReportEvent.OnUpdateClicked -> sendEffect(FireReportEffect.NavigateToUpdateOptions)
            FireReportEvent.OnEmiToggled -> project { it.copy(emiIncluded = !it.emiIncluded) }
            FireReportEvent.OnProjectedToggled -> project { it.copy(showProjected = !it.showProjected) }
            is FireReportEvent.OnYearSelected -> project { it.copy(selectedYear = event.year) }
            FireReportEvent.OnDownloadClicked -> downloadFireReport()
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            getFireReportUseCase()
                .onSuccess { report -> project { it.copy(source = UiState.Success(report)) } }
                .onError { error -> project { it.copy(source = UiState.Error(error.message)) } }
        }
    }

    /** Applies [change] and re-projects the report through the selected year and toggles. */
    private fun project(change: (FireReportUiState) -> FireReportUiState) {
        _uiState.update { current ->
            val next = change(current)
            next.copy(
                report = when (val source = next.source) {
                    UiState.Loading -> UiState.Loading
                    is UiState.Error -> UiState.Error(source.message)
                    is UiState.Success -> UiState.Success(
                        source.data.toUiModel(
                            includeEmi = next.emiIncluded,
                            yearLimit = next.selectedYear.limit(),
                            showProjected = next.showProjected
                        )
                    )
                }
            )
        }
    }

    private fun downloadFireReport() {
        if (_uiState.value.isDownloading) return
        val userName = (_uiState.value.source as? UiState.Success)?.data?.userName?.let { "${it}_" } ?: ""
        viewModelScope.launch {
            _uiState.update { it.copy(isDownloading = true) }
            downloadFirePdfUseCase(
                fileName = "Velvet_FIRE_Report_$userName${
                    DateTimeUtils.epochMillisToSlashDateReversed(Clock.System.now().toEpochMilliseconds())
                }",
                onSuccess = {
                    _uiState.update { it.copy(isDownloading = false) }
                    viewModelScope.launch { SnackBarController.showSuccess("Report Downloaded") }
                },
                onFailed = { message ->
                    _uiState.update { it.copy(isDownloading = false) }
                    viewModelScope.launch { SnackBarController.showError(message) }
                }
            ).onError {
                _uiState.update { it.copy(isDownloading = false) }
                SnackBarController.showError(it.message)
            }
        }
    }

    private fun sendEffect(effect: FireReportEffect) {
        viewModelScope.launch { _effect.send(effect) }
    }
}

enum class SelectedYear(val value: String) {
    FIVE_YEARS("5 yrs"),
    TEN_YEARS("10 yrs"),
    TWENTY_YEARS("20 yrs")
}

fun SelectedYear.limit(): Int {
    return when (this) {
        SelectedYear.FIVE_YEARS -> 5
        SelectedYear.TEN_YEARS -> 10
        SelectedYear.TWENTY_YEARS -> 20
    }
}
