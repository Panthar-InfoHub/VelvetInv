package org.sharad.velvetinvestment.presentation.onboarding.models

import org.sharad.velvetinvestment.domain.LoanTypes

data class LoanInfo(
    val loanType: LoanTypes,
    val outstandingAmount: Long,
    val monthlyEmi: Long,
    val tenure: Int,
)

