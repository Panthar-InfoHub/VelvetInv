package org.sharad.velvetinvestment.presentation.profile.compose.ProfileNew.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.sharad.velvetinvestment.shared.compose.BackHeader
import org.sharad.velvetinvestment.shared.theme.VelvetTheme

object AboutUsTextStyle {

    @Composable
    fun screenTitle() =
        MaterialTheme.typography.headlineMedium.copy(fontSize = 22.sp, fontWeight = FontWeight.SemiBold)

    @Composable
    fun sectionTitle() =
        MaterialTheme.typography.labelLarge

    @Composable
    fun subHeading() =
        MaterialTheme.typography.bodySmall.copy(
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp,
            lineHeight = 20.sp
        )

    @Composable
    fun bodyTitle() =
        MaterialTheme.typography.bodySmall.copy(
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            lineHeight = 20.sp
        )

    @Composable
    fun body() =
        MaterialTheme.typography.bodySmall.copy(
            fontSize = 14.sp,
            lineHeight = 20.sp
        )

    @Composable
    fun bullet() =
        MaterialTheme.typography.bodySmall.copy(
            fontSize = 14.sp,
            lineHeight = 20.sp
        )
}

@Composable
fun AboutUsScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {}
) {

    Scaffold(
        modifier = modifier,
        containerColor = Color.White,
        topBar = {
            BackHeader(
                heading = "About Us",
                showBack = true,
                onBackClick = onBack
            )
        }
    ) { paddingValues ->
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(paddingValues)
                    .padding(
                        start = 20.dp,
                        end = 20.dp,
                        top = 12.dp,
                        bottom =32.dp
                    ),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {


                AboutUsSection(
                    title = "1. About Velvet Investing",
                    content = {
                        AboutBodyText(
                            "Velvet Investing is a personal finance and investment platform built to help users understand their complete financial life — not just their portfolio."
                        )

                        AboutBodyText(
                            "The app brings together your income, expenses, assets, liabilities, insurance, investments, and financial goals to generate a clear picture of your current financial health."
                        )

                        AboutBodyText(
                            "Velvet helps users move from random investing to structured, goal-based financial planning by converting financial data into measurable insights."
                        )
                    }
                )

                AboutUsSection(
                    title = "2. Our Core USP",
                    content = {

                        AboutBodyText(
                            "Our core USP is the Velvet F.I.R.E. Report."
                        )

                        AboutBodyText(
                            "F.I.R.E. stands for Financial Independence, Retire Early."
                        )


                        AboutBodyText(
                            "The Velvet F.I.R.E. Report helps users understand:"
                        )

                        BulletList(
                            items = listOf(
                                "Where they stand financially today",
                                "How much wealth they need for financial independence",
                                "How far they are from their F.I.R.E. number",
                                "How much they need to save and invest for future goals",
                                "Whether their insurance coverage is enough",
                                "When they may achieve financial independence"
                            )
                        )

                        AboutBodyText(
                            "The report calculates a user’s F.I.R.E. Number, F.I.R.E. Progress, Net Worth, Savings Rate, Goal Readiness, Insurance Gap, and projected year of financial independence. In the sample report, the F.I.R.E. Number is calculated using annual expenses and a 30x multiplier based on the 4% safe withdrawal rule."
                        )
                    }
                )

                AboutUsSection(
                    title = "3. What We Calculate",
                    content = {

                        AboutBodyText(
                            "Velvet Investing uses structured financial calculations to help users understand their financial health in a simple way."
                        )

                        CalculationBlock(
                            title = "Net Worth",
                            whatItMeans = "Net Worth shows the user’s real financial position after comparing what they own and what they owe.",
                            howCalculated = "Net Worth = Total Assets − Total Liabilities\n\nAssets include mutual funds, stocks, fixed deposits, gold, cash, savings, and other owned resources.\n\nLiabilities include loans, EMIs, and other financial obligations. In the F.I.R.E. Report, assets and liabilities are used to calculate the user’s net worth."
                        )

                        CalculationBlock(
                            title = "F.I.R.E. Number",
                            whatItMeans = "The F.I.R.E. Number is the estimated wealth required to become financially independent and maintain the current lifestyle without active income.",
                            howCalculated = "F.I.R.E. Number = Annual Expenses × 30\n\nThis is based on the 4% safe withdrawal rule, where the user can withdraw approximately 4% of the portfolio annually. In the sample report, the F.I.R.E. Number is shown as Annual Expenses × 30."
                        )

                        CalculationBlock(
                            title = "F.I.R.E. Progress",
                            whatItMeans = "F.I.R.E. Progress shows how close the user is to achieving financial independence.",
                            howCalculated = "F.I.R.E. Progress % = Current Portfolio Value ÷ F.I.R.E. Number × 100\n\nThis tells users what percentage of their financial independence journey is already complete. The report uses portfolio value and F.I.R.E. Number to calculate F.I.R.E. percentage year by year."
                        )

                        CalculationBlock(
                            title = "F.I.R.E. Gap",
                            whatItMeans = "F.I.R.E. Gap shows how much more wealth the user still needs to accumulate to become financially independent.",
                            howCalculated = "F.I.R.E. Gap = F.I.R.E. Number − Current Portfolio Value\n\nThis helps users understand the exact wealth shortfall between their current financial position and their financial freedom target."
                        )

                        CalculationBlock(
                            title = "Projected F.I. Year",
                            whatItMeans = "Projected F.I. Year estimates the year in which the user may reach financial independence.",
                            howCalculated = "Velvet projects future portfolio growth, annual expenses, goal payouts, and future F.I.R.E. Number year by year.\n\nThe year where F.I.R.E. Progress becomes 100% or more is treated as the projected financial independence year.\n\nIn the sample report, the projected financial independence year is shown as 2050, when the F.I.R.E. percentage crosses 100%."
                        )

                        CalculationBlock(
                            title = "Monthly Surplus",
                            whatItMeans = "Monthly Surplus shows how much money is left after monthly expenses.",
                            howCalculated = "Monthly Surplus = Monthly Income − Monthly Expenses\n\nThis shows the user’s available amount for investing, saving, emergency planning, and goal funding. The report calculates surplus by subtracting expenses from income."
                        )

                        CalculationBlock(
                            title = "Savings Rate",
                            whatItMeans = "Savings Rate shows what percentage of income is being saved or invested.",
                            howCalculated = "Savings Rate = Monthly Surplus ÷ Monthly Income × 100\n\nA higher savings rate usually means faster progress toward goals and financial independence. In the sample report, savings rate is calculated using surplus and income."
                        )

                        CalculationBlock(
                            title = "Asset Allocation",
                            whatItMeans = "Asset Allocation shows how the user’s wealth is distributed across different asset classes.",
                            howCalculated = "Asset Allocation % = Value of Asset Class ÷ Total Assets × 100\n\nVelvet tracks allocation across categories such as equity, debt, gold, cash, savings, and other assets. This helps users understand whether their money is too concentrated or properly diversified. The report shows net worth distribution across financial assets, cash and savings, and gold."
                        )

                        CalculationBlock(
                            title = "Goal Future Value",
                            whatItMeans = "Goal Future Value estimates how much a goal may cost in the future after considering inflation or growth in cost.",
                            howCalculated = "Future Value = Today’s Goal Cost × (1 + Inflation/Growth Rate) ^ Number of Years\n\nFor example, if a goal costs ₹10 lakh today, Velvet projects what it may cost in the target year based on assumed inflation or future value growth. The report uses future value logic for goal planning."
                        )

                        CalculationBlock(
                            title = "Monthly SIP Required",
                            whatItMeans = "Monthly SIP Required shows how much the user needs to invest every month to reach a specific financial goal.",
                            howCalculated = "Velvet uses the goal amount, target year, number of years left, expected return, and inflation assumptions to estimate the required monthly investment.\n\nMonthly SIP = Required monthly investment to reach the future goal value within the available time period\n\nIn the report, goal planning shows monthly SIP required for each goal and the total monthly savings required across all active goals."
                        )

                        CalculationBlock(
                            title = "Year-by-Year Savings Requirement",
                            whatItMeans = "This shows how much the user needs to save every month and every year to stay on track for all active goals.",
                            howCalculated = "Total Monthly Requirement = Sum of Monthly SIPs for all active goals\n\nOnce a goal is completed, its SIP requirement is removed from future years. This gives users a clear year-wise view of their required commitment."
                        )

                        CalculationBlock(
                            title = "Term Life Insurance Gap",
                            whatItMeans = "Term Life Insurance Gap shows whether the user’s current life cover is enough to protect liabilities and family needs.",
                            howCalculated = "Velvet estimates recommended term cover using an age-based income multiplier:\n\nAge 20–30: 30× annual income\nAge 31–40: 20× annual income\nAge 41–50: 15× annual income\nAge 50+: 10× annual income\n\nTerm Insurance Gap = Recommended Cover − Current Cover\n\nThe report uses this age-based multiplier method to calculate recommended term life coverage."
                        )

                        CalculationBlock(
                            title = "Health Insurance Gap",
                            whatItMeans = "Health Insurance Gap shows whether the user’s current health cover is sufficient.",
                            howCalculated = "Velvet uses age-based minimum health cover recommendations:\n\nAge 20–30: ₹10 lakh\nAge 31–40: ₹15 lakh\nAge 41–50: ₹25 lakh\nAge 51+: ₹40 lakh\n\nHealth Insurance Gap = Recommended Health Cover − Current Health Cover\n\nThe report compares current health coverage with the recommended amount and highlights any shortfall."
                        )
                    }
                )

                AboutUsSection(
                    title = "4. Why It Matters",
                    content = {

                        AboutBodyText(
                            "Most users invest without knowing whether their current money habits are enough to support their future life."
                        )

                        AboutBodyText(
                            "Velvet Investing helps users answer important questions like:"
                        )

                        BulletList(
                            items = listOf(
                                "What is my real net worth today?",
                                "Am I saving enough every month?",
                                "How much wealth do I need for financial independence?",
                                "How far am I from my F.I.R.E. number?",
                                "How much should I invest monthly for each goal?",
                                "When can I become financially independent?",
                                "Is my insurance coverage enough?",
                                "Which financial gap should I fix first?"
                            )
                        )

                        AboutBodyText(
                            "These insights help users take better financial actions instead of making investment decisions blindly."
                        )
                    }
                )

                AboutUsSection(
                    title = "5. Our Mission",
                    content = {

                        AboutBodyText(
                            "Our mission is to make financial planning simple, measurable, and actionable for Indian investors."
                        )

                        AboutBodyText(
                            "Velvet Investing is designed to help users understand their financial health, plan their goals, protect their family, and move closer to financial freedom."
                        )

                        AboutBodyText(
                            "We believe investing should not start with a product."
                        )

                        AboutBodyText(
                            "It should start with a clear understanding of your income, expenses, assets, liabilities, goals, insurance needs, and your personal F.I.R.E. number."
                        )

                        AboutBodyText(
                            "Velvet helps users save more, invest better, and achieve financial freedom earlier."
                        )
                    }
                )
            }
    }
}
@Composable
private fun AboutUsSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text(
            text = title,
            style = AboutUsTextStyle.sectionTitle()
        )

        content()
    }
}
@Composable
private fun AboutBodyText(
    text: String
) {

    Text(
        text = text,
        style = AboutUsTextStyle.body(),
        color = Color.Black
    )
}

@Composable
private fun CalculationBlock(
    title: String,
    whatItMeans: String,
    howCalculated: String
) {

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text(
            text = title,
            style = AboutUsTextStyle.sectionTitle()
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Text(
                text = "What it means:",
                style = AboutUsTextStyle.bodyTitle()
            )

            Text(
                text = whatItMeans,
                style = AboutUsTextStyle.body()
            )
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Text(
                text = "How it is calculated:",
                style = AboutUsTextStyle.bodyTitle()
            )

            Text(
                text = howCalculated,
                style = AboutUsTextStyle.body()
            )
        }
    }
}

@Composable
private fun BulletList(
    items: List<String>,
) {

    Column(
        verticalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier.padding(start = 16.dp)
    ) {

        items.forEachIndexed { index, item ->

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.Top
            ) {

                Text(
                    text = "•",
                    style = AboutUsTextStyle.bullet()
                )

                Text(
                    text = item,
                    style = AboutUsTextStyle.bullet(),
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AboutUsPreview() {
    VelvetTheme {
        AboutUsScreen()
    }
}
