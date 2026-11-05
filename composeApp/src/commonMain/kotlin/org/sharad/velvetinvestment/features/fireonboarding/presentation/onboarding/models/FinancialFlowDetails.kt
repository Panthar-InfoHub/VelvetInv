package org.sharad.velvetinvestment.features.fireonboarding.presentation.onboarding.models

import org.sharad.velvetinvestment.utils.formatMoneyWithUnits

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

/** Each expense's share of the monthly total; all zero while nothing is entered. */
fun FinancialFlowDetails.expensePercentages(): ExpensePercentages {
    val house = houseExpense ?: 0L
    val food = foodExpense ?: 0L
    val transport = transportExpense ?: 0L
    val other = otherExpense ?: 0L
    val total = house + food + transport + other
    if (total == 0L) return ExpensePercentages()
    return ExpensePercentages(
        housePercent = house.toFloat() / total * 100f,
        foodPercent = food.toFloat() / total * 100f,
        transportPercent = transport.toFloat() / total * 100f,
        otherPercent = other.toFloat() / total * 100f,
        totalExpense = total
    )
}

/** Monthly expense, surplus and savings rate, formatted for the summary card. */
fun FinancialFlowDetails.summary(): FinancialSummary {
    val monthlyIncome = (annualIncome ?: 0) / 12
    val totalExpense = (houseExpense ?: 0) + (foodExpense ?: 0) + (transportExpense ?: 0) + (otherExpense ?: 0)
    val surplus = monthlyIncome - totalExpense
    val savingsRate = if (monthlyIncome > 0) (surplus * 100) / monthlyIncome else 0
    return FinancialSummary(
        totalExpense = "₹${formatMoneyWithUnits(totalExpense)}",
        monthlySurplus = "₹${formatMoneyWithUnits(surplus)}",
        savingsRate = "$savingsRate%"
    )
}
