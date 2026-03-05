package org.sharad.velvetinvestment.presentation.portfolio.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.sharad.velvetinvestment.domain.models.portfolio.FDDetailsDomain
import org.sharad.velvetinvestment.domain.usecases.fdportfoliousecases.GetFDDetailsUseCase
import org.sharad.velvetinvestment.presentation.portfolio.models.FDDetailsUiModel
import org.sharad.velvetinvestment.presentation.portfolio.models.FDNomineeUiModel
import org.sharad.velvetinvestment.utils.LoadingState
import org.sharad.velvetinvestment.utils.formatMoneyAfterL
import org.sharad.velvetinvestment.utils.isoUtcToDisplayDate
import org.sharad.velvetinvestment.utils.networking.onError
import org.sharad.velvetinvestment.utils.networking.onSuccess

class FDDetailsViewModel(
    private val getFDDetailsUseCase: GetFDDetailsUseCase,
    private val fdId: String
) : ViewModel() {

    private val _loadingState = MutableStateFlow<LoadingState>(value = LoadingState.Loading)
    val loadingState: StateFlow<LoadingState> = _loadingState.asStateFlow()

    private val _fdDetails = MutableStateFlow<FDDetailsUiModel?>(null)
    val fdDetails: StateFlow<FDDetailsUiModel?> = _fdDetails

    init {
        loadFDDetails(fdId)
    }

    fun loadFDDetails(fdId: String) {
        viewModelScope.launch {

            _loadingState.value = LoadingState.Loading

            getFDDetailsUseCase(fdId)
                .onSuccess { domain ->
                    _fdDetails.value = domain.toUiModel()
                    _loadingState.value = LoadingState.Success
                }
                .onError { error ->
                    _loadingState.value = LoadingState.Error(error.name)
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
