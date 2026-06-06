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

object AboutVelvetTextStyle {

    @Composable
    fun screenTitle() =
        MaterialTheme.typography.headlineMedium.copy(fontSize = 24.sp, fontWeight = FontWeight.SemiBold)

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
fun AboutVelvetScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {}
) {
    Scaffold(
        modifier = modifier,
        containerColor = Color.White,
        topBar = {
            BackHeader(
                heading = "About Velvet",
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
                    text = "ABOUT VELVET INVESTING",
                    style = AboutVelvetTextStyle.screenTitle()
                )
                Text(
                    text = "Investing With Purpose. Planning With Clarity.",
                    style = AboutVelvetTextStyle.subHeading(),
                    color = Color.Gray
                )
            }

            AboutBodyText(
                "Velvet Investing is a modern wealth management platform designed to help individuals make smarter financial decisions and build long-term wealth with confidence."
            )

            AboutBodyText(
                "We combine goal-based financial planning, data-driven insights, and curated investment solutions to help investors move beyond chasing returns and focus on achieving meaningful financial outcomes."
            )

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Our mission is simple:",
                    style = AboutVelvetTextStyle.bodyTitle()
                )
                AboutBodyText(
                    "Help every investor understand where they stand today, where they want to go, and how to get there."
                )
            }

            AboutVelvetSection(title = "Our Philosophy") {
                Text(
                    text = "Successful investing is not about timing the market.",
                    style = AboutVelvetTextStyle.bodyTitle()
                )
                AboutBodyText(
                    "It is about creating a structured financial plan, staying disciplined through market cycles, and making decisions that align with long-term goals."
                )
                AboutBodyText(
                    "At Velvet Investing, we believe wealth creation should be intentional, measurable, and aligned with the life you want to build."
                )
            }

            AboutVelvetSection(title = "What Makes Velvet Different") {
                Text(
                    text = "Financial Planning Before Product Selection",
                    style = AboutVelvetTextStyle.bodyTitle()
                )
                AboutBodyText(
                    "Most investment platforms begin with products. We begin with you."
                )
                AboutBodyText(
                    "Before recommending investments, Velvet analyzes your financial profile, goals, liabilities, savings habits, and risk appetite to create a personalized financial roadmap."
                )
                AboutBodyText(
                    "Because the right investment depends on the right objective."
                )
            }

            AboutVelvetSection(title = "The Velvet F.I.R.E. Framework") {
                AboutBodyText(
                    "Our proprietary F.I.R.E. (Financial Independence & Retire Early) Framework helps investors understand their complete financial picture."
                )
                AboutBodyText(
                    "Through the Velvet F.I.R.E. Report, investors can monitor:"
                )
                BulletList(
                    items = listOf(
                        "Net Worth Growth",
                        "Savings Efficiency",
                        "Debt Management",
                        "Goal Progress",
                        "Retirement Readiness",
                        "Insurance Adequacy",
                        "Financial Independence Progress"
                    )
                )
                AboutBodyText(
                    "This transforms investing from a product decision into a long-term wealth-building strategy."
                )
            }

            AboutVelvetSection(title = "Goal-Based Wealth Creation") {
                Text(
                    text = "Every investment should have a purpose.",
                    style = AboutVelvetTextStyle.bodyTitle()
                )
                AboutBodyText(
                    "Whether your objective is:"
                )
                BulletList(
                    items = listOf(
                        "Retirement Planning",
                        "Building Long-Term Wealth",
                        "Purchasing a Home",
                        "Funding Education",
                        "Creating an Emergency Reserve",
                        "Achieving Financial Independence"
                    )
                )
                AboutBodyText(
                    "Velvet helps align your investments with your financial goals and timelines."
                )
            }

            AboutVelvetSection(title = "Curated Investment Portfolios") {
                AboutBodyText(
                    "Investors often struggle to select funds that match their risk profile and objectives."
                )
                AboutBodyText(
                    "Velvet simplifies this process through professionally curated portfolios designed around different investment goals, risk preferences, and time horizons."
                )
                AboutBodyText(
                    "Each portfolio is structured to promote diversification, consistency, and long-term wealth creation."
                )
            }

            AboutVelvetSection(title = "What We Offer") {
                OfferItem(
                    title = "Mutual Fund Investments",
                    description = "Access a carefully curated universe of mutual funds designed to support long-term financial goals through disciplined investing."
                )
                OfferItem(
                    title = "Fixed Deposits",
                    description = "Explore fixed deposit opportunities from trusted financial institutions to balance stability alongside growth-oriented investments."
                )
                OfferItem(
                    title = "Goal Planning",
                    description = "Create, track, and monitor financial goals with personalized projections and progress tracking."
                )
                OfferItem(
                    title = "Wealth Health Reports",
                    description = "Receive periodic assessments of your financial position, highlighting strengths, risks, and opportunities for improvement."
                )
                OfferItem(
                    title = "Velvet F.I.R.E. Reports",
                    description = "Gain access to a comprehensive financial freedom assessment designed to measure and accelerate your journey toward financial independence."
                )
            }

            AboutVelvetSection(title = "Our Approach to Wealth Management") {
                AboutBodyText(
                    "We believe successful wealth creation is built on five core principles:"
                )
                ApproachItem(
                    title = "Clarity",
                    description = "Understanding your financial position before making investment decisions."
                )
                ApproachItem(
                    title = "Discipline",
                    description = "Maintaining consistency regardless of short-term market fluctuations."
                )
                ApproachItem(
                    title = "Diversification",
                    description = "Managing risk through balanced asset allocation."
                )
                ApproachItem(
                    title = "Protection",
                    description = "Safeguarding wealth through adequate insurance and contingency planning."
                )
                ApproachItem(
                    title = "Long-Term Thinking",
                    description = "Focusing on sustainable wealth creation rather than short-term market noise."
                )
            }

            AboutVelvetSection(title = "Security & Trust") {
                AboutBodyText(
                    "Protecting your financial information is fundamental to everything we do."
                )
                AboutBodyText(
                    "Velvet Investing follows industry-standard security practices and works with regulated financial partners to provide a secure and reliable investing experience."
                )
                AboutBodyText(
                    "Your financial data remains confidential, protected, and accessible only to you."
                )
            }

            AboutVelvetSection(title = "Our Vision") {
                AboutBodyText(
                    "To become India’s most trusted platform for financial planning and goal-based wealth creation."
                )
                AboutBodyText(
                    "A future where every individual has access to professional-quality financial insights, understands their financial trajectory, and can confidently work towards financial independence."
                )
            }

            AboutVelvetSection(title = "The Velvet Promise") {
                AboutBodyText("We cannot control markets.")
                AboutBodyText("We cannot predict short-term outcomes.")
                AboutBodyText(
                    "What we can provide is a structured framework, intelligent insights, and disciplined guidance to help you make better financial decisions over time."
                )
                AboutBodyText("Because lasting wealth is not built through speculation.")
                AboutBodyText("It is built through planning, consistency, and informed action.")
                AboutBodyText("Save More. Invest Better.")
                AboutBodyText("Achieve Financial Freedom Earlier.")
                AboutBodyText("Velvet Investing — Your Partner in Building Long-Term Wealth.")
            }
        }
    }
}

@Composable
private fun AboutVelvetSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = title,
            style = AboutVelvetTextStyle.screenTitle()
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
        style = AboutVelvetTextStyle.body(),
        color = Color.Black
    )
}

@Composable
private fun OfferItem(
    title: String,
    description: String
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = title,
            style = AboutVelvetTextStyle.bodyTitle()
        )
        AboutBodyText(description)
    }
}

@Composable
private fun ApproachItem(
    title: String,
    description: String
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = title,
            style = AboutVelvetTextStyle.bodyTitle()
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
                    style = AboutVelvetTextStyle.bullet()
                )
                Text(
                    text = item,
                    style = AboutVelvetTextStyle.bullet(),
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AboutVelvetPreview() {
    VelvetTheme {
        AboutVelvetScreen()
    }
}
