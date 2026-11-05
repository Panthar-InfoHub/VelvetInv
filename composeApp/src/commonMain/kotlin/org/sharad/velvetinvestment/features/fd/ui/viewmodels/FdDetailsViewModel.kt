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
import org.sharad.velvetinvestment.utils.SnackBarController
import org.sharad.velvetinvestment.features.fd.domain.model.CustomerType
import org.sharad.velvetinvestment.features.fd.domain.model.FDDetailsDomain
import org.sharad.velvetinvestment.features.fd.domain.model.PayoutType
import org.sharad.velvetinvestment.features.fd.domain.usecases.GetFDDetailsUseCase
import org.sharad.velvetinvestment.features.fd.domain.utils.calculateMaturity
import org.sharad.velvetinvestment.features.fd.ui.viewmodels.FdDetailsEffect.*

data class FDTenureUiModel(
    val id: String,
    val tenureLabel: String,
    val tenureDays: Int,
    val interestRate: Double,
    val annualYield: Double,
    /**
     * Whether this slab pays the best rate on offer for its payout mode. Worked out from the rates
     * themselves rather than taken from the gateway's `is_default_selection`, which marks the slab
     * the issuer wants pre-picked and is not always the one that pays most. Ties are kept: when
     * several terms share the top rate, every one of them is flagged.
     */
    val isMaxReturn: Boolean,
    val payoutFrequency: PayoutType,
    val maturityAmount: Long
)

data class FdDetailsUiState(
    val details: FDDetailsDomain? = null,
    val calculatedTenures: List<FDTenureUiModel> = emptyList(),
    val selectedPayoutMode: PayoutType = PayoutType.Cumulative,
    val selectedCustomerType: CustomerType = CustomerType.STANDARD,
    /** The dropdowns offer every option the API accepts, not just the ones a response contains. */
    val frequencies: List<PayoutType> = PayoutType.all,
    val customerTypes: List<CustomerType> = CustomerType.entries,
    val isLoading: Boolean = false,
    /**
     * A payout or applicant change is re-fetching the rates. Only the figures that depend on them
     * are skeletoned; the rest of the screen stays as it is.
     */
    val isRefreshingRates: Boolean = false,
    val errorMessage: String? = null,
    val activeSheet: FDModalType? = null,
    val investInput: String = "",
){
    /** The server returns only the selected payout and applicant's rates, so they are shown as they come. */
    val tenureRows: List<FDTenureUiModel>
        get() = calculatedTenures.reversed()

    /**
     * The slab the header card quotes its interest and tenure from.
     *
     * It is read off the same list the table below shows, so the two can never disagree. Where
     * several terms tie at the best rate the shortest wins: the same return, reached sooner, is
     * the one worth advertising.
     */
    val headlineTenure: FDTenureUiModel?
        get() = tenureRows.filter { it.isMaxReturn }.minByOrNull { it.tenureDays }
            ?: tenureRows.maxByOrNull { it.interestRate }
}

enum class FDModalType {
    PAYOUT,
    APPLICABLE,
    INVEST
}

sealed interface FdDetailsEvent {
    data object OnBackClicked : FdDetailsEvent
    data object LoadData : FdDetailsEvent
    data object OnShareClicked : FdDetailsEvent
    data class OnUpdateInvest(val amount: String) : FdDetailsEvent
    data class OnUpdatePayout(val payout: PayoutType) : FdDetailsEvent
    data class OnUpdateApplicable(val customerType: CustomerType) : FdDetailsEvent
    data object OnEditAmountClicked : FdDetailsEvent
    data object OnPayoutTypeClicked : FdDetailsEvent
    data object OnApplicantCategoryClicked : FdDetailsEvent
    data class OnTenureSelected(val tenureId: String) : FdDetailsEvent
    data object OnInvestNowClicked : FdDetailsEvent
    data object OnCloseSheet : FdDetailsEvent
}

sealed interface FdDetailsEffect {
    object NavigateBack : FdDetailsEffect
    data class NavigateToSetInvestmentDetails(val id: String) : FdDetailsEffect
}

class FdDetailsViewModel(
    private val id: String,
    private val getFDDetailsUseCase: GetFDDetailsUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(FdDetailsUiState())
    val uiState: StateFlow<FdDetailsUiState> = _uiState.asStateFlow()

    private val _effect = Channel<FdDetailsEffect>()
    val effect = _effect.receiveAsFlow()

    private var ratesJob: Job? = null

    /** The selection the rates on screen were fetched for, restored when a re-fetch fails. */
    private var appliedPayout: PayoutType = PayoutType.Cumulative
    private var appliedCustomerType: CustomerType = CustomerType.STANDARD
    init{
        loadFdDetails()
    }

    fun handleEvent(event: FdDetailsEvent) {
        when (event) {
            FdDetailsEvent.OnBackClicked -> sendEffect(FdDetailsEffect.NavigateBack)
            FdDetailsEvent.OnShareClicked -> {
                _uiState.value.details?.let {
//                    sendEffect(FdDetailsEffect.ShareFdDetails("Check out this FD from ${it.bankName}"))
                }
            }
            FdDetailsEvent.OnEditAmountClicked -> {
                // Velvet's details screen picks these in bottom sheets (JN uses inline dropdowns).
                _uiState.update { it.copy(activeSheet = FDModalType.INVEST) }
            }
            FdDetailsEvent.OnPayoutTypeClicked -> {
                _uiState.update { it.copy(activeSheet = FDModalType.PAYOUT) }
            }
            FdDetailsEvent.OnApplicantCategoryClicked -> {
                _uiState.update { it.copy(activeSheet = FDModalType.APPLICABLE) }
            }
            is FdDetailsEvent.OnUpdateInvest -> {
                val cleanAmount = event.amount.filter { it.isDigit() }
                _uiState.update { it.copy(investInput = cleanAmount) }
                val amountLong = cleanAmount.toLongOrNull()
                if (amountLong != null) {
                    updateInvest(amountLong)
                } else if (cleanAmount.isEmpty()) {
                    updateInvest(0L)
                }
            }
            is FdDetailsEvent.OnUpdatePayout -> onRateSelectionChanged(payout = event.payout)
            is FdDetailsEvent.OnUpdateApplicable -> onRateSelectionChanged(customerType = event.customerType)
            is FdDetailsEvent.OnTenureSelected -> {
                // Update selected tenure logic if needed in domain model or state
            }
            FdDetailsEvent.OnInvestNowClicked -> {
                _uiState.value.details?.let {
                    sendEffect(NavigateToSetInvestmentDetails(it.id))
                }
            }
            FdDetailsEvent.OnCloseSheet -> {
                _uiState.update { it.copy(activeSheet = null) }
            }

            FdDetailsEvent.LoadData -> loadFdDetails()
        }
    }

    private fun updateInvest(amount: Long) {
        _uiState.update { state ->
            val updatedDetails = state.details?.copy(invest = amount)
            state.copy(
                details = updatedDetails,
                calculatedTenures = calculateTenureReturns(updatedDetails, state.selectedPayoutMode)
            )
        }
    }

    /**
     * Re-fetches the rates for a new payout or applicant selection.
     *
     * The dropdown moves at once and only the rate-dependent figures wait on the call. A newer
     * change cancels the call in flight, so a slow answer can never land over a later choice.
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

        ratesJob?.cancel()
        ratesJob = viewModelScope.launch {
            getFDDetailsUseCase(id, payout, customerType)
                .onSuccess { data ->
                    _uiState.update { it.withRates(data, payout, customerType).copy(isRefreshingRates = false) }
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
                }
        }
    }

    /**
     * [data]'s rates under the selection they were fetched for. Its `invest` is only the minimum
     * deposit, so an amount the user has already entered is kept.
     */
    private fun FdDetailsUiState.withRates(
        data: FDDetailsDomain,
        payout: PayoutType,
        customerType: CustomerType
    ): FdDetailsUiState {
        appliedPayout = payout
        appliedCustomerType = customerType
        val updated = data.copy(
            invest = details?.invest ?: data.invest,
            selectedPayout = payout,
            applicable = customerType.displayName
        )
        return copy(
            details = updated,
            selectedPayoutMode = payout,
            selectedCustomerType = customerType,
            calculatedTenures = calculateTenureReturns(updated, payout)
        )
    }

    private fun calculateTenureReturns(
        details: FDDetailsDomain?,
        payoutType: PayoutType?
    ): List<FDTenureUiModel> {
        if (details == null) return emptyList()
        val currentPayout = payoutType ?: details.selectedPayout ?: PayoutType.Cumulative

        // The table is narrowed to a single payout mode before it is shown, so the best rate is
        // decided within each mode — whichever list the user is looking at names its own best.
        val bestRateByPayout = details.interestRates
            .groupBy { it.payoutFrequency.id }
            .mapValues { (_, rates) -> rates.maxOf { it.interestRate } }

        return details.interestRates.map { tenure ->
            val maturity = calculateMaturity(
                principal = details.invest,
                rate = tenure.interestRate,
                days = tenure.tenureDays,
                frequency = currentPayout
            )
            FDTenureUiModel(
                id = tenure.id,
                tenureLabel = tenure.tenureLabel,
                tenureDays = tenure.tenureDays,
                interestRate = tenure.interestRate,
                annualYield = tenure.annualYield,
                // Rates are parsed from strings, so equal slabs land on the same Double; the
                // tolerance only guards against a value that arrives fractionally off.
                isMaxReturn = bestRateByPayout[tenure.payoutFrequency.id]
                    ?.let { best -> tenure.interestRate >= best - RATE_EQUALITY_TOLERANCE }
                    ?: false,
                payoutFrequency = tenure.payoutFrequency,
                maturityAmount = maturity.toLong()
            )
        }
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
                        it.copy(details = null).withRates(data, payout, customerType).copy(
                            investInput = data.invest.toString(),
                            isLoading = false,
                            errorMessage = null
                        )
                    }
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

    private fun sendEffect(effect: FdDetailsEffect) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }

    private companion object {
        /** Two rates within this of each other are the same rate as far as the badge is concerned. */
        const val RATE_EQUALITY_TOLERANCE = 0.0001
    }
}
