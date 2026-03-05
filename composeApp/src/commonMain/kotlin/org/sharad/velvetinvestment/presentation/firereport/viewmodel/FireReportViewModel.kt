package org.sharad.velvetinvestment.presentation.firereport.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.sharad.velvetinvestment.domain.usecases.userfinance.GetFireReportUseCase
import org.sharad.velvetinvestment.presentation.firereport.uimodels.FireCombinedUIState
import org.sharad.velvetinvestment.presentation.firereport.uimodels.toUI
import org.sharad.velvetinvestment.utils.UiState
import org.sharad.velvetinvestment.utils.networking.NetworkResponse
import org.sharad.velvetinvestment.utils.networking.onError
import org.sharad.velvetinvestment.utils.networking.onSuccess
import org.sharad.velvetinvestment.utils.networking.toMessage

class FireReportViewModel(
    private val getFireReportUseCase: GetFireReportUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow<UiState<FireCombinedUIState>>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _emiIncluded= MutableStateFlow(false)
    val emiIncluded= _emiIncluded.asStateFlow()


    init {
        loadData()
    }

    fun loadData(year: Int? = null) {
        viewModelScope.launch {
           getFireReportUseCase()
                .onSuccess {
                    _uiState.value = UiState.Success(it.toUI())
                }
                .onError {
                    _uiState.value = UiState.Error(it.toMessage())
                }
        }
    }

    fun toggleEmi(){
        _emiIncluded.value = !_emiIncluded.value
    }

}