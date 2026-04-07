package org.sharad.velvetinvestment.data.remote.mapper


import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import org.sharad.velvetinvestment.data.remote.model.onboarding.*
import org.sharad.velvetinvestment.domain.models.goals.GoalRequest
import org.sharad.velvetinvestment.presentation.onboarding.models.*
import org.sharad.velvetinvestment.utils.DateTimeUtils.epochToYYYYMMdd


fun AssetFlowDetails.toAssetsDto(): Assets {
    return Assets(
        cash_saving = cash ?: 0L,
        fd = fixedDeposits ?: 0L,
        gold = goldAndCommodities ?: 0L,
        mutual_funds = mutualFunds ?: 0L,
        real_estate = realEstate ?: 0L,
        stocks = stocksAndShares ?: 0L
    )
}


// -------------------------------
// ✅ FINANCE MAPPER
// -------------------------------

fun FinancialFlowDetails.toFinanceDto(): Finance {
    return Finance(
        annual_income = annualIncome ?: 0L,
        expense_food = foodExpense ?: 0L,
        expense_house = houseExpense ?: 0L,
        expense_others = otherExpense ?: 0L,
        expense_transportation = transportExpense ?: 0L
    )
}


// -------------------------------
// ✅ INSURANCE MAPPER
// -------------------------------

fun Pair<Long, Long>.toInsuranceDto(): Insurance {
    return Insurance(
        health_insurance = first,
        life_insurance = second
    )
}


// -------------------------------
// ✅ LOAN MAPPER
// -------------------------------

fun List<LoanInfo>.toLoanDto(): List<Loan> {
    return map {
        Loan(
            loan_name = it.loanType.code,
            loan_type = it.loanType.name,
            monthly_emi = it.monthlyEmi,
            outstanding_amount = it.outstandingAmount,
            tenure_months = it.tenure
        )
    }
}


// -------------------------------
// ✅ PROFILE MAPPER
// -------------------------------

fun PersonalDetails.toProfileDto(): Profile {
    return Profile(
        city = city,
        dob = epochToYYYYMMdd(dob),
        full_name = fullName,
        email = email
    )
}


// -------------------------------
// ✅ GOALS MAPPER
// -------------------------------

fun List<GoalRequest>.toGoalsDto(): List<JsonElement> {
    return map { it.toDto() }.map { it.toJsonElement() }
}

