package org.sharad.velvetinvestment.presentation.onboarding.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import org.sharad.velvetinvestment.utils.parseSafeLong

class InsuranceCoverageViewModel: ViewModel() {

    private val _healthInsuranceAmount= MutableStateFlow(0L)
    val healthInsuranceAmount=_healthInsuranceAmount.asStateFlow()

    private val _lifeInsuranceAmount= MutableStateFlow(0L)
    val lifeInsuranceAmount=_lifeInsuranceAmount.asStateFlow()

    val totalInsuranceAmount= _healthInsuranceAmount.combine(_lifeInsuranceAmount){i1,i2->
        i1+i2
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = 0L
    )

    fun onHealthInsuranceAmountChange(amount: String) {
        _healthInsuranceAmount.value =
            parseSafeLong(amount, _healthInsuranceAmount.value) ?: 0L
    }

    fun onLifeInsuranceAmountChange(amount: String) {
        _lifeInsuranceAmount.value =
            parseSafeLong(amount, _lifeInsuranceAmount.value) ?: 0L
    }


}