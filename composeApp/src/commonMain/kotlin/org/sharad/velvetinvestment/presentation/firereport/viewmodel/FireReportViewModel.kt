package org.sharad.velvetinvestment.presentation.firereport.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.sharad.velvetinvestment.domain.models.fire.FireReportDomain
import org.sharad.velvetinvestment.domain.usecases.userfinance.DownloadFirePdfUseCase
import org.sharad.velvetinvestment.domain.usecases.userfinance.GetFireReportUseCase
import org.sharad.velvetinvestment.presentation.firereport.uimodels.toUiModel
import org.sharad.velvetinvestment.utils.SnackBarController
import org.sharad.velvetinvestment.utils.SnackBarType
import org.sharad.velvetinvestment.utils.UiState
import org.sharad.velvetinvestment.utils.networking.onError
import org.sharad.velvetinvestment.utils.networking.onSuccess

class FireReportViewModel(
    private val getFireReportUseCase: GetFireReportUseCase,
    private val downloadFirePdfUseCase: DownloadFirePdfUseCase
): ViewModel() {


    private val _uiState = MutableStateFlow<UiState<FireReportDomain>>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _selectedYear = MutableStateFlow(SelectedYear.TWENTY_YEARS)
    val selectedYear = _selectedYear.asStateFlow()

    private val _emiIncluded= MutableStateFlow(false)
    val emiIncluded= _emiIncluded.asStateFlow()

    private val _downloadingReport= MutableStateFlow(false)
    val downloadingReport= _downloadingReport.asStateFlow()


    val fireReportUiModel = combine(
        uiState,
        selectedYear,
        emiIncluded
    ) { state, yearSelection, includeEmi ->

        when (state) {

            is UiState.Loading -> UiState.Loading

            is UiState.Error -> UiState.Error(state.message)

            is UiState.Success -> {

                val domain = state.data
                val limit = yearSelection.limit()

                UiState.Success(
                    domain.toUiModel(
                        includeEmi = includeEmi,
                        yearLimit = limit
                    )
                )
            }
        }

    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UiState.Loading
    )

    init {
        loadData()
    }

    fun loadData(year: Int? = null) {
        viewModelScope.launch {
           getFireReportUseCase()
                .onSuccess {
                    _uiState.value = UiState.Success(it)
                }
                .onError {
                    _uiState.value = UiState.Error(it.message)
                }
        }
    }

    fun toggleEmi(){
        _emiIncluded.value = !_emiIncluded.value
    }

    fun onSelectedYearChange(
        selectedYear: SelectedYear
    ){
        _selectedYear.value = selectedYear
    }

    fun downloadFireReport(){
        viewModelScope.launch {
            _downloadingReport.value = true
            downloadFirePdfUseCase(
                onSuccess = {
                    _downloadingReport.value = false
                    viewModelScope.launch { SnackBarController.showSnackBar(SnackBarType.Success("Report Downloaded")) }
                },
                onFailed = {
                    _downloadingReport.value = false
                    viewModelScope.launch { SnackBarController.showSnackBar(SnackBarType.Error(it)) }
                }
            ).onError {
                _downloadingReport.value = false
                SnackBarController.showSnackBar(SnackBarType.Error(it.message))
            }
        }
    }

}

enum class SelectedYear(val value: String){
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