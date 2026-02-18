package org.sharad.velvetinvestment.presentation.mutualfund.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.sharad.velvetinvestment.domain.usecases.fundusecases.GetMutualFundDetailsUseCase
import org.sharad.velvetinvestment.domain.usecases.fundusecases.GetMutualFundGraphUseCase
import org.sharad.velvetinvestment.presentation.mutualfund.DetailsState
import org.sharad.velvetinvestment.presentation.mutualfund.GraphDurationSelection
import org.sharad.velvetinvestment.presentation.mutualfund.GraphState
import org.sharad.velvetinvestment.presentation.mutualfund.MutualFundScreenState
import org.sharad.velvetinvestment.utils.networking.onError
import org.sharad.velvetinvestment.utils.networking.onSuccess

class MutualFundDetailsScreenViewModel(
    private val id: String,
    private val getDetailsUseCase: GetMutualFundDetailsUseCase,
    private val getGraphUseCase: GetMutualFundGraphUseCase
): ViewModel() {

    private val _detailsState=MutableStateFlow<DetailsState>(DetailsState.Loading)
    val detailsState = _detailsState.asStateFlow()


    private val _graphState = MutableStateFlow<GraphState>(GraphState.Loading)
    val graphState: StateFlow<GraphState> = _graphState.asStateFlow()

    private val _selectedYear = MutableStateFlow<GraphDurationSelection>(GraphDurationSelection.ThreeYears)
    val selectedYear= _selectedYear.asStateFlow()

    val uiState= combine(graphState,detailsState){graphState,detailsState->

        MutualFundScreenState(
            detailsState = detailsState,
            graphState = graphState
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = MutualFundScreenState()
    )

    init {
        loadInitial()
    }

    fun loadInitial() {
        loadDetails()
        loadGraph()
    }

    fun loadDetails() {
        viewModelScope.launch {
            _detailsState.value = DetailsState.Loading
            getDetailsUseCase(id)
                .onSuccess {
                    _detailsState.value = DetailsState.Success(it)
                }
                .onError {
                    _detailsState.value = DetailsState.Error(it.name)
                }
        }
    }


    fun loadGraph() {
        viewModelScope.launch {
            _graphState.value = GraphState.Loading
            getGraphUseCase(id, _selectedYear.value.id)
                .onSuccess {
                    _graphState.value = GraphState.Success(it)
                }
                .onError {
                    _graphState.value = GraphState.Error(it.name)
                }
        }
    }

    fun onSelectedYearChange(newDuration: GraphDurationSelection)  {
        _selectedYear.value = newDuration
        loadGraph()
    }

}