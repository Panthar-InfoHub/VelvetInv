package org.sharad.velvetinvestment.presentation.portfolio.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.sharad.velvetinvestment.domain.TransactionStatus
import org.sharad.velvetinvestment.domain.models.portfolio.BankDetails
import org.sharad.velvetinvestment.domain.models.portfolio.SIPDetailsDomain
import org.sharad.velvetinvestment.domain.models.portfolio.TransactionHistoryDomain
import org.sharad.velvetinvestment.presentation.portfolio.compose.RedemptionInputType
import org.sharad.velvetinvestment.presentation.portfolio.compose.RedemptionType
import org.sharad.velvetinvestment.utils.LoadingState

class SIPDetailsViewModel: ViewModel() {

    private val _loadingState = MutableStateFlow<LoadingState>(LoadingState.Loading)
    val loadingState: StateFlow<LoadingState> = _loadingState.asStateFlow()

    private val _sipDetails = MutableStateFlow<SIPDetailsDomain?>(null)
    val sipDetails: StateFlow<SIPDetailsDomain?> = _sipDetails.asStateFlow()

    private val _showRedemptionSheet = MutableStateFlow(false)
    val showRedemptionSheet = _showRedemptionSheet.asStateFlow()

    private val _selectedRedemptionType = MutableStateFlow(RedemptionType.PARTIAL)
    val selectedRedemptionType = _selectedRedemptionType.asStateFlow()

    private val _selectedInputType = MutableStateFlow(RedemptionInputType.UNITS)
    val selectedInputType = _selectedInputType.asStateFlow()

    private val _redemptionUnits = MutableStateFlow("")
    val redemptionUnits = _redemptionUnits.asStateFlow()

    private val _redemptionAmount = MutableStateFlow("")
    val redemptionAmount = _redemptionAmount.asStateFlow()

    private val _isSubmitting = MutableStateFlow(false)
    val isSubmitting = _isSubmitting.asStateFlow()


    fun onRedemptionTypeChange(type: RedemptionType) {
        _selectedRedemptionType.value = type
    }

    fun onInputTypeChange(type: RedemptionInputType) {
        _selectedInputType.value = type
    }

    fun onUnitsChange(units: String) {
        _redemptionUnits.value = units
    }

    fun onAmountChange(amount: String) {
        _redemptionAmount.value = amount
    }

    fun onDismissRedemptionSheet() {
        _showRedemptionSheet.value = false
    }

    fun onShowRedemptionSheet() {
        _showRedemptionSheet.value = true
    }

    fun submitRedemption() {
        viewModelScope.launch {
            _isSubmitting.value = true
            delay(2000) // Simulate API call
            _isSubmitting.value = false
            _showRedemptionSheet.value = false
            // Handle success/error as needed
        }
    }
}