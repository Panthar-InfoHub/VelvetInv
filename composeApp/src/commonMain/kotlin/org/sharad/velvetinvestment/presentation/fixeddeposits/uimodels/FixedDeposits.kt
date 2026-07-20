package org.sharad.velvetinvestment.presentation.fixeddeposits.uimodels

import org.sharad.velvetinvestment.data.remote.mapper.format2
import org.sharad.velvetinvestment.domain.models.fd.FDTenureDomain
import org.sharad.velvetinvestment.presentation.fixeddeposits.viewmodel.calculateMaturity
import org.sharad.velvetinvestment.utils.formatMoneyAfterL
import kotlin.math.round

fun FDTenureDomain.toUIModel(invest: Long): FixedDepositTenureUIModel {
    val maturity = calculateMaturity(
        principal = invest,
        rate = interestRate,
        days = tenureDays,
        frequency = payoutFrequency
    )

    val formattedRate = (round(interestRate * 100) / 100)

    return FixedDepositTenureUIModel(
        tenureText = tenureLabel.replace(
            Regex("\\bMonth\\b", RegexOption.IGNORE_CASE),
            "Months"
        ),
        interestText = "${formattedRate.format2()}%",
        returnText = formatMoneyAfterL(maturity.toLong())
    )
}