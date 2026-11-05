package org.sharad.velvetinvestment.features.fireonboarding.data.mapper

import org.sharad.velvetinvestment.domain.LoanTypes
import org.sharad.velvetinvestment.features.fireonboarding.data.remote.model.AssetsStepBodyDto
import org.sharad.velvetinvestment.features.fireonboarding.data.remote.model.FinanceStepBodyDto
import org.sharad.velvetinvestment.features.fireonboarding.data.remote.model.GoalsStepBodyDto
import org.sharad.velvetinvestment.features.fireonboarding.data.remote.model.InsuranceStepBodyDto
import org.sharad.velvetinvestment.features.fireonboarding.data.remote.model.LoanEntryDto
import org.sharad.velvetinvestment.features.fireonboarding.data.remote.model.LoansStepBodyDto
import org.sharad.velvetinvestment.features.fireonboarding.presentation.onboarding.models.AssetFlowDetails
import org.sharad.velvetinvestment.features.fireonboarding.presentation.onboarding.models.FinancialFlowDetails
import org.sharad.velvetinvestment.features.fireonboarding.presentation.onboarding.models.LoanInfo
import org.sharad.velvetinvestment.features.goals.data.mapper.toCreateBody
import org.sharad.velvetinvestment.features.goals.domain.models.CreateGoalRequest
import org.sharad.velvetinvestment.shared.domain.models.UserLoanDomain

/** Blank fields go out as zero: the step endpoints expect every key. */
fun FinancialFlowDetails.toStepBody(): FinanceStepBodyDto = FinanceStepBodyDto(
    annual_income = annualIncome ?: 0L,
    expense_house = houseExpense ?: 0L,
    expense_food = foodExpense ?: 0L,
    expense_transportation = transportExpense ?: 0L,
    expense_others = otherExpense ?: 0L
)

fun AssetFlowDetails.toStepBody(): AssetsStepBodyDto = AssetsStepBodyDto(
    mutual_funds = mutualFunds ?: 0L,
    stocks = stocksAndShares ?: 0L,
    fd = fixedDeposits ?: 0L,
    real_estate = realEstate ?: 0L,
    gold = goldAndCommodities ?: 0L,
    cash_saving = cash ?: 0L
)

fun List<LoanInfo>.toStepBody(): LoansStepBodyDto = LoansStepBodyDto(
    loans = map {
        LoanEntryDto(
            loan_type = it.loanType.apiCode,
            loan_name = it.loanType.displayName,
            outstanding_amount = it.outstandingAmount,
            monthly_emi = it.monthlyEmi,
            tenure_months = it.tenure
        )
    }
)

fun insuranceStepBody(lifeInsurance: Long, healthInsurance: Long): InsuranceStepBodyDto =
    InsuranceStepBodyDto(life_insurance = lifeInsurance, health_insurance = healthInsurance)

fun List<CreateGoalRequest>.toGoalsStepBody(): GoalsStepBodyDto =
    GoalsStepBodyDto(goals = map { it.toCreateBody() })

/**
 * The `loan_type` the onboarding endpoint takes. The API sample uses `HOME_LOAN` and `CAR_LOAN`;
 * the rest follow the same `<KIND>_LOAN` pattern.
 */
val LoanTypes.apiCode: String
    get() = when (this) {
        LoanTypes.VEHICLE_LOAN -> "CAR_LOAN"
        else -> name
    }

/**
 * A loan type as the server sends it back. `GET /user/` carries the onboarding codes
 * (`HOME_LOAN`, `CAR_LOAN`); loans saved by older builds carry the display name ("Home Loan").
 */
fun loanTypeFromServer(value: String?): LoanTypes {
    val v = value?.trim().orEmpty()
    return LoanTypes.entries.firstOrNull {
        it.apiCode.equals(v, ignoreCase = true) ||
                it.name.equals(v, ignoreCase = true) ||
                it.displayName.equals(v, ignoreCase = true)
    } ?: LoanTypes.OTHER
}

fun UserLoanDomain.toLoanInfo(): LoanInfo = LoanInfo(
    loanType = loanTypeFromServer(loanType),
    outstandingAmount = outstandingAmount,
    monthlyEmi = monthlyEmi,
    tenure = tenureMonths
)
