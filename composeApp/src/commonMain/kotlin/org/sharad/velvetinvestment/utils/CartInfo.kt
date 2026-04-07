package org.sharad.velvetinvestment.utils

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object CartInfo {

    private val _fundAmount= MutableStateFlow(0)
    val fundAmount=_fundAmount.asStateFlow()

    fun updateFundAmount(amount:Int) {
        _fundAmount.value = amount
    }

}