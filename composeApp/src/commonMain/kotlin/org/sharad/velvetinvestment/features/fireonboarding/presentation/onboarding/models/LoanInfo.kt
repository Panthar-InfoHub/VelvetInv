package org.sharad.velvetinvestment.features.fireonboarding.presentation.onboarding.models

import org.sharad.velvetinvestment.domain.LoanTypes

data class LoanInfo(
    val loanType: LoanTypes,
    val outstandingAmount: Long,
    val monthlyEmi: Long,
    val tenure: Int,
)

