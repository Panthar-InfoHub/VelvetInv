package org.sharad.velvetinvestment.utils

sealed interface UIState {
    data object Loading: UIState
    data object Success: UIState
    data class Error(val error:String): UIState
}