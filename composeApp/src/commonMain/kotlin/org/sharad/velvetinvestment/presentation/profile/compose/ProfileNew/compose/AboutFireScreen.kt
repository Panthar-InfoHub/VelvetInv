package org.sharad.velvetinvestment.presentation.profile.compose.ProfileNew.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
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

object AboutFireTextStyle {

    @Composable
    fun screenTitle() =
        MaterialTheme.typography.headlineMedium.copy(fontSize = 24.sp, fontWeight = FontWeight.SemiBold)

    @Composable
    fun sectionTitle() =
        MaterialTheme.typography.labelLarge.copy(fontSize = 16.sp, fontWeight = FontWeight.Bold)

    @Composable
    fun subHeading() =
        MaterialTheme.typography.bodySmall.copy(
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
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
            lineHeight = 22.sp
        )

    @Composable
    fun bullet() =
        MaterialTheme.typography.bodySmall.copy(
            fontSize = 14.sp,
            lineHeight = 22.sp
        )
}

@Composable
fun AboutFireScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {}
) {
    Scaffold(
        modifier = modifier,
        containerColor = Color.White,
        topBar = {
            BackHeader(
                heading = "About F.I.R.E. Report",
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
                    start = 24.dp,
                    end = 24.dp,
                    top = 12.dp,
                    bottom = 32.dp
                ),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "ABOUT THE VELVET F.I.R.E. REPORT",
                    style = AboutFireTextStyle.screenTitle()
                )
                Text(
                    text = "Your Financial Freedom Blueprint",
                    style = AboutFireTextStyle.subHeading(),
                    color = Color.Gray
                )
            }

            AboutBodyText(
                "The Velvet F.I.R.E. Report is a comprehensive financial health assessment designed to help you understand where you stand today and what it will take to achieve long-term financial independence."
            )

            AboutBodyText(
                "Built using your income, expenses, assets, liabilities, investments, insurance coverage, and financial goals, the report transforms complex financial information into clear, actionable insights."
            )

            AboutBodyText(
                "Rather than focusing solely on investment returns, the F.I.R.E. Report evaluates your overall financial position and provides a structured roadmap towards financial security, wealth creation, and financial freedom."
            )

            AboutFireSection(title = "Understanding F.I.R.E.") {
                AboutBodyText(
                    "F.I.R.E. (Financial Independence, Retire Early) is a financial planning philosophy focused on building sufficient wealth to support your desired lifestyle without depending entirely on active income."
                )
                AboutBodyText(
                    "At Velvet Investing, we view F.I.R.E. not as early retirement, but as achieving the freedom to make life decisions with greater confidence, flexibility, and financial security."
                )
            }

            AboutFireSection(title = "What Your F.I.R.E. Report Includes") {
                FireItem(
                    title = "Net Worth Analysis",
                    description = "Your Net Worth represents the foundation of your financial health. It is calculated as the total value of your assets minus your liabilities."
                )
                
                Text(text = "The report evaluates:", style = AboutFireTextStyle.bodyTitle())
                BulletList(
                    items = listOf(
                        "Investments and Savings",
                        "Fixed Deposits",
                        "Equity Holdings",
                        "Retirement Assets",
                        "Real Estate Holdings",
                        "Outstanding Loans and Debt"
                    )
                )
                AboutBodyText("A growing net worth is one of the strongest indicators of long-term wealth creation.")

                Spacer(modifier = Modifier.height(8.dp))

                FireItem(
                    title = "Savings Rate",
                    description = "Your Savings Rate measures how efficiently you convert income into future wealth. The report calculates the percentage of your income that is being saved and invested after accounting for expenses."
                )
                AboutBodyText("A higher savings rate generally improves wealth accumulation, accelerates goal achievement, and shortens the journey towards financial independence.")

                Spacer(modifier = Modifier.height(8.dp))

                FireItem(
                    title = "Debt-to-Income Ratio (DTI)",
                    description = "The Debt-to-Income Ratio measures the proportion of your monthly income allocated towards debt obligations."
                )
                Text(text = "The report analyzes:", style = AboutFireTextStyle.bodyTitle())
                BulletList(
                    items = listOf(
                        "Home Loan EMIs",
                        "Personal Loan EMIs",
                        "Vehicle Loans",
                        "Credit Card Obligations"
                    )
                )
                AboutBodyText("Maintaining a healthy DTI ratio improves financial flexibility and allows a greater portion of income to be directed towards investments and long-term goals.")

                Spacer(modifier = Modifier.height(8.dp))

                FireItem(
                    title = "Financial Independence Score",
                    description = "The Financial Independence Score is Velvet Investing’s proprietary indicator of overall financial progress."
                )
                Text(text = "This score combines multiple aspects of your financial profile, including:", style = AboutFireTextStyle.bodyTitle())
                BulletList(
                    items = listOf(
                        "Net Worth",
                        "Savings Behaviour",
                        "Debt Levels",
                        "Investment Assets",
                        "Goal Progress",
                        "Retirement Readiness"
                    )
                )
                AboutBodyText("The score provides a simple way to measure your current position and track progress over time.")
            }

            AboutFireSection(title = "Goal Readiness Assessment") {
                AboutBodyText("Every financial goal requires a dedicated strategy.")
                Text(text = "The report evaluates your preparedness across goals such as:", style = AboutFireTextStyle.bodyTitle())
                BulletList(
                    items = listOf(
                        "Retirement",
                        "Home Ownership",
                        "Children’s Education",
                        "Emergency Planning",
                        "Lifestyle Goals",
                        "Wealth Creation"
                    )
                )
                AboutBodyText("For each goal, the report assesses whether you are currently on track and identifies any funding gaps that may require attention.")
            }

            AboutFireSection(title = "Retirement Readiness Analysis") {
                AboutBodyText("Retirement planning extends beyond building a corpus. The report evaluates whether your current savings, investments, and future contributions are sufficient to support your desired retirement lifestyle.")
                Text(text = "Factors considered include:", style = AboutFireTextStyle.bodyTitle())
                BulletList(
                    items = listOf(
                        "Current Age",
                        "Retirement Timeline",
                        "Future Expenses",
                        "Inflation Impact",
                        "Existing Assets",
                        "Future Investments"
                    )
                )
                AboutBodyText("This analysis helps determine whether your current trajectory is aligned with your long-term retirement objectives.")
            }

            AboutFireSection(title = "Insurance Adequacy Review") {
                AboutBodyText("Financial security is built not only through wealth creation but also through protection. The report reviews your existing insurance coverage and identifies potential gaps across:")
                
                FireItem(
                    title = "Health Insurance",
                    description = "Assessment of whether your existing health coverage is sufficient to protect your family’s financial wellbeing."
                )
                
                FireItem(
                    title = "Term Life Insurance",
                    description = "Evaluation of whether your current life cover adequately protects your dependents and future financial commitments."
                )
            }

            AboutFireSection(title = "Long-Term Wealth Projection") {
                AboutBodyText("The F.I.R.E. Report provides a personalized financial projection based on your current financial profile.")
                Text(text = "Using assumptions around:", style = AboutFireTextStyle.bodyTitle())
                BulletList(
                    items = listOf(
                        "Income Growth",
                        "Investment Contributions",
                        "Inflation",
                        "Expected Returns",
                        "Future Goals"
                    )
                )
                AboutBodyText("the report projects your financial journey year-by-year. This allows you to visualize:")
                BulletList(
                    items = listOf(
                        "Future Net Worth Growth",
                        "Portfolio Expansion",
                        "Goal Achievement Timelines",
                        "Retirement Preparedness",
                        "Progress Towards Financial Independence"
                    )
                )
            }

            AboutFireSection(title = "Personalized Recommendations") {
                AboutBodyText("The report concludes with tailored recommendations designed to strengthen your financial position.")
                Text(text = "These recommendations may include:", style = AboutFireTextStyle.bodyTitle())
                BulletList(
                    items = listOf(
                        "Improving Savings Efficiency",
                        "Optimizing Asset Allocation",
                        "Reducing Debt Burden",
                        "Strengthening Insurance Coverage",
                        "Accelerating Goal Funding",
                        "Enhancing Retirement Preparedness"
                    )
                )
                AboutBodyText("Each recommendation is aligned to your individual financial profile and long-term objectives.")
            }

            AboutFireSection(title = "Why the F.I.R.E. Report Matters") {
                AboutBodyText("Many investors focus only on returns. The most successful wealth creators focus on the complete picture.")
                AboutBodyText("The Velvet F.I.R.E. Report provides a structured framework to measure, monitor, and improve the financial decisions that ultimately determine long-term financial freedom.")
            }

            AboutFireSection(title = "Clarity Today. Confidence Tomorrow. Financial Freedom for Life.") {
                AboutBodyText("This version is much closer to what a premium wealth platform, wealth advisor, PMS, or private wealth firm would publish inside their app. It feels advisory-led rather than educational and positions the F.I.R.E. Report as Velvet Investing’s flagship intellectual property rather than just another financial report.")
            }
        }
    }
}

@Composable
private fun AboutFireSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = title,
            style = AboutFireTextStyle.screenTitle()
        )
        content()
        Spacer(modifier = Modifier.height(4.dp))
        HorizontalDivider(
            color = Color(0xFFEAEAEA)
        )
    }
}

@Composable
private fun AboutBodyText(
    text: String
) {
    Text(
        text = text,
        style = AboutFireTextStyle.body(),
        color = Color.Black
    )
}

@Composable
private fun FireItem(
    title: String,
    description: String
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = title,
            style = AboutFireTextStyle.bodyTitle()
        )
        AboutBodyText(description)
    }
}

@Composable
private fun BulletList(
    items: List<String>,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier.padding(start = 12.dp)
    ) {
        items.forEach { item ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = "•",
                    style = AboutFireTextStyle.bullet()
                )
                Text(
                    text = item,
                    style = AboutFireTextStyle.bullet(),
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AboutFirePreview() {
    VelvetTheme {
        AboutFireScreen()
    }
}
