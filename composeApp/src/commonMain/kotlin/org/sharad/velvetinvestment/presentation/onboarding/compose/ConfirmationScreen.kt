package org.sharad.velvetinvestment.presentation.onboarding.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import org.sharad.emify.core.ui.theme.Primary
import org.sharad.emify.core.ui.theme.Secondary
import org.sharad.emify.core.ui.theme.bgColor1
import org.sharad.emify.core.ui.theme.titleColor
import org.sharad.velvetinvestment.data.remote.model.onboarding.Assets
import org.sharad.velvetinvestment.data.remote.model.onboarding.Finance
import org.sharad.velvetinvestment.data.remote.model.onboarding.Insurance
import org.sharad.velvetinvestment.data.remote.model.onboarding.Loan
import org.sharad.velvetinvestment.data.remote.model.onboarding.OnBoardingBodyDto
import org.sharad.velvetinvestment.data.remote.model.onboarding.Profile
import org.sharad.velvetinvestment.domain.models.goals.GoalRequest
import org.sharad.velvetinvestment.presentation.onboarding.compose.personaldetails.NextButtonFooter
import org.sharad.velvetinvestment.presentation.onboarding.models.AssetFlowDetails
import org.sharad.velvetinvestment.presentation.onboarding.models.FinancialFlowDetails
import org.sharad.velvetinvestment.presentation.onboarding.models.LoanInfo
import org.sharad.velvetinvestment.presentation.onboarding.models.PersonalDetails
import org.sharad.velvetinvestment.presentation.onboarding.viewmodel.OnBoardingConfirmationViewModel
import org.sharad.velvetinvestment.utils.AppBackHandler
import org.sharad.velvetinvestment.utils.DateTimeUtils.epochToYYYYMMdd
import org.sharad.velvetinvestment.utils.formatMoneyWithUnits
import org.sharad.velvetinvestment.utils.genericDropShadow
import org.sharad.velvetinvestment.utils.theme.titlesStyle

@Composable
fun OnBoardingConfirmationScreen(
    name: String,
    city: String,
    annualIncome: Long,
    retirementYear: Int,

    monthlyIncome: Long,
    housingExpense: Long,
    otherExpenses: Long,
    emiExpense: Long,
    netSurplus: Long,

    totalAssets: Long,
    totalLiabilities: Long,
    netWorth: Long,

    additionalLoans: List<LoanInfo>,

    termLifeCover: Long,
    totalInsuranceLiabilities: Long,

    goals: List<GoalRequest>,
    pv: PaddingValues,
    onPrev: () -> Boolean,
    onNext: () -> Unit,
    personalDetails: PersonalDetails,
    healthInsurance: Long,
    financiaData: FinancialFlowDetails,
    assetsInfo: AssetFlowDetails
) {

    val viewModel: OnBoardingConfirmationViewModel= koinViewModel()
    val loading by viewModel.loading.collectAsStateWithLifecycle()

    AppBackHandler(true){
        onPrev()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ){
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
                .background(Color(0xFFF6F7FB))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        )
        {

            item {
                SectionCard(title = "Personal Details") {
                    KeyValueRow("Name", name)
                    KeyValueRow("City", city)
                    KeyValueRow("Annual Income", "₹${formatMoneyWithUnits(annualIncome)}")
                    KeyValueRow("Retirement Year", retirementYear.toString())
                }
            }

            item {
                SectionCard(title = "Monthly Cash Flow") {
                    KeyValueRow("Monthly Income", "₹${formatMoneyWithUnits(monthlyIncome)}")
                    KeyValueRow(
                        "Housing Expense",
                        "-₹${formatMoneyWithUnits(housingExpense)}",
                        Color.Red
                    )
                    KeyValueRow(
                        "Other Expenses",
                        "-₹${formatMoneyWithUnits(otherExpenses)}",
                        Color.Red
                    )
                    KeyValueRow(
                        "EMI Payments",
                        "-₹${formatMoneyWithUnits(emiExpense)}",
                        Color.Red
                    )
                    HorizontalDivider()
                    KeyValueRow(
                        "Net Surplus",
                        "₹${formatMoneyWithUnits(netSurplus)}",
                        Secondary
                    )
                }
            }

            item {
                SectionCard(title = "Financial Overview") {
                    KeyValueRow(
                        "Total Assets",
                        "₹${formatMoneyWithUnits(totalAssets)}"
                    )
                    KeyValueRow(
                        "Total Liabilities",
                        "₹${formatMoneyWithUnits(totalLiabilities)}",
                        Color.Red
                    )
                    HorizontalDivider()
                    KeyValueRow(
                        "Net Worth",
                        "₹${formatMoneyWithUnits(netWorth)}",
                        Secondary
                    )
                }
            }

            if (additionalLoans.isNotEmpty()) {
                item {
                    SectionCard(title = "Additional Loans (${additionalLoans.size})") {
                        additionalLoans.forEach {
                            KeyValueRowWithContext(
                                label = it.loanType.displayName,
                                value = "₹${formatMoneyWithUnits(it.outstandingAmount)}",
                                context = "EMI: ₹${formatMoneyWithUnits(it.monthlyEmi)}"
                            )
                        }
                    }
                }
            }

            item {
                SectionCard(title = "Insurance Coverage") {
                    KeyValueRow(
                        "Term Life",
                        "₹${formatMoneyWithUnits(termLifeCover)}"
                    )
                    KeyValueRow(
                        "Total Liabilities",
                        "₹${formatMoneyWithUnits(totalInsuranceLiabilities)}"
                    )
                }
            }

            if (goals.isNotEmpty()) {
                item {
                    SectionCard(title = "Goals (${goals.size})") {
                        goals.forEach {
//                            KeyValueRow(
//                                label = it.,
//                                value = it.?.toString() ?: "-",
//                                valueColor = Secondary
//                            )
                        }
                    }
                }
            }
            item {
                Spacer(
                    modifier = Modifier.height(16.dp)
                )
            }
        }

        NextButtonFooter(
            onClick = {
                viewModel.onboardUser(
                    OnBoardingBodyDto(assets = Assets(
                        cash_saving = assetsInfo.cash?:0L,
                        fd =  assetsInfo.fixedDeposits?:0L,
                        gold = assetsInfo.goldAndCommodities?:0L,
                        mutual_funds =  assetsInfo.mutualFunds?:0L,
                        real_estate =  assetsInfo.realEstate?:0L,
                        stocks =  assetsInfo.stocksAndShares?:0L
                    ),
                    finance = Finance(
                        annual_income = financiaData.annualIncome?:0L,
                        expense_food = financiaData.foodExpense?:0L,
                        expense_house = financiaData.houseExpense?:0L,
                        expense_others = financiaData.otherExpense?:0L,
                        expense_transportation = financiaData.transportExpense?:0L
                    ),
                    goals = emptyList(),
                    insurance = Insurance(
                        health_insurance = healthInsurance,
                        life_insurance = termLifeCover
                    ),
                    loans = additionalLoans.map {it->
                        Loan(
                            loan_name = it.loanType.code,
                            loan_type = it.loanType.name,
                            monthly_emi = it.monthlyEmi,
                            outstanding_amount = it.outstandingAmount,
                            tenure_months = it.tenure
                        )
                    },
                    profile = Profile(
                        city = personalDetails.city,
                        dob = epochToYYYYMMdd(personalDetails.dob),
                        full_name = personalDetails.fullName
                    ))
                ){
                    onNext()
                }
            },
            pv=pv,
            value = "Confirm"
        )

    }
}

@Composable
fun KeyValueRowWithContext(label: String, value: String,context:String,valueColor: Color = bgColor1) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = Primary, style = MaterialTheme.typography.labelLarge)
        Column(
            horizontalAlignment = Alignment.End
        ){
            Text(value, color = valueColor, style = MaterialTheme.typography.labelLarge)
            Text(text = context, style = titlesStyle, color = titleColor)
        }
    }
}

@Composable
private fun SectionCard(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .genericDropShadow(RoundedCornerShape(15.dp))
            .clip(RoundedCornerShape(15.dp))
            .background(Color.White)
            .padding(horizontal = 20.dp, vertical = 32.dp),
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            color = Color.Black
        )
        Spacer(Modifier.height(16.dp))
        Column(
            modifier=Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ){
            content() }
    }
}

@Composable
private fun KeyValueRow(
    label: String,
    value: String,
    valueColor: Color = bgColor1
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = Primary, style = MaterialTheme.typography.labelLarge)
        Text(value, color = valueColor,  style = MaterialTheme.typography.labelLarge)
    }
}
