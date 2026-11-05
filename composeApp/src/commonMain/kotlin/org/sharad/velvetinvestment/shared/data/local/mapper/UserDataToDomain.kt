package org.sharad.velvetinvestment.shared.data.local.mapper

import org.sharad.velvetinvestment.features.bottomNavigation.domain.models.GoalsSummaryDomain
import org.sharad.velvetinvestment.features.goals.data.mapper.toDomain
import org.sharad.velvetinvestment.features.goals.data.mapper.toSummary
import org.sharad.velvetinvestment.shared.data.remote.model.userdata.DashboardDto
import org.sharad.velvetinvestment.shared.data.remote.model.userdata.EmiSplitDto
import org.sharad.velvetinvestment.shared.data.remote.model.userdata.UserHomeDataDto
import org.sharad.velvetinvestment.shared.data.remote.model.userdata.OnboardingStatusDto
import org.sharad.velvetinvestment.shared.data.remote.model.userdata.UserAssets
import org.sharad.velvetinvestment.shared.data.remote.model.userdata.UserDataDto
import org.sharad.velvetinvestment.shared.data.remote.model.userdata.UserFinance
import org.sharad.velvetinvestment.shared.data.remote.model.userdata.UserGoal
import org.sharad.velvetinvestment.shared.data.remote.model.userdata.UserInsurance
import org.sharad.velvetinvestment.shared.data.remote.model.userdata.UserLoan
import org.sharad.velvetinvestment.shared.domain.models.DashboardSummaryDomain
import org.sharad.velvetinvestment.shared.domain.models.EmiSplitDomain
import org.sharad.velvetinvestment.shared.domain.models.UserHomeDataDomain
import org.sharad.velvetinvestment.shared.domain.models.OnboardingStatusDomain
import org.sharad.velvetinvestment.shared.domain.models.UserAssetsDomain
import org.sharad.velvetinvestment.shared.domain.models.UserDataDomain
import org.sharad.velvetinvestment.shared.domain.models.UserFinanceDomain
import org.sharad.velvetinvestment.shared.domain.models.UserInsuranceDomain
import org.sharad.velvetinvestment.shared.domain.models.UserLoanDomain


fun UserDataDto.toDomain(): UserDataDomain = UserDataDomain(
    id = data.id,
    name = data.full_name.orEmpty().trim(),
    email = data.email.orEmpty(),
    mobile = data.phone_no.orEmpty(),
    dob = data.dob.orEmpty(),
    city = data.city,
    goals = data.user_goals.map { it.toGoalSummary() },
    onboarding = data.onboarding?.toDomain() ?: OnboardingStatusDomain.EMPTY,
    isSkipped = data.is_skip,
    isKycCompleted = data.is_kyc_completed,
    isKycUnderReview = data.kyc_under_review,
    dashboard = data.dashboard?.toDomain() ?: DashboardSummaryDomain(),
    investorProfile = data.investor_profile,
    investmentAccount = data.investment_account,
    mpinIsSetup = data.mpin_is_setup,
    mpinEnabled = data.mpin_enabled,
    finance = data.user_finance?.toDomain(),
    assets = data.user_assets?.toDomain(),
    insurance = data.user_insurance?.toDomain(),
    loans = data.user_loan.map { it.toDomain() },
    isFireCompleted = data.is_fire_completed || data.onboarding?.is_fire_completed == true,
    homeData = data.user_home_data?.toDomain() ?: UserHomeDataDomain()
)

fun UserHomeDataDto.toDomain(): UserHomeDataDomain = UserHomeDataDomain(
    netWorth = net_worth.toLong(),
    fireNumber = fire_number?.toDomain() ?: EmiSplitDomain(),
    totalExpenses = total_expenses?.toDomain() ?: EmiSplitDomain(),
    firePercentage = fire_percentage?.toDomain() ?: EmiSplitDomain()
)

fun EmiSplitDto.toDomain(): EmiSplitDomain = EmiSplitDomain(
    includeEmi = emi_include,
    excludeEmi = emi_exclude
)

fun OnboardingStatusDto.toDomain(): OnboardingStatusDomain = OnboardingStatusDomain(
    currentStage = current_stage.orEmpty(),
    basicDetailsStatus = basic_details_status.orEmpty(),
    readinessStatus = readiness_status.orEmpty(),
    kycStatus = kyc_status.orEmpty(),
    pennyDropStatus = penny_drop_status.orEmpty(),
    emailStatus = email_status.orEmpty(),
    profileStatus = profile_status.orEmpty(),
    nomineeStatus = nominee_status.orEmpty(),
    isCompleted = is_completed,
    completedAt = completed_at,
    kycFailureReason = reason?.takeIf { it.isNotBlank() } ?: UNKNOWN_KYC_FAILURE_REASON,
    financeStatus = finance_status ?: fire_stages["finance"].orEmpty(),
    assetsStatus = assets_status ?: fire_stages["assets"].orEmpty(),
    loanStatus = loan_status ?: fire_stages["loan"].orEmpty(),
    insuranceStatus = insurance_status ?: fire_stages["insurance"].orEmpty(),
    goalsStatus = goals_status ?: fire_stages["goals"].orEmpty(),
    isFireCompleted = is_fire_completed,
    fireCompletedAt = fire_completed_at
)

private const val UNKNOWN_KYC_FAILURE_REASON = "Failed due to unknown reason"


fun DashboardDto.toDomain(): DashboardSummaryDomain = DashboardSummaryDomain(
    portfolioValue = portfolio_value,
    mutualFunds = mutual_funds,
    fixedDeposits = fixed_deposits,
    totalReturns = total_returns,
    returnPercent = return_percent,
    monthChangePercent = month_change_percent
)

fun UserFinance.toDomain(): UserFinanceDomain = UserFinanceDomain(
    annualIncome = annual_income.toAmount(),
    houseExpense = expense_house.toAmount(),
    foodExpense = expense_food.toAmount(),
    transportExpense = expense_transportation.toAmount(),
    otherExpense = expense_others.toAmount()
)

fun UserAssets.toDomain(): UserAssetsDomain = UserAssetsDomain(
    mutualFunds = mutual_funds.toAmount(),
    stocks = stocks.toAmount(),
    fixedDeposits = fd.toAmount(),
    realEstate = real_estate.toAmount(),
    gold = gold.toAmount(),
    cash = cash_saving.toAmount()
)

fun UserInsurance.toDomain(): UserInsuranceDomain = UserInsuranceDomain(
    lifeInsurance = life_insurance.toAmount(),
    healthInsurance = health_insurance.toAmount()
)

fun UserLoan.toDomain(): UserLoanDomain = UserLoanDomain(
    id = id,
    loanType = loan_type.orEmpty(),
    loanName = loan_name.orEmpty(),
    outstandingAmount = outstanding_amount.toAmount(),
    monthlyEmi = monthly_emi.toAmount(),
    tenureMonths = tenure_months ?: 0
)

/** A goal card for home and the goals list, mapped exactly as the goals feature maps `GET /user-goal/`. */
fun UserGoal.toGoalSummary(): GoalsSummaryDomain = toDomain().toSummary()

/** Server amounts are Postgres numerics sent as strings, sometimes with decimals. */
private fun String?.toAmount(): Long =
    this?.trim()?.toDoubleOrNull()?.toLong() ?: 0L
