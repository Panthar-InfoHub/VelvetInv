package org.sharad.velvetinvestment.presentation.portfolio.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.sharad.velvetinvestment.domain.models.portfolio.FDDetailsDomain
import org.sharad.velvetinvestment.domain.usecases.fdusecases.GetFDDetailsUseCase
import org.sharad.velvetinvestment.presentation.portfolio.models.FDDetailsUiModel
import org.sharad.velvetinvestment.presentation.portfolio.models.FDNomineeUiModel
import org.sharad.velvetinvestment.utils.UIState
import org.sharad.velvetinvestment.utils.formatMoneyAfterL
import org.sharad.velvetinvestment.utils.isoUtcToDisplayDate
import org.sharad.velvetinvestment.utils.networking.onError
import org.sharad.velvetinvestment.utils.networking.onSuccess

class FDDetailsViewModel(
    private val getFDDetailsUseCase: GetFDDetailsUseCase,
    private val fdId: String
) : ViewModel() {

    private val _uiState = MutableStateFlow<UIState>(value = UIState.Loading)
    val uiState: StateFlow<UIState> = _uiState.asStateFlow()

    private val _fdDetails = MutableStateFlow<FDDetailsUiModel?>(null)
    val fdDetails: StateFlow<FDDetailsUiModel?> = _fdDetails

    init {
        loadFDDetails(fdId)
    }

    fun loadFDDetails(fdId: String) {
        viewModelScope.launch {

            _uiState.value = UIState.Loading

            getFDDetailsUseCase(fdId)
                .onSuccess { domain ->
                    _fdDetails.value = domain.toUiModel()
                    _uiState.value = UIState.Success
                }
                .onError { error ->
                    _uiState.value = UIState.Error(error.name.toString())
                }
        }
    }
}

private fun FDDetailsDomain.toUiModel(): FDDetailsUiModel {

    return FDDetailsUiModel(
        bankName = bankInfo.bankName,
        fdAccountNumber = bankInfo.fdAccountNumber,

        principalAmount = "₹ "+formatMoneyAfterL(investmentDetails.principalAmount),
        interestRate = "${investmentDetails.interestRate}%",
        tenure = "${investmentDetails.tenureMonths} months",
        maturityAmount = "₹ "+formatMoneyAfterL(investmentDetails.maturityAmount),
        interestEarnedTillDate = "₹ "+formatMoneyAfterL(investmentDetails.interestEarnedTillDate),

        nominees = nomineeDetails.map {
            FDNomineeUiModel(
                fullName = it.fullName,
                relationship = it.relationship,
                dateOfBirth = it.dateOfBirth.isoUtcToDisplayDate(),
                nomineeId = it.id
            )
        },

        startDate = timelineDetails.startDate.isoUtcToDisplayDate(),
        maturityDate = timelineDetails.maturityDate.isoUtcToDisplayDate(),
        daysRemaining = "${timelineDetails.daysRemaining} days"
    )
}
