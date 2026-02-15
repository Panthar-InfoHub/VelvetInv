package org.sharad.velvetinvestment.presentation.portfolio.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import org.sharad.velvetinvestment.domain.TransactionStatus
import org.sharad.velvetinvestment.domain.models.portfolio.BankDetails
import org.sharad.velvetinvestment.domain.models.portfolio.SIPDetailsDomain
import org.sharad.velvetinvestment.domain.models.portfolio.TransactionHistoryDomain
import org.sharad.velvetinvestment.utils.UIState
import kotlin.time.Clock

class SIPDetailsViewModel: ViewModel() {

    private val _uiState = MutableStateFlow<UIState>(UIState.Loading)
    val uiState: StateFlow<UIState> = _uiState.asStateFlow()

    private val _sipDetails = MutableStateFlow<SIPDetailsDomain?>(null)
    val sipDetails: StateFlow<SIPDetailsDomain?> = _sipDetails.asStateFlow()

    init {
        loadSIPDetails()
    }

    fun loadSIPDetails() {
        viewModelScope.launch {
            _uiState.value = UIState.Loading

            delay(1500) // simulate API delay

            try {

                val fakeData = SIPDetailsDomain(
                    id = "1",
                    icon = "",
                    fundName = "Axis Small Cap Fund",
                    fundCategory = "Equity • Small Cap",
                    amount = "₹5,000",
                    metadata = listOf(
                        "Total Returns" to "-60 (6.00%)",
                        "Day returns" to "-40.05 (4.09%)",
                        "XIRR" to "-19.11%",
                        "Current NAV" to "137.42",
                        "Avg NAV" to "146.32",
                        "Folio no." to "22265704/95",
                        "Balance Units" to "6.834"
                    ),
                    nextInstallment = "05 Feb 2026",
                    sipId = "SIP123456",
                    autopayId = "AUTO987654",
                    transactionHistory = listOf(
                        TransactionHistoryDomain(
                            title = "6 Installment",
                            date = "10 Feb 2026 at 10:30 am",
                            type = TransactionStatus.SUCCESS
                        ),
                        TransactionHistoryDomain(
                            title = "7 Installment",
                            date = "10 Jan 2026 at 10:30 am",
                            type = TransactionStatus.SUCCESS
                        ),
                        TransactionHistoryDomain(
                            title = "Failed",
                            date = "10 Dec 2025 at 10:30 am",
                            type = TransactionStatus.FAILED
                        )
                    ),
                    bankDetails = BankDetails(
                        bankName = "HDFC Bank",
                        accountNumber = "XXXXXX4598",
                        bankIcon = ""
                    )
                )

                _sipDetails.value = fakeData
                _uiState.value = UIState.Success

            } catch (e: Exception) {
                _uiState.value = UIState.Error("Failed to load SIP details")
            }
        }
    }

}