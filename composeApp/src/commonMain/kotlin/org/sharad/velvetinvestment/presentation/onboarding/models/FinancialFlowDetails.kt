package org.sharad.velvetinvestment.presentation.onboarding.models

data class FinancialFlowDetails(
    val annualIncome:Long?=null,
    val houseExpense:Long?=null,
    val foodExpense:Long?=null,
    val transportExpense:Long?=null,
    val otherExpense:Long?=null
)

data class FinancialSummary(
    val totalExpense: String,
    val monthlySurplus:String,
    val savingsRate: String
)

data class ExpensePercentages(
    val housePercent: Float = 0f,
    val foodPercent: Float = 0f,
    val transportPercent: Float = 0f,
    val otherPercent: Float = 0f,
    val totalExpense: Long = 0L
)
