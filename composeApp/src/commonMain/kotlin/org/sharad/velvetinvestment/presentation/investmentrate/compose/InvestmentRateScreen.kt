package org.sharad.velvetinvestment.presentation.investmentrate.compose

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import org.sharad.emify.core.ui.theme.LightGreen
import org.sharad.emify.core.ui.theme.Primary
import org.sharad.emify.core.ui.theme.Secondary
import org.sharad.emify.core.ui.theme.darkBlue
import org.sharad.emify.core.ui.theme.TextGray
import org.sharad.emify.core.ui.theme.healthColor
import org.sharad.emify.core.ui.theme.titleColor
import org.sharad.velvetinvestment.domain.models.user.InvestmentRateDomain
import org.sharad.velvetinvestment.domain.models.user.SavingTrendsDomain
import org.sharad.velvetinvestment.domain.models.user.SpendingCategoriesDomain
import org.sharad.velvetinvestment.domain.models.user.SpendingChartData
import org.sharad.velvetinvestment.presentation.investmentrate.viewmodel.InvestmentRateScreenViewModel
import org.sharad.velvetinvestment.shared.PieChart
import org.sharad.velvetinvestment.shared.PieChartEntry
import org.sharad.velvetinvestment.shared.UiStateContainer
import org.sharad.velvetinvestment.shared.compose.BackHeader
import org.sharad.velvetinvestment.utils.formatMoneyWithUnits
import org.sharad.velvetinvestment.utils.formatWithCommas
import org.sharad.velvetinvestment.shared.theme.Poppins
import org.sharad.velvetinvestment.shared.theme.VelvetTheme
import org.sharad.velvetinvestment.shared.theme.titlesStyle
import org.sharad.velvetinvestment.utils.trimTo


@Composable
fun InvestmentRateScreen(
    onBackClick: () -> Unit
) {
    val viewModel: InvestmentRateScreenViewModel = koinViewModel()
    val state by viewModel.investmentRateState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.White,
        topBar = {
            BackHeader(
                heading = "Saving pattern",
                showBack = true,
                onBackClick = onBackClick
            )
        }
    ) {pv->
        UiStateContainer(
            uiState = state,
            onRetry = { viewModel.loadInvestmentRate() },
            modifier = Modifier.padding(pv)
        ) { data ->
            InvestmentRateContent(data = data)
        }
    }
}

@Composable
fun InvestmentRateContent(data: InvestmentRateDomain) {
    val animationProgress = remember { Animatable(0f) }

    LaunchedEffect(data.trends) {
        animationProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 1000,
                easing = FastOutSlowInEasing
            )
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        item {
            AverageSavingsSummaryCard(data = data)
        }
        item {
            SavingVsInvestingSection(
                trends = data.trends,
                animationProgress = { animationProgress.value }
            )
        }
        item {
            SpendingCategoriesSection(
                categories = data.spendingCategories,
                animationProgress = { animationProgress.value }
            )
        }
    }
}

@Composable
fun AverageSavingsSummaryCard(data: InvestmentRateDomain) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Primary)
    ) {
        Column(
            modifier = Modifier.padding(28.dp)
        ) {
            Text(
                text = "Average Savings Patterns",
                color = Color.White,
                fontFamily = Poppins,
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "${data.currentSavingPercentage.trimTo(1)}%",
                    color = Color.White,
                    fontSize = 36.sp,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "${if (data.percentDelta >= 0) "+" else ""}${data.percentDelta}%",
                    color = LightGreen,
                    fontSize = 16.sp,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 6.dp)
                )
            }
            Text(
                text = "You saved ₹${formatWithCommas(data.savingDelta)} more than last month.",
                color = Color.White,
                lineHeight = 20.sp,
                fontFamily = Poppins,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun SavingVsInvestingSection(
    trends: List<SavingTrendsDomain>,
    animationProgress: () -> Float
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Saving vs Investing",
                    fontFamily = Poppins,
                    style = MaterialTheme.typography.labelLarge
                )
                Text(
                    text = "Last 6 months trend",
                    color = titleColor,
                    style = titlesStyle
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                LegendItem(color = darkBlue, label = "Saving")
                Spacer(modifier = Modifier.width(12.dp))
                LegendItem(color = Secondary, label = "Investing")
            }
        }

        SavingVsInvestingBarChart(
            trends = trends,
            animationProgress = animationProgress,
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .padding(horizontal = 16.dp)
        )
    }
}



@Composable
fun SpendingCategoriesSection(
    categories: SpendingCategoriesDomain,
    animationProgress: () -> Float = { 1f }
) {
    Column {
        Text(
            text = "Spending Categories",
            style = MaterialTheme.typography.labelLarge
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            Box(
                modifier = Modifier.size(150.dp),
                contentAlignment = Alignment.Center
            ) {
                PieChart(
                    data = listOf(
                        PieChartEntry(value = categories.investments.percent.toFloat(), color = Primary),
                        PieChartEntry(value = categories.essentials.percent.toFloat(), color = Secondary),
                        PieChartEntry(value = categories.savings.percent.toFloat(), color = healthColor.copy(0.8f))
                    ),
                    modifier = Modifier.size(120.dp),
                    strokeWidth = 60f,
                    animationProgress = animationProgress
                )
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Total", fontSize = 12.sp, color = TextGray, fontFamily = Poppins)
                    Text(
                        text = "₹${formatMoneyWithUnits(categories.investments.amount + categories.essentials.amount + categories.savings.amount)}",
                        fontSize = 16.sp,
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Bold,
                        color = Primary
                    )
                }
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SpendingLegendItem(color = Primary, label = "Investments", percent = categories.investments.percent)
                SpendingLegendItem(color = Secondary, label = "Essentials", percent = categories.essentials.percent)
                SpendingLegendItem(color = healthColor, label = "Lifestyle", percent = categories.savings.percent)
            }
        }
    }
}

@Composable
fun LegendItem(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = label, fontSize = 12.sp, color = Primary, fontFamily = Poppins)
    }
}

@Composable
fun SpendingLegendItem(color: Color, label: String, percent: Double) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(color)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = label, fontSize = 14.sp, color = Primary, fontFamily = Poppins)
        }
        Text(
            text = "${percent.toInt()}%",
            fontSize = 14.sp,
            fontFamily = Poppins,
            fontWeight = FontWeight.Bold,
            color = Primary
        )
    }
}


@Preview
@Composable
fun InvestmentRateScreenPreview() {
    val mockData = InvestmentRateDomain(
        currentSavingPercentage = 45.0,
        previousMonthSavingPercentage = 42.7,
        percentDelta = 2.3,
        savingDelta = 145000,
        trends = listOf(
            SavingTrendsDomain("Jan", 4000, 3000),
            SavingTrendsDomain("Feb", 6000, 4500),
            SavingTrendsDomain("Mar", 7000, 4000),
            SavingTrendsDomain("Apr", 12000, 7000),
            SavingTrendsDomain("May", 3000, 2500),
            SavingTrendsDomain("Jun", 9000, 8000)
        ),
        spendingCategories = SpendingCategoriesDomain(
            savings = SpendingChartData(30000, 30.0),
            investments = SpendingChartData(50000, 50.0),
            essentials = SpendingChartData(20000, 20.0)
        )
    )

    VelvetTheme {
        Surface(color = Color.White) {
            InvestmentRateContent(data = mockData)
        }
    }
}
