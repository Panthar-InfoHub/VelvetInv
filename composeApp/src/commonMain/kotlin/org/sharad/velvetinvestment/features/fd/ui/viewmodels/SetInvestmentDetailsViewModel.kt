package org.sharad.velvetinvestment.features.fd.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.sharad.velvetinvestment.utils.networking.onError
import org.sharad.velvetinvestment.utils.networking.onSuccess
import org.sharad.velvetinvestment.features.fd.data.models.dto.PurchaseFDBodyDto
import org.sharad.velvetinvestment.features.fd.domain.model.CustomerType
import org.sharad.velvetinvestment.features.fd.domain.model.FDDetailsDomain
import org.sharad.velvetinvestment.features.fd.domain.model.FDTenureDomain
import org.sharad.velvetinvestment.features.fd.domain.model.PayoutType
import org.sharad.velvetinvestment.features.fd.domain.usecases.GetFDDetailsUseCase
import org.sharad.velvetinvestment.features.fd.domain.usecases.PurchaseFDUseCase
import org.sharad.velvetinvestment.features.fd.domain.utils.calculateMaturity
import org.sharad.velvetinvestment.features.fd.domain.utils.trimTo
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.Padding
import kotlinx.datetime.format.char
import kotlinx.datetime.plus
import kotlinx.datetime.todayIn
import org.sharad.velvetinvestment.utils.SnackBarController

data class SetInvestmentDetailsUiState(
    val details: FDDetailsDomain? = null,
    val amount: String = "",
    val selectedTenure: FDTenureDomain? = null,
    val selectedPayoutMode: PayoutType = PayoutType.Cumulative,
    val selectedCustomerType: CustomerType = CustomerType.STANDARD,
    /** Every option the API accepts, as on the FD details screen. */
    val frequencies: List<PayoutType> = PayoutType.all,
    val customerTypes: List<CustomerType> = CustomerType.entries,
    /** The tenures for the selected payout and applicant, exactly as the server returned them. */
    val availableTenures: List<FDTenureDomain> = emptyList(),
    /** A payout or applicant change is re-fetching the rates; tenures and returns are skeletoned. */
    val isRefreshingRates: Boolean = false,
    val maturityAmount: String = "₹0",
    val totalInterest: String = "₹0",
    val interestRate: String = "0% p.a.",
    val maturityDate: String = "N/A",
    val isLoading: Boolean = false,
    val isPurchasing: Boolean = false,
    val errorMessage: String? = null,
    val showError: Boolean = false,
    val errorText: String = "",
    val minAmount: Long = 0,
    val isButtonEnabled: Boolean = false
)

sealed interface SetInvestmentDetailsEvent {
    data object LoadDetails : SetInvestmentDetailsEvent
    data class OnAmountChanged(val amount: String) : SetInvestmentDetailsEvent
    data class OnTenureChanged(val tenure: FDTenureDomain) : SetInvestmentDetailsEvent
    data class OnPayoutModeChanged(val payout: PayoutType) : SetInvestmentDetailsEvent
    data class OnCustomerTypeChanged(val customerType: CustomerType) : SetInvestmentDetailsEvent
    data object OnBackClicked : SetInvestmentDetailsEvent
    data object OnContinueClicked : SetInvestmentDetailsEvent
}

sealed interface SetInvestmentDetailsEffect {
    data object NavigateBack : SetInvestmentDetailsEffect
    data class LaunchWebView(val url: String) : SetInvestmentDetailsEffect
}

class SetInvestmentDetailsViewModel(
    private val id: String,
    private val getFDDetailsUseCase: GetFDDetailsUseCase,
    private val purchaseFDUseCase: PurchaseFDUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SetInvestmentDetailsUiState())
    val uiState: StateFlow<SetInvestmentDetailsUiState> = _uiState.asStateFlow()

    private val _effect = Channel<SetInvestmentDetailsEffect>()
    val effect = _effect.receiveAsFlow()

    private var ratesJob: Job? = null

    /** The selection the rates on screen were fetched for, restored when a re-fetch fails. */
    private var appliedPayout: PayoutType = PayoutType.Cumulative
    private var appliedCustomerType: CustomerType = CustomerType.STANDARD

    fun handleEvent(event: SetInvestmentDetailsEvent) {
        when (event) {
            is SetInvestmentDetailsEvent.LoadDetails -> loadFdDetails()
            is SetInvestmentDetailsEvent.OnAmountChanged -> {
                updateAmount(event.amount)
            }

            is SetInvestmentDetailsEvent.OnTenureChanged -> updateTenure(event.tenure)

            is SetInvestmentDetailsEvent.OnPayoutModeChanged ->
                onRateSelectionChanged(payout = event.payout)

            is SetInvestmentDetailsEvent.OnCustomerTypeChanged ->
                onRateSelectionChanged(customerType = event.customerType)

            SetInvestmentDetailsEvent.OnBackClicked -> {
                sendEffect(SetInvestmentDetailsEffect.NavigateBack)
            }

            SetInvestmentDetailsEvent.OnContinueClicked -> {
                purchaseFD()
            }
        }
    }
    init{
        loadFdDetails()
    }
    /** The full-screen load: first entry, and retry from the error screen. */
    private fun loadFdDetails() {
        ratesJob?.cancel()
        _uiState.update { it.copy(isLoading = true, isRefreshingRates = false) }
        // A retry asks again for what is on screen, not for a selection that just failed.
        val payout = appliedPayout
        val customerType = appliedCustomerType
        viewModelScope.launch {
            getFDDetailsUseCase(id, payout, customerType)
                .onSuccess { data ->
                    _uiState.update {
                        it.copy(selectedTenure = null).withRates(data, payout, customerType).copy(
                            amount = data.minDeposit.toString(),
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                    updateAmount(_uiState.value.amount)
                }
                .onError { error ->
                    SnackBarController.showError(error.message)
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.message
                        )
                    }
                }
        }
    }

    private fun updateAmount(input: String) {
        _uiState.update { state ->
            val parsedAmount = input.toLongOrNull()

            val hasInvalidNumber = input.isNotBlank() && parsedAmount == null
            val isBelowMinAmount = parsedAmount != null && parsedAmount < state.minAmount

            val errorText = when {
                hasInvalidNumber -> "Please enter a valid amount"
                isBelowMinAmount -> "Minimum amount is ₹${state.minAmount}"
                else -> ""
            }

            state.copy(
                amount = input,
                showError = hasInvalidNumber || isBelowMinAmount,
                errorText = errorText
            )
        }
        calculateReturns()
        updateButtonState()
    }

    private fun updateTenure(tenure: FDTenureDomain) {
        _uiState.update {
            it.copy(
                selectedTenure = tenure
            )
        }

        calculateReturns()
        updateButtonState()
    }

    /**
     * Re-fetches the rates for a new payout or applicant selection.
     *
     * The dropdown moves at once; tenures and returns wait on the call, and Invest Now stays off
     * until they land. A newer change cancels the call in flight.
     */
    private fun onRateSelectionChanged(
        payout: PayoutType = _uiState.value.selectedPayoutMode,
        customerType: CustomerType = _uiState.value.selectedCustomerType
    ) {
        val current = _uiState.value
        if (payout == current.selectedPayoutMode && customerType == current.selectedCustomerType) return

        _uiState.update {
            it.copy(
                selectedPayoutMode = payout,
                selectedCustomerType = customerType,
                isRefreshingRates = true
            )
        }
        updateButtonState()

        ratesJob?.cancel()
        ratesJob = viewModelScope.launch {
            getFDDetailsUseCase(id, payout, customerType)
                .onSuccess { data ->
                    _uiState.update { it.withRates(data, payout, customerType).copy(isRefreshingRates = false) }
                    // The minimum may differ for the new selection, so the amount is re-validated.
                    updateAmount(_uiState.value.amount)
                }
                .onError { error ->
                    SnackBarController.showError(error.message)
                    _uiState.update {
                        it.copy(
                            selectedPayoutMode = appliedPayout,
                            selectedCustomerType = appliedCustomerType,
                            isRefreshingRates = false
                        )
                    }
                    updateButtonState()
                }
        }
    }

    /**
     * [data]'s rates under the selection they were fetched for. A tenure already picked stays
     * picked when the new rates still offer that term, now at its new rate.
     */
    private fun SetInvestmentDetailsUiState.withRates(
        data: FDDetailsDomain,
        payout: PayoutType,
        customerType: CustomerType
    ): SetInvestmentDetailsUiState {
        appliedPayout = payout
        appliedCustomerType = customerType
        return copy(
            details = data,
            minAmount = data.minDeposit,
            selectedPayoutMode = payout,
            selectedCustomerType = customerType,
            availableTenures = data.interestRates,
            selectedTenure = selectedTenure?.let { picked ->
                data.interestRates.firstOrNull { it.tenureDays == picked.tenureDays }
            }
        )
    }

    private fun calculateReturns() {
        val state = _uiState.value
        val principal = state.amount.toLongOrNull() ?: 0L
        val tenure = state.selectedTenure
        val payout = state.selectedPayoutMode

        if (tenure == null || principal == 0L) {
            _uiState.update {
                it.copy(
                    maturityAmount = "₹0",
                    totalInterest = "₹0",
                    interestRate = tenure?.let { rate -> "${rate.interestRate}% p.a." } ?: "0% p.a.",
                    maturityDate = "N/A"
                )
            }
            return
        }

        val maturityValue = calculateMaturity(
            principal = principal,
            rate = tenure.interestRate,
            days = tenure.tenureDays,
            frequency = payout
        )

        val totalInterest = maturityValue - principal
        val today = kotlin.time.Clock.System.todayIn(TimeZone.currentSystemDefault())
        val maturityDateObj = today.plus(tenure.tenureDays, DateTimeUnit.DAY)
        val formattedDate = dateFormatter.format(maturityDateObj)

        _uiState.update {
            it.copy(
                maturityAmount = "₹${maturityValue.trimTo(2)}",
                totalInterest = "₹${totalInterest.trimTo(2)}",
                interestRate = "${tenure.interestRate}% p.a.",
                maturityDate = formattedDate
            )
        }
    }
    private val dateFormatter = LocalDate.Format {
        this@Format.day(padding = Padding.ZERO)
        char(' ')
        monthName(MonthNames.ENGLISH_ABBREVIATED) // e.g., "Jan", "Oct"
        char(' ')
        year()
    }

    private fun updateButtonState() {
        _uiState.update { state ->
            val amount = state.amount.toLongOrNull()

            state.copy(
                isButtonEnabled =
                    state.selectedTenure != null &&
                            !state.isRefreshingRates &&
                            amount != null &&
                            amount > 0 &&
                            amount >= state.minAmount &&
                            !state.showError
            )
        }
    }

    private fun purchaseFD() {
        val state = _uiState.value
        val details = state.details ?: return
        val tenure = state.selectedTenure ?: return
        val amount = state.amount.toLongOrNull() ?: return
        val payout = state.selectedPayoutMode

        _uiState.update { it.copy(isPurchasing = true) }
        viewModelScope.launch {
            val body = PurchaseFDBodyDto(
                investment_amount = amount,
                investment_period = tenure.tenureDays,
                payout_frequency = payout.id,
                product_id = details.id,
                tenure = tenure.tenureDays
            )
            purchaseFDUseCase(body)
                .onSuccess { url ->
                    _uiState.update { it.copy(isPurchasing = false) }
                    // The web view pops this screen too once the payment flow finishes.
                    sendEffect(SetInvestmentDetailsEffect.LaunchWebView(url))
                }
                .onError { error ->
                    SnackBarController.showError(error.message)
                    _uiState.update {
                        it.copy(
                            isPurchasing = false,
                            errorMessage = error.message
                        )
                    }
                }
        }
    }

    private fun sendEffect(effect: SetInvestmentDetailsEffect) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }
}
