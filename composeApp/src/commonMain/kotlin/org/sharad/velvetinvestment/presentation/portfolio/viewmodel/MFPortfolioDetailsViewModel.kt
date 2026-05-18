package org.sharad.velvetinvestment.presentation.portfolio.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.sharad.velvetinvestment.data.remote.model.fundredeem.FullRedemptionRequestDto
import org.sharad.velvetinvestment.data.remote.model.fundredeem.PartialRedemptionRequestDto
import org.sharad.velvetinvestment.domain.models.portfolio.SIPDetailsDomain
import org.sharad.velvetinvestment.domain.usecases.fundusecases.RedeemFullFundUseCase
import org.sharad.velvetinvestment.domain.usecases.fundusecases.RedeemPartialFundUseCase
import org.sharad.velvetinvestment.presentation.portfolio.compose.RedemptionInputType
import org.sharad.velvetinvestment.presentation.portfolio.compose.RedemptionType
import org.sharad.velvetinvestment.utils.LoadingState
import org.sharad.velvetinvestment.utils.SnackBarController
import org.sharad.velvetinvestment.utils.networking.onError
import org.sharad.velvetinvestment.utils.networking.onSuccess

sealed interface MFPortfolioSideEffects{
    data class openRedeemptionUrl(val url: String): MFPortfolioSideEffects
}
class MFPortfolioDetailsViewModel(
    private val partialRedemptionUseCase: RedeemPartialFundUseCase,
    private val redeemFullFundUseCase: RedeemFullFundUseCase
): ViewModel() {

    private val _loadingState = MutableStateFlow<LoadingState>(LoadingState.Success)
    val loadingState: StateFlow<LoadingState> = _loadingState.asStateFlow()

    private val _sideEffects = MutableSharedFlow<MFPortfolioSideEffects>()
    val sideEffects = _sideEffects.asSharedFlow()

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

    fun submitRedemption(
        schemeId: Int,
        folioNo: String,
    ) {
        viewModelScope.launch {
            _isSubmitting.value = true
           when(_selectedRedemptionType.value){
               RedemptionType.FULL ->{
                   redeemFullFundUseCase(
                       data = FullRedemptionRequestDto(
                           schemeId = schemeId,
                           folioNo = folioNo
                       )
                   )
                       .onSuccess {url->
                           _sideEffects.emit(MFPortfolioSideEffects.openRedeemptionUrl(url))
                       }
                       .onError {
                           SnackBarController.showError(it.message)
                       }
               }
               RedemptionType.PARTIAL -> {

                   val units = _redemptionUnits.value.toDoubleOrNull()
                   val amount = _redemptionAmount.value.toIntOrNull()

                   when (_selectedInputType.value) {

                       RedemptionInputType.UNITS -> {
                           if (units == null) {
                               SnackBarController.showError(
                                   "Enter valid redemption units"
                               )
                               _isSubmitting.value = false
                               return@launch
                           }
                       }

                       RedemptionInputType.AMOUNT -> {
                           if (amount == null) {
                               SnackBarController.showError(
                                   "Enter valid redemption amount"
                               )
                               _isSubmitting.value = false
                               return@launch
                           }
                       }
                   }

                   partialRedemptionUseCase(
                       data = PartialRedemptionRequestDto(
                           schemeId = schemeId,
                           folioNo = folioNo,
                           redemptionUnits = if (
                               _selectedInputType.value == RedemptionInputType.UNITS
                           ) {
                               units
                           } else {
                               null
                           },
                           redemptionAmount = if (
                               _selectedInputType.value == RedemptionInputType.AMOUNT
                           ) {
                               amount
                           } else {
                               null
                           },
                       )
                   )
                       .onSuccess { url ->
                           _sideEffects.emit(
                               MFPortfolioSideEffects.openRedeemptionUrl(url)
                           )
                       }
                       .onError {
                           SnackBarController.showError(it.message)
                       }
               }
           }
            _isSubmitting.value = false
            _showRedemptionSheet.value = false
        }
    }
}