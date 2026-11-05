package org.sharad.velvetinvestment.shared.domain.models

/**
 * The FIRE questionnaire answers `GET /user/` carries. Each block is null (or the list empty) until
 * the user has filled in that step. Amounts are whole rupees.
 */

/** Income and the four monthly expense heads. */
data class UserFinanceDomain(
    val annualIncome: Long = 0L,
    val houseExpense: Long = 0L,
    val foodExpense: Long = 0L,
    val transportExpense: Long = 0L,
    val otherExpense: Long = 0L
) {
    val monthlyIncome: Double
        get() = annualIncome / 12.0

    val monthlyExpenses: Long
        get() = houseExpense + foodExpense + transportExpense + otherExpense
}

data class UserAssetsDomain(
    val mutualFunds: Long = 0L,
    val stocks: Long = 0L,
    val fixedDeposits: Long = 0L,
    val realEstate: Long = 0L,
    val gold: Long = 0L,
    val cash: Long = 0L
) {
    val total: Long
        get() = mutualFunds + stocks + fixedDeposits + realEstate + gold + cash
}

data class UserInsuranceDomain(
    val lifeInsurance: Long = 0L,
    val healthInsurance: Long = 0L
)

data class UserLoanDomain(
    val id: String,
    /** Server enum, e.g. `HOME_LOAN`, `CAR_LOAN`. */
    val loanType: String,
    val loanName: String,
    val outstandingAmount: Long,
    val monthlyEmi: Long,
    val tenureMonths: Int
)
