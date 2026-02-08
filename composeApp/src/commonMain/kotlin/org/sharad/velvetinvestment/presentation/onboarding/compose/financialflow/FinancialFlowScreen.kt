package org.sharad.velvetinvestment.presentation.onboarding.compose.financialflow

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import org.sharad.emify.core.ui.theme.bgColor2
import org.sharad.emify.core.ui.theme.bgColor3
import org.sharad.emify.core.ui.theme.bgColor1
import org.sharad.emify.core.ui.theme.foodExpenseColor
import org.sharad.emify.core.ui.theme.homeGoalColor
import org.sharad.emify.core.ui.theme.othersExpenseColor
import org.sharad.emify.core.ui.theme.titleColor
import org.sharad.emify.core.ui.theme.travelExpenseColor
import org.sharad.velvetinvestment.presentation.onboarding.models.ExpensePercentages
import org.sharad.velvetinvestment.presentation.onboarding.models.FinancialFlowDetails
import org.sharad.velvetinvestment.presentation.onboarding.models.FinancialSummary
import org.sharad.velvetinvestment.presentation.onboarding.viewmodel.FinancialFlowScreenViewModel
import org.sharad.velvetinvestment.shared.compose.ContinueBackButtonFooter
import org.sharad.velvetinvestment.utils.AppBackHandler
import org.sharad.velvetinvestment.utils.theme.subHeadingMedium
import org.sharad.velvetinvestment.utils.theme.titlesStyle
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.icon_food
import velvet.composeapp.generated.resources.icon_house
import velvet.composeapp.generated.resources.icon_others
import velvet.composeapp.generated.resources.icon_transport

@Composable
fun FinancialFlowScreen(
    modifier: Modifier = Modifier,
    pv: PaddingValues,
    onNext: () -> Unit,
    onPrev: () -> Unit
) {

    AppBackHandler(true){
        onPrev()
    }

    val viewModel: FinancialFlowScreenViewModel= koinViewModel()
    val financialInfo by viewModel.financialInfo.collectAsStateWithLifecycle()
    val expensePercent by viewModel.expensePercentages.collectAsStateWithLifecycle()
    val financialSummary by viewModel.financialSummary.collectAsStateWithLifecycle()

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        )
        {

            item {
                GenericInfoHeader(
                    heading = "Your Financial Flow",
                    subHeading = "Understanding your income and expenses helps us create a personalized investment plan"
                )
            }

            item {
                IncomeEntry(
                    annualIncome=financialInfo.annualIncome,
                    onAnnualIncomeUpdate = {viewModel.onAnnualIncomeUpdate(it)},
                )
            }
            
            item { 
                MonthlyExpenses(
                    financialInfo = financialInfo,
                    expensePercent=expensePercent,
                    onHouseExpenseUpdate = { viewModel.onHouseExpenseUpdate(it) },
                    onFoodExpenseUpdate = { viewModel.onFoodExpenseUpdate(it) },
                    onTransportExpenseUpdate = { viewModel.onTransportExpenseUpdate(it) },
                    onOtherExpenseUpdate = { viewModel.onOtherExpenseUpdate(it) }
                )
            }

            item{
                Summary(
                    summary = financialSummary
                )
            }

            item {
                Spacer(
                    modifier = Modifier.height(80.dp+pv.calculateBottomPadding())
                )
            }

        }
        ContinueBackButtonFooter(
            onContinue = onNext,
            onBack = onPrev,
            pv = pv
        )
    }

}

@Composable
fun Summary(summary: FinancialSummary) {
    Box(
        modifier = Modifier.fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(bgColor3.copy(alpha = 0.1f), RoundedCornerShape(10.dp)),
        contentAlignment = Alignment.Center
    ){
        Column(
            modifier = Modifier.fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ){
            Text(
                text="Financial Summary",
                style = MaterialTheme.typography.headlineSmall
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Total Expenses",
                        style = MaterialTheme.typography.headlineSmall,
                        color = Primary,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = summary.totalExpense,
                        style = MaterialTheme.typography.headlineSmall,
                        color = Secondary,
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Monthly Surplus",
                        style = MaterialTheme.typography.headlineSmall,
                        color = Primary,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = summary.monthlySurplus,
                        style = MaterialTheme.typography.headlineSmall,
                        color = Secondary,
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Savings Rate",
                        style = MaterialTheme.typography.headlineSmall,
                        color = Primary,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = summary.savingsRate,
                        style = MaterialTheme.typography.headlineSmall,
                        color = Secondary,
                    )
                }
            }
        }
    }
}

@Composable
fun MonthlyExpenses(
    financialInfo: FinancialFlowDetails,
    onHouseExpenseUpdate: (String) -> Unit,
    onFoodExpenseUpdate: (String) -> Unit,
    onTransportExpenseUpdate: (String) -> Unit,
    onOtherExpenseUpdate: (String) -> Unit,
    expensePercent: ExpensePercentages
) {
    Column(
        modifier=Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Monthly Expenses",
            style = MaterialTheme.typography.headlineLarge,
            color = Primary
        )

        ExpandableExpenseEntryField(
            heading = "House & Utilities",
            subHeading = "Rent/EMI, electricity, water, internet",
            amount = financialInfo.houseExpense,
            percentage = expensePercent.housePercent.toInt(),
            accentColor = homeGoalColor,
            icon = Res.drawable.icon_house,
            onValueChange = onHouseExpenseUpdate
        )
        ExpandableExpenseEntryField(
            heading = "Food & Groceries",
            subHeading = "Food & Groceries",
            amount = financialInfo.foodExpense,
            percentage = expensePercent.foodPercent.toInt(),
            accentColor = foodExpenseColor,
            icon = Res.drawable.icon_food,
            onValueChange = onFoodExpenseUpdate
        )
        ExpandableExpenseEntryField(
            heading = "Transportation",
            subHeading = "Fuel, public transport, cab rides",
            amount = financialInfo.transportExpense,
            percentage = expensePercent.transportPercent.toInt(),
            accentColor = travelExpenseColor,
            icon = Res.drawable.icon_transport,
            onValueChange = onTransportExpenseUpdate
        )
        ExpandableExpenseEntryField(
            heading = "Others",
            subHeading = "Shopping, entertainment, healthcare",
            amount = financialInfo.otherExpense,
            percentage = expensePercent.otherPercent.toInt(),
            accentColor = othersExpenseColor,
            icon = Res.drawable.icon_others,
            onValueChange = onOtherExpenseUpdate
        )

    }
}

@Composable
fun IncomeEntry(annualIncome: Long?, onAnnualIncomeUpdate: (String) -> Unit) {
    Column(
        modifier=Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        MoneyTextField(
            value = annualIncome?.toString() ?: "",
            modifier = Modifier.fillMaxWidth(),
            onValueChange = onAnnualIncomeUpdate,
            placeHolder = "",
            label = "Current Annual Income (Post Taxes)",
            mandatory = false
        )
        Box(
            modifier=Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(bgColor2.copy(alpha = 0.1f), RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center
        ){
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(vertical = 24.dp, horizontal = 16.dp)
            ){
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Text(
                        text = "Monthly Income",
                        style = subHeadingMedium,
                        color = bgColor1,
                    )

                    Text(
                        text = annualIncome?.let { "₹${it/12}" } ?: "₹0",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Primary
                    )
                }
            }
        }

        HorizontalDivider(
            modifier=Modifier.fillMaxWidth(),
            thickness = 1.dp,
            color = Color(0xffDEE2F6BF).copy(0.75f)
        )
    }
}

@Composable
fun GenericInfoHeader(
    heading:String,
    subHeading:String
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text=heading,
            style = MaterialTheme.typography.headlineLarge,
            color = Primary,
        )
        Text(
            text=subHeading,
            style = titlesStyle,
            color = titleColor
        )
    }
}