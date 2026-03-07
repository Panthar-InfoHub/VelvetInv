package org.sharad.velvetinvestment.presentation.goals.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.sharad.velvetinvestment.domain.models.home.GoalsSummaryDomain
import org.sharad.velvetinvestment.domain.usecases.home.GetGoalsSummaryUseCase
import org.sharad.velvetinvestment.utils.UiState
import org.sharad.velvetinvestment.utils.networking.onError
import org.sharad.velvetinvestment.utils.networking.onSuccess
import org.sharad.velvetinvestment.utils.networking.toMessage

class GoalInfoScreenViewModel(
    private val getGoalsSummaryUseCase: GetGoalsSummaryUseCase
): ViewModel() {

    private val _goalsInfo = MutableStateFlow<UiState<List<GoalsSummaryDomain>>>(UiState.Loading)
    val goalsInfo = _goalsInfo.asStateFlow()

    init {
        loadGoals()
    }

    fun loadGoals() {
        viewModelScope.launch {
            getGoalsSummaryUseCase()
                .onSuccess {
                    _goalsInfo.value= UiState.Success(it)
                }
                .onError {
                    _goalsInfo.value= UiState.Error(it.toMessage())
                }

        }
    }

}