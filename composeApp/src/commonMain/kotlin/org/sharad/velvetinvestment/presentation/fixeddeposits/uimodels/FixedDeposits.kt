package org.sharad.velvetinvestment.presentation.fixeddeposits.uimodels

import org.sharad.velvetinvestment.domain.models.fd.FDTenureDomain
import org.sharad.velvetinvestment.presentation.fixeddeposits.viewmodel.calculateMaturity
import org.sharad.velvetinvestment.utils.formatMoneyAfterL

fun FDTenureDomain.toUIModel(invest: Long): FixedDepositTenureUIModel {
    val maturity = calculateMaturity(
        principal = invest,
        rate = interestRate,
        days = tenureDays,
        frequency = payoutFrequency
    )

    return FixedDepositTenureUIModel(
        tenureText = tenureLabel,
        interestText = "${interestRate}%",
        returnText = formatMoneyAfterL(maturity.toLong())
    )
}