package org.sharad.velvetinvestment.presentation.fixeddeposits.uimodels

import org.sharad.velvetinvestment.domain.models.fixeddeposits.FixedDepositDomain
import org.sharad.velvetinvestment.utils.formatWithCommas

fun FixedDepositDomain.toUI(): FixedDepositUIModel {

    val tenuresUI = tenures.map {

        val minDays = it.tenure.min.totalDays
        val maxDays = it.tenure.max.totalDays

        FixedDepositTenureUIModel(
            tenureText = "${it.tenure.min} - ${it.tenure.max}",
            interestText = "${it.interestRate}%",
            returnText = "₹ ${formatWithCommas(it.receiveMin)} - ₹ ${formatWithCommas(it.receiveMax)}",
            minDays = minDays,
            maxDays = maxDays,
            interestRate = it.interestRate,
            returnMin = it.receiveMin,
            returnMax = it.receiveMax
        )
    }

    return FixedDepositUIModel(
        bankName = bankName,
        bankLogoUrl = bankLogoUrl,
        riskLevel = riskLevel,
        interest = "$baseInterest% p.a.",
        tenures = tenuresUI,
        id=id,
        bankTag = bankTag
    )
}