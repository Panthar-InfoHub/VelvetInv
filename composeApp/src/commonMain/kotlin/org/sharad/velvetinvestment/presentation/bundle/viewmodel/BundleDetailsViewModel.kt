package org.sharad.velvetinvestment.presentation.bundle.viewmodel

import org.sharad.velvetinvestment.data.remote.mapper.toFundDomain
import org.sharad.velvetinvestment.domain.usecases.fundusecases.GetMutualFundDetailsUseCase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.sharad.velvetinvestment.data.remote.mapper.deriveTransactionRules
import org.sharad.velvetinvestment.domain.models.bundle.BundleDomain
import org.sharad.velvetinvestment.domain.models.bundle.FundDomain
import org.sharad.velvetinvestment.domain.models.bundle.PortfolioSlotDomain
import org.sharad.velvetinvestment.domain.usecases.fixeddepositusecases.GetBundleFundsUseCase
import org.sharad.velvetinvestment.domain.usecases.fundusecases.AddBundleToCartLumpsumUseCase
import org.sharad.velvetinvestment.domain.usecases.fundusecases.AddBundleToCartSipUseCase
import org.sharad.velvetinvestment.utils.FundTypeSelector
import org.sharad.velvetinvestment.utils.SelectedFundType
import org.sharad.velvetinvestment.utils.SnackBarController
import org.sharad.velvetinvestment.utils.networking.onError
import org.sharad.velvetinvestment.utils.networking.onSuccess
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

data class BundleTransactionRules(
    val minBundleSipAmount: Int,
    val minBundleLumpsumAmount: Int,

    val minDailySipAmount: Int,
    val minWeeklySipAmount: Int,
    val minFortnightlySipAmount: Int,
    val minMonthlySipAmount: Int,
    val minQuarterlySipAmount: Int,
    val minSemiAnnualSipAmount: Int,
    val minAnnualSipAmount: Int,

    val sipAllowedDates: List<Int>,
    val sipFrequencies: List<String>
)
class BundleDetailsViewModel(
    private val bundleKey: String,
    private val getBundleFundsUseCase: GetBundleFundsUseCase,
    private val addBundleToCartLumpsumUseCase: AddBundleToCartLumpsumUseCase,
    private val addBundleToCartSipUseCase: AddBundleToCartSipUseCase,
    private val getMutualFundDetailsUseCase: GetMutualFundDetailsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<BundleDetailsState>(BundleDetailsState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _investmentAmount = MutableStateFlow(0L)
    val investmentAmount = _investmentAmount.asStateFlow()

    // Tracks whether the user has manually edited the amount so we don't
    // overwrite their input when the bundle/rules reload.
    private var hasUserSetAmount = false

    private val _selectedSipDay = MutableStateFlow<Int?>(null)
    val selectedSipDay = _selectedSipDay.asStateFlow()

    private val _isAddingToCart = MutableStateFlow(false)
    val isAddingToCart = _isAddingToCart.asStateFlow()

    init {
        loadBundleDetails()
    }

    fun setInvestmentAmount(amount: Long) {
        hasUserSetAmount = true
        _investmentAmount.value = amount
    }

    fun setSelectedSipDay(day: Int) {
        _selectedSipDay.value = day
    }

    fun loadBundleDetails() {
        viewModelScope.launch {
            _uiState.value = BundleDetailsState.Loading
            getBundleFundsUseCase(bundleKey)
                .onSuccess { bundle ->
                    val rules = bundle.deriveTransactionRules()
                    _uiState.value =
                        BundleDetailsState.Success(
                            data = bundle,
                            transactionRules = rules
                        )
                    
                    // Seed the investment amount from the bundle's minimum
                    // transaction amount for the currently selected fund type,
                    // unless the user has already entered their own amount.
                    if (!hasUserSetAmount) {
                        _investmentAmount.value =
                            if (FundTypeSelector.fundType.value == SelectedFundType.SIP) {
                                rules.minBundleSipAmount.toLong()
                            } else {
                                rules.minBundleLumpsumAmount.toLong()
                            }
                    }
                }
                .onError {
                    _uiState.value = BundleDetailsState.Error(it.message)
                }
        }
    }

    fun updateSelectedFund(categoryId: String, slotId: String, newFund: FundDomain) {
        val currentState = _uiState.value
        if (currentState is BundleDetailsState.Success) {
            val updatedCategories = currentState.data.categories.map { category ->
                if (category.id == categoryId) {
                    val updatedSlots = category.slots.map { slot ->
                        if (slot.id == slotId) {
                            slot.copy(selectedFund = newFund)
                        } else {
                            slot
                        }
                    }
                    category.copy(slots = updatedSlots)
                } else {
                    category
                }
            }
            val updatedBundle = currentState.data.copy(categories = updatedCategories)
            _uiState.value = currentState.copy(
                data = updatedBundle,
                transactionRules = updatedBundle.deriveTransactionRules()
            )
        }
    }

    fun addBundleToCart(onSuccess: () -> Unit) {
        val currentState = _uiState.value
        if (currentState !is BundleDetailsState.Success) return

        val slots = currentState.data.categories.flatMap { it.slots }

        if (slots.any { it.selectedFund == null }) {
            viewModelScope.launch{
                SnackBarController.showError("Please select a fund for all categories.")
            }
            return
        }

        val amount = _investmentAmount.value
        val fundType = FundTypeSelector.fundType.value

        val selections = slots.map { slot ->
            org.sharad.velvetinvestment.data.remote.model.bundlecart.BundleSelection(
                mf_product_id = slot.selectedFund!!.id,
                allocation_percentage = slot.allocationPercentage
            )
        }

        viewModelScope.launch {
            _isAddingToCart.value = true

            if (fundType == SelectedFundType.SIP) {
                val day = _selectedSipDay.value ?: return@launch

                val today = Clock.System.now()
                    .toLocalDateTime(TimeZone.currentSystemDefault())
                    .date

                val startDate = calculateSipStartDate(today, day)
                val endDate = startDate
                    .plus(DatePeriod(years = 40))
                    .minus(DatePeriod(days = 1))

                val request =
                    org.sharad.velvetinvestment.data.remote.model.bundlecart.AddBundleSipRequest(
                        bundle_id = bundleKey,
                        amount = amount,
                        sip_st_date = startDate.toString(),
                        sip_en_date = endDate.toString(),
                        sip_freq = "OM",
                        sip_day = day,
                        sip_amt = amount,
                        selections = selections
                    )

                addBundleToCartSipUseCase(request)
                    .onSuccess {
                        _isAddingToCart.value = false
                        SnackBarController.showSuccess("Bundle added to cart")
                        onSuccess()
                    }
                    .onError {
                        _isAddingToCart.value = false
                        SnackBarController.showError(it.message)
                    }
            } else {
                addBundleToCartLumpsumUseCase(
                    bundleId = bundleKey,
                    amount = amount,
                    selections = selections
                )
                    .onSuccess {
                        _isAddingToCart.value = false
                        SnackBarController.showSuccess("Bundle added to cart")
                        onSuccess()
                    }
                    .onError {
                        _isAddingToCart.value = false
                        SnackBarController.showError(it.message)
                    }
            }
        }
    }

    private fun calculateSipStartDate(today: LocalDate, sipDay: Int): LocalDate {
        val minimumDate = today.plus(DatePeriod(months = 1))
        var startDate = try {
            LocalDate(year = minimumDate.year, month = minimumDate.month, dayOfMonth = sipDay)
        } catch (e: Exception) {
             LocalDate(year = minimumDate.year, month = minimumDate.month, dayOfMonth = 28)
        }

        if (startDate < minimumDate) {
            val nextMonth = minimumDate.plus(DatePeriod(months = 1))
            startDate = try {
                LocalDate(year = nextMonth.year, month = nextMonth.month, dayOfMonth = sipDay)
            } catch (e: Exception) {
                LocalDate(year = nextMonth.year, month = nextMonth.month, dayOfMonth = 28)
            }
        }
        return startDate
    }

    fun updateSlots(
        categoryId: String,
        slots: List<PortfolioSlotDomain>
    ) {
        val currentState = _uiState.value

        if (currentState is BundleDetailsState.Success) {

            val updatedCategories =
                currentState.data.categories.map { category ->

                    if (category.id == categoryId) {
                        category.copy(slots = slots)
                    } else {
                        category
                    }
                }

            val updatedBundle = currentState.data.copy(
                categories = updatedCategories
            )

            _uiState.value = currentState.copy(
                data = updatedBundle,
                transactionRules = updatedBundle.deriveTransactionRules()
            )
        }
    }

    fun updateSlotWithMutualFund(categoryId: String, slotId: String, mutualFundId: String) {
        viewModelScope.launch {
            getMutualFundDetailsUseCase(mutualFundId)
                .onSuccess { details ->
                    val fundDomain = details.toFundDomain()
                    val currentState = _uiState.value
                    if (currentState is BundleDetailsState.Success) {
                        val updatedCategories = currentState.data.categories.map { category ->
                            if (category.id == categoryId) {
                                val updatedSlots = category.slots.map { slot ->
                                    if (slot.id == slotId) {
                                        slot.copy(selectedFund = fundDomain)
                                    } else {
                                        slot
                                    }
                                }
                                // Prepend to funds list if not already there
                                val updatedFunds = if (category.funds.none { it.id == fundDomain.id }) {
                                    listOf(fundDomain) + category.funds
                                } else {
                                    // Move to front if already exists
                                    listOf(fundDomain) + category.funds.filter { it.id != fundDomain.id }
                                }
                                category.copy(slots = updatedSlots, funds = updatedFunds)
                            } else {
                                category
                            }
                        }
                        val updatedBundle = currentState.data.copy(categories = updatedCategories)
                        _uiState.value = currentState.copy(
                            data = updatedBundle,
                            transactionRules = updatedBundle.deriveTransactionRules()
                        )
                    }
                }
                .onError {
                    SnackBarController.showError(it.message)
                }
        }
    }
}

sealed class BundleDetailsState {
    object Loading : BundleDetailsState()
    data class Success(val data: BundleDomain, val transactionRules: BundleTransactionRules) : BundleDetailsState()
    data class Error(val message: String) : BundleDetailsState()
}
