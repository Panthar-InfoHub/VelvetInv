package org.sharad.velvetinvestment.presentation.homescreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.sharad.velvetinvestment.data.remote.mapper.toHomeScreenUiData
import org.sharad.velvetinvestment.domain.repository.UserAuth
import org.sharad.velvetinvestment.domain.usecases.home.HomeScreenUseCases
import org.sharad.velvetinvestment.presentation.homescreen.uimodels.HomeScreenUiData
import org.sharad.velvetinvestment.utils.UiState
import org.sharad.velvetinvestment.utils.networking.NetworkResponse
import org.sharad.velvetinvestment.utils.networking.onError
import org.sharad.velvetinvestment.utils.networking.onSuccess
import org.sharad.velvetinvestment.utils.networking.toMessage

class HomeScreenViewModel(
    private val repo: UserAuth
) : ViewModel() {

    private val _homeState =
        MutableStateFlow<UiState<HomeScreenUiData>>(UiState.Loading)
    val homeState = _homeState.asStateFlow()

    init {
        loadHome()
    }

    fun loadHome() {
        viewModelScope.launch {

            _homeState.value = UiState.Loading

            repo.getUserData()
                .onSuccess {
                    _homeState.value = UiState.Success(it.toHomeScreenUiData())
                }
                .onError {
                    _homeState.value = UiState.Error(it.message)
                }

        }
    }
}

