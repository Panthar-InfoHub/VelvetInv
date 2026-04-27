package org.sharad.velvetinvestment.presentation.fixeddeposits.uimodels

import org.sharad.velvetinvestment.data.remote.mapper.PayoutType
import org.sharad.velvetinvestment.domain.models.fd.FDTenureDomain
import org.sharad.velvetinvestment.domain.models.fixeddeposits.RiskLevel

data class FDPurchaseUiModel(
    val tenures: List<FDTenureDomain>,
    val selectedTenure: FDTenureDomain?=null,
    val frequencies: List<PayoutType>,
    val selectedFrequency: PayoutType?=null,
    val amount:Long? = null,
    val amountInput: String = "",
    val showError: Boolean = false,
    val errorText: String = "",
    val minAmount: Long,
    val bankName: String,
    val bankLogo: String,
    val riskLabel: RiskLevel,
    val highestInterestRate: Double,
    val loading: Boolean = false,
    val id:String
)
