package org.sharad.velvetinvestment.utils

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object SnackBarController {

    private val _snackBars = MutableSharedFlow<SnackBarType>()
    val snackBars = _snackBars.asSharedFlow()

    suspend fun showSnackBar(snackBarType: SnackBarType) {
        _snackBars.emit(snackBarType)
    }


}

sealed class SnackBarType{
    data class Success(val message: String): SnackBarType()
    data class Error(val message: String): SnackBarType()
    data class Warning(val message: String): SnackBarType()
    data class Info(val message: String): SnackBarType()
    data class Neutral(val message: String): SnackBarType()
}
