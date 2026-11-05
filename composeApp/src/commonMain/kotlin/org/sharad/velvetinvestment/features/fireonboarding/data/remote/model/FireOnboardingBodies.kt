package org.sharad.velvetinvestment.features.fireonboarding.data.remote.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

/**
 * Request bodies of the FIRE onboarding steps (`POST /onboarding/{finance|assets|loans|insurance|goals}`).
 * Every step takes one of two bodies: [FireStepSkipBodyDto] to skip it, or the step's own data
 * body with `skip = false`.
 */

/** `{ "skip": true }` — the same for every step. */
@Serializable
data class FireStepSkipBodyDto(
    val skip: Boolean = true
)

/** `POST /onboarding/finance`. Expenses are monthly, income is annual. */
@Serializable
data class FinanceStepBodyDto(
    val skip: Boolean = false,
    val annual_income: Long,
    val expense_house: Long,
    val expense_food: Long,
    val expense_transportation: Long,
    val expense_others: Long
)

/** `POST /onboarding/assets` */
@Serializable
data class AssetsStepBodyDto(
    val skip: Boolean = false,
    val mutual_funds: Long,
    val stocks: Long,
    val fd: Long,
    val real_estate: Long,
    val gold: Long,
    val cash_saving: Long
)

/** `POST /onboarding/loans` — the whole loan list in one go. */
@Serializable
data class LoansStepBodyDto(
    val skip: Boolean = false,
    val loans: List<LoanEntryDto>
)

@Serializable
data class LoanEntryDto(
    val loan_type: String,
    val loan_name: String,
    val outstanding_amount: Long,
    val monthly_emi: Long,
    val tenure_months: Int
)

/** `POST /onboarding/insurance` — cover amounts, not premiums. */
@Serializable
data class InsuranceStepBodyDto(
    val skip: Boolean = false,
    val life_insurance: Long,
    val health_insurance: Long
)

/**
 * `POST /onboarding/goals`. Each goal is the same object `POST /user-goal/` takes, built by the
 * goals feature's `CreateGoalRequest.toCreateBody()` so type-specific fields are sent (or left out)
 * exactly as there.
 */
@Serializable
data class GoalsStepBodyDto(
    val skip: Boolean = false,
    val goals: List<JsonObject>
)
