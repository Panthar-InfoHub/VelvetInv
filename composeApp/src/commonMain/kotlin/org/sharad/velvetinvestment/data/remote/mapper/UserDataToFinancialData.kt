package org.sharad.velvetinvestment.data.remote.mapper

import org.sharad.velvetinvestment.data.remote.model.useedata.UserFinance
import org.sharad.velvetinvestment.presentation.onboarding.models.FinancialFlowDetails

fun UserFinance.toFinancialFlowDetails(): FinancialFlowDetails {
    return FinancialFlowDetails(
        annualIncome = annual_income.toLongOrNull(),
        houseExpense = expense_house.toLongOrNull(),
        foodExpense = expense_food.toLongOrNull(),
        transportExpense = expense_transportation.toLongOrNull(),
        otherExpense = expense_others.toLongOrNull()
    )
}