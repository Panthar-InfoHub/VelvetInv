package org.sharad.velvetinvestment.presentation.onboarding.models

data class FinancialFlowDetails(
    val annualIncome:Int,
    val houseExpense:Int,
    val foodExpense:Int,
    val transportExpense:Int,
    val otherExpense:Int
)

data class FinancialSummary(
    val totalExpense:Int,
    val monthlySurplus:Int,
    val savingsRate: Double
)
