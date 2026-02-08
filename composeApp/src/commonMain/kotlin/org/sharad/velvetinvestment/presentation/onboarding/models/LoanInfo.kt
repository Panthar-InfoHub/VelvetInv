package org.sharad.velvetinvestment.presentation.onboarding.models

import org.sharad.velvetinvestment.domain.LoanTypes

data class LoanInfo(
    val loanType: LoanTypes?=null,
    val outstandingAmount: Long?=null,
    val monthlyEmi: Long?=null,
    val tenure: Int?=null,
)

