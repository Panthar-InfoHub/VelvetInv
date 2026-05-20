package org.sharad.velvetinvestment.presentation.homescreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.sharad.velvetinvestment.data.remote.mapper.toHomeScreenUiData
import org.sharad.velvetinvestment.domain.usecases.fundusecases.GetUserCartUseCase
import org.sharad.velvetinvestment.domain.usecases.user.GetUserDataUseCase
import org.sharad.velvetinvestment.presentation.homescreen.uimodels.HomeScreenUiData
import org.sharad.velvetinvestment.utils.UiState
import org.sharad.velvetinvestment.utils.networking.onError
import org.sharad.velvetinvestment.utils.networking.onSuccess

class HomeScreenViewModel(
    private val getUserDataUseCase: GetUserDataUseCase,
    private val loadCartUseCase: GetUserCartUseCase
) : ViewModel() {

    private val _homeState =
        MutableStateFlow<UiState<HomeScreenUiData>>(UiState.Loading)
    val homeState = _homeState.asStateFlow()

    init {
        loadHomeData()
    }

    fun loadHomeData(){
        loadHome()
        loadCart()
    }

    fun loadHome() {
        viewModelScope.launch {

            _homeState.value = UiState.Loading

            getUserDataUseCase()
                .onSuccess {
                    _homeState.value = UiState.Success(it.toHomeScreenUiData())
                }
                .onError {
                    _homeState.value = UiState.Error(it.message)
                }

        }
    }

    fun toggleHidden() {
        _homeState.update {
            if (it is UiState.Success) {
                UiState.Success(it.data.copy(hidden = !it.data.hidden))
            } else it
        }
    }

    fun loadCart(){
        viewModelScope.launch {
            loadCartUseCase()
        }
    }

}

