package org.sharad.velvetinvestment.presentation.mutualfund.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.sharad.velvetinvestment.data.remote.model.mfdetails.Metrics
import org.sharad.velvetinvestment.domain.models.mutualfunds.MutualFundGraphPointsDomain
import org.sharad.velvetinvestment.domain.usecases.fundusecases.GetMutualFundDetailsUseCase
import org.sharad.velvetinvestment.domain.usecases.fundusecases.GetMutualFundGraphUseCase
import org.sharad.velvetinvestment.presentation.mutualfund.CalculatorInputState
import org.sharad.velvetinvestment.presentation.mutualfund.DetailsState
import org.sharad.velvetinvestment.presentation.mutualfund.GraphDurationSelection
import org.sharad.velvetinvestment.presentation.mutualfund.GraphState
import org.sharad.velvetinvestment.presentation.mutualfund.MutualFundScreenState
import org.sharad.velvetinvestment.presentation.mutualfund.StableMetricUi
import org.sharad.velvetinvestment.utils.networking.onError
import org.sharad.velvetinvestment.utils.networking.onSuccess
import org.sharad.velvetinvestment.utils.pruneForGraph

class MutualFundDetailsScreenViewModel(
    private val id: String,
    private val getDetailsUseCase: GetMutualFundDetailsUseCase,
    private val getGraphUseCase: GetMutualFundGraphUseCase
): ViewModel() {

    val idTemp="771e9ac9-7159-477d-b54f-634a63f9ae75"
    private val _detailsState=MutableStateFlow<DetailsState>(DetailsState.Loading)
    val detailsState = _detailsState.asStateFlow()

    private val _calculatorInput = MutableStateFlow(CalculatorInputState())
    val calculatorInput = _calculatorInput.asStateFlow()


    private val _graphState = MutableStateFlow<GraphState>(GraphState.Loading)
    val graphState: StateFlow<GraphState> = _graphState.asStateFlow()

    private val _selectedYear = MutableStateFlow(GraphDurationSelection.ThreeYears)
    val selectedYear= _selectedYear.asStateFlow()

    private val _bottomSheetVisibility= MutableStateFlow(false)
    val bottomSheetVisibility= _bottomSheetVisibility.asStateFlow()

    val chartPoints: StateFlow<List<MutualFundGraphPointsDomain>> =
        graphState.map { state ->
            when (state) {
                is GraphState.Success -> {
                    state.data.graphPoints
                        .pruneForGraph()
                }
                else -> emptyList()
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )


    val uiState= combine(graphState,detailsState,chartPoints){graphState,detailsState, chartData->

        MutualFundScreenState(
            detailsState = detailsState,
            graphState = graphState,
            chartPoints=chartData
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = MutualFundScreenState()
    )

    val stableMetric: StateFlow<StableMetricUi?> = combine(_detailsState) { detailsStateArray ->
        val detail=detailsStateArray[0]
                when (detail) {
                    is DetailsState.Success -> {
                        detail.data.metrics.getBestMetric()
                    }
                    else -> null
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = null
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
                    _detailsState.value = DetailsState.Error(it.message)
                }
        }
    }


    fun loadGraph() {
        viewModelScope.launch {
            _graphState.value = GraphState.Loading
            getGraphUseCase(idTemp, _selectedYear.value.id)
                .onSuccess {
                    _graphState.value = GraphState.Success(it)
                }
                .onError {
                    _graphState.value = GraphState.Error(it.message)
                }
        }
    }

    fun onSelectedYearChange(newDuration: GraphDurationSelection)  {
        _selectedYear.value = newDuration
        loadGraph()
    }

    fun showBottomSheet(){
        _bottomSheetVisibility.value=true
    }

    fun hideBottomSheet(){
        _bottomSheetVisibility.value=false
    }


    fun onSipToggle(isSip: Boolean) {
        _calculatorInput.value = _calculatorInput.value.copy(isSip = isSip)
    }

    fun onInvestmentChange(value: Long) {
        _calculatorInput.value = _calculatorInput.value.copy(monthlyInvestment = value)
    }

    fun onTimeChange(value: Int) {
        _calculatorInput.value = _calculatorInput.value.copy(timeInYears = value)
    }
}

fun Metrics.getBestMetric(): StableMetricUi? {
    return when {
        return_3y != null -> StableMetricUi("3Y", return_3y)
        return_1y != null -> StableMetricUi("1Y", return_1y)
        return_6m != null -> StableMetricUi("6M", return_6m)
        return_90d != null -> StableMetricUi("3M", return_90d)
        return_30d != null -> StableMetricUi("1M", return_30d)
        else -> null
    }
}


fun Metrics.getPreferredReturn(): Float {
    return when {
        return_1y != null -> return_1y.toFloat()
        return_6m != null -> return_6m.toFloat()
        return_90d != null -> return_90d.toFloat()
        return_30d != null -> return_30d.toFloat()
        return_3y != null -> return_3y.toFloat()
        else -> 12f
    }
}