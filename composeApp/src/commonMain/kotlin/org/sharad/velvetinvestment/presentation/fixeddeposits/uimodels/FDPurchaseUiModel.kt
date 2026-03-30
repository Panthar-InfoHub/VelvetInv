package org.sharad.velvetinvestment.presentation.fixeddeposits.uimodels

import org.sharad.velvetinvestment.domain.models.fd.FDTenureDomain
import org.sharad.velvetinvestment.domain.models.fixeddeposits.RiskLevel

data class FDPurchaseUiModel(
    val tenures: List<FDTenureDomain>,
    val selectedTenure: FDTenureDomain?=null,
    val frequencies: List<String>,
    val selectedFrequency: String?=null,
    val amount:Long,
    val minAmount: Long,
    val bankName: String,
    val bankLogo: String,
    val riskLabel: RiskLevel,
    val highestInterestRate: Double,
    val loading: Boolean = false
)
