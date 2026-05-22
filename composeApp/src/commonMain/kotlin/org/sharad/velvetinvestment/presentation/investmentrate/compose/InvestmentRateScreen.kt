package org.sharad.velvetinvestment.presentation.investmentrate.compose

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import org.sharad.emify.core.ui.theme.LightGreen
import org.sharad.emify.core.ui.theme.Primary
import org.sharad.emify.core.ui.theme.Secondary
import org.sharad.emify.core.ui.theme.darkBlue
import org.sharad.emify.core.ui.theme.TextGray
import org.sharad.emify.core.ui.theme.healthColor
import org.sharad.emify.core.ui.theme.titleColor
import org.sharad.velvetinvestment.domain.models.user.EssentialsBreakdownDomain
import org.sharad.velvetinvestment.domain.models.user.EssentialsChartData
import org.sharad.velvetinvestment.domain.models.user.InvestmentBreakdownDomain
import org.sharad.velvetinvestment.domain.models.user.InvestmentChartData
import org.sharad.velvetinvestment.domain.models.user.InvestmentRateDomain
import org.sharad.velvetinvestment.domain.models.user.SavingTrendsDomain
import org.sharad.velvetinvestment.domain.models.user.SpendingCategoriesDomain
import org.sharad.velvetinvestment.domain.models.user.SpendingChartData
import org.sharad.velvetinvestment.presentation.investmentrate.viewmodel.InvestmentRateScreenViewModel
import org.sharad.velvetinvestment.shared.PieChart
import org.sharad.velvetinvestment.shared.PieChartEntry
import org.sharad.velvetinvestment.shared.UiStateContainer
import org.sharad.velvetinvestment.shared.compose.BackHeader
import org.sharad.velvetinvestment.shared.theme.LocalVelvetShapes
import org.sharad.velvetinvestment.utils.formatMoneyWithUnits
import org.sharad.velvetinvestment.utils.formatWithCommas
import org.sharad.velvetinvestment.shared.theme.Poppins
import org.sharad.velvetinvestment.shared.theme.VelvetTheme
import org.sharad.velvetinvestment.shared.theme.titlesStyle
import org.sharad.velvetinvestment.utils.trimTo
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.arrow_down
import velvet.composeapp.generated.resources.fd_icon
import velvet.composeapp.generated.resources.ic_home
import velvet.composeapp.generated.resources.icon_food
import velvet.composeapp.generated.resources.icon_house
import velvet.composeapp.generated.resources.icon_others
import velvet.composeapp.generated.resources.icon_transport
import velvet.composeapp.generated.resources.nav_icon_full_screener
import velvet.composeapp.generated.resources.nav_icon_home


@Composable
fun InvestmentRateScreen(
    onBackClick: () -> Unit
) {
    val viewModel: InvestmentRateScreenViewModel = koinViewModel()
    val state by viewModel.investmentRateState.collectAsStateWithLifecycle()
    val investmentBreakdownExpanded by viewModel.investmentBreakdownExpanded.collectAsStateWithLifecycle()
    val essentialsBreakdownExpanded by viewModel.essentialsBreakdownExpanded.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color(0xffF8F9FB),
        topBar = {
            BackHeader(
                heading = "Saving pattern",
                showBack = true,
                onBackClick = onBackClick,
                modifier = Modifier.fillMaxWidth().background(Color.White)
            )
        }
    ) {pv->
        UiStateContainer(
            uiState = state,
            onRetry = { viewModel.loadInvestmentRate() },
            modifier = Modifier.padding(pv)
        ) { data ->
            InvestmentRateContent(
                data = data,
                toggleInvestmentBreakdown = { viewModel.toggleInvestmentBreakdown() },
                toggleEssentialsBreakdown = { viewModel.toggleEssentialsBreakdown() },
                investmentBreakdownExpanded = investmentBreakdownExpanded,
                essentialsBreakdownExpanded = essentialsBreakdownExpanded
            )
        }
    }
}

@Composable
fun InvestmentRateContent(
    data: InvestmentRateDomain,
    toggleInvestmentBreakdown: () -> Unit,
    toggleEssentialsBreakdown: () -> Unit,
    essentialsBreakdownExpanded: Boolean,
    investmentBreakdownExpanded: Boolean
) {
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
        verticalArrangement = Arrangement.spacedBy(20.dp),
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
        item {
            Text(
                text = "Spending Breakdown",
                style = MaterialTheme.typography.headlineSmall,
                color = darkBlue
            )
        }
        item {
            CategoryBreakdownSection(
                investmentBreakdown= data.spendingCategories.investments.breakdown,
                essentialsBreakdown= data.spendingCategories.essentials.breakdown,
                toggleInvestmentBreakdown = toggleInvestmentBreakdown,
                toggleEssentialsBreakdown = toggleEssentialsBreakdown,
                investementExpended = investmentBreakdownExpanded,
                essentialsExpended = essentialsBreakdownExpanded
            )
        }

    }
}

@Composable
fun CategoryBreakdownSection(
    investmentBreakdown: InvestmentBreakdownDomain,
    essentialsBreakdown: EssentialsBreakdownDomain,
    toggleEssentialsBreakdown: () -> Unit,
    toggleInvestmentBreakdown: () -> Unit,
    essentialsExpended: Boolean,
    investementExpended: Boolean
) {
    val borderColor= Color(0xffE1E2E4)
    Column(
        modifier = Modifier.fillMaxWidth()
            .clip(LocalVelvetShapes.current.roundedDp20)
            .border(1.dp, borderColor, LocalVelvetShapes.current.roundedDp20)
            .background(Color.White)
            .animateContentSize()
    ) {
        BreakdownHeader(
            icon = Res.drawable.nav_icon_full_screener,
            iconTint = darkBlue,
            title = "Investments",
            backgroundColor = Color(0xffDBE1FF).copy(alpha = 0.3f),
            expended= investementExpended,
            onClick = toggleInvestmentBreakdown
        )
        if(investementExpended){ InvestmentBreakDownSection(data = investmentBreakdown) }

        HorizontalDivider(color = borderColor)

        BreakdownHeader(
            icon = Res.drawable.nav_icon_home,
            iconTint = Color(0xff76613D),
            title = "Essentials",
            backgroundColor = Color(0xffFCDFB1).copy(alpha = 0.5f),
            expended= essentialsExpended,
            onClick = toggleEssentialsBreakdown
        )

        if(essentialsExpended){ EssentialsBreakDownSection(data = essentialsBreakdown) }
    }
}

@Composable
private fun EssentialsBreakDownSection(data: EssentialsBreakdownDomain) {
    Column(
        modifier = Modifier.fillMaxWidth()
            .padding(bottom = 24.dp, top = 4.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        EntryRow(
            icon= Res.drawable.icon_house,
            title= "House",
            percent= data.house.percent,
            amount = data.house.amount
        )
        EntryRow(
            icon= Res.drawable.icon_food,
            title= "Fixed Deposits",
            percent= data.food.percent,
            amount = data.food.amount
        )
        EntryRow(
            icon= Res.drawable.icon_transport,
            title= "Transportation",
            percent= data.transportation.percent,
            amount = data.transportation.amount
        )
        EntryRow(
            icon= Res.drawable.icon_others,
            title= "Others",
            percent= data.others.percent,
            amount = data.others.amount
        )
    }
}

@Composable
private fun InvestmentBreakDownSection(data: InvestmentBreakdownDomain) {
    Column(
        modifier = Modifier.fillMaxWidth()
            .padding(bottom = 24.dp, top = 4.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        EntryRow(
            icon= Res.drawable.nav_icon_full_screener,
            title= "Mutual Funds",
            percent= data.mutualFunds.percent,
            amount = data.mutualFunds.amount
        )
        EntryRow(
            icon= Res.drawable.fd_icon,
            title= "Fixed Deposits",
            percent= data.fixedDeposits.percent,
            amount = data.fixedDeposits.amount
        )
    }
}

@Composable
private fun EntryRow(
    icon: DrawableResource,
    title: String,
    percent: Double,
    amount: Long
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 28.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = Color(0xff757680),
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = title,
            color = titleColor,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = "${percent.toInt()}%",
            fontWeight = FontWeight.SemiBold,
            color = Primary,
            style = MaterialTheme.typography.bodySmall,
        )
//        text = formatMoneyWithUnits(amount)+ " (${percent.toInt()}%)",
    }
}

@Composable
private fun BreakdownHeader(
    icon: DrawableResource,
    title: String,
    iconTint: Color,
    backgroundColor: Color,
    expended: Boolean,
    onClick: () -> Unit,
) {
    val rotationAngle = animateFloatAsState(
        targetValue = if (expended) 180f else 0f,
        animationSpec = tween(
            durationMillis = 300,
            easing = FastOutSlowInEasing
        )
    )
    Row(
        modifier = Modifier
            .clickable(onClick=onClick)
            .fillMaxWidth()
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier.size(40.dp)
                .clip(CircleShape)
                .background(backgroundColor),
            contentAlignment = Alignment.Center
        ){
            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.fillMaxSize()
                    .padding(10.dp)
            )
        }
        Text(
            text = title,
            color = iconTint,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.weight(1f)
        )
        Icon(
            painter = painterResource(Res.drawable.arrow_down),
            contentDescription = null,
            tint = iconTint,
            modifier = Modifier
                .size(12.dp)
                .graphicsLayer {
                    rotationZ= rotationAngle.value
                }
        )
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
        modifier = Modifier
            .clip(LocalVelvetShapes.current.roundedDp20)
            .border(1.dp, Color(0xffE1E2E4), LocalVelvetShapes.current.roundedDp20)
            .background(Color.White)
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Saving vs Investing",
                    fontFamily = Poppins,
                    style = MaterialTheme.typography.headlineSmall,
                    color = darkBlue
                )
                Text(
                    text = "Last 6 months trend",
                    color = titleColor,
                    style = titlesStyle
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(start = 16.dp)) {
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
    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        Text(
            text = "Spending Categories",
            style = MaterialTheme.typography.headlineSmall,
            color = darkBlue
        )

        Row(
            modifier = Modifier.fillMaxWidth()
                .clip(LocalVelvetShapes.current.roundedDp20)
                .border(1.dp, Color(0xffE1E2E4), LocalVelvetShapes.current.roundedDp20)
                .background(Color.White)
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            Box(
                modifier = Modifier.size(130.dp),
                contentAlignment = Alignment.Center
            ) {
                PieChart(
                    data = listOf(
                        PieChartEntry(value = categories.investments.percent.toFloat(), color = Primary),
                        PieChartEntry(value = categories.essentials.percent.toFloat(), color = Secondary),
                        PieChartEntry(value = categories.savings.percent.toFloat(), color = healthColor.copy(0.8f))
                    ),
                    modifier = Modifier.size(110.dp),
                    strokeWidth = 50f,
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
                SpendingLegendItem(color = healthColor, label = "Savings", percent = categories.savings.percent)
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
            fontWeight = FontWeight.SemiBold,
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
        trends = listOf(SavingTrendsDomain(
                monthText = "Jan 26",
                savings = 4000,
                investments = 3000,
            ), SavingTrendsDomain(
                monthText = "Feb 26",
                savings = 6000,
                investments = 4500,
            ), SavingTrendsDomain(
                monthText = "Mar 26",
                savings = 7000,
                investments = 4000,
            ), SavingTrendsDomain(
                monthText = "Apr 26",
                savings = 12000,
                investments = 7000,
            ), SavingTrendsDomain(
                monthText = "May 26",
                savings = 3000,
                investments = 2500,
            ), SavingTrendsDomain(
                monthText = "Jun 26",
                savings = 9000,
                investments = 8000,
            ),),
        spendingCategories = SpendingCategoriesDomain(
            savings = SpendingChartData(
                amount = 30000,
                percent = 30.0,
            ),
            investments = InvestmentChartData(
                amount = 50000,
                percent = 50.0,
                breakdown = InvestmentBreakdownDomain(
                    mutualFunds = SpendingChartData(
                        amount = 35000,
                        percent = 70.0,
                    ),
                    fixedDeposits = SpendingChartData(
                        amount = 15000,
                        percent = 30.0,
                    ),
                ),
            ),
            essentials = EssentialsChartData(
                amount = 20000,
                percent = 20.0,
                breakdown = EssentialsBreakdownDomain(
                    house = SpendingChartData(
                        amount = 7000,
                        percent = 35.0,
                    ),
                    food = SpendingChartData(
                        amount = 5000,
                        percent = 25.0,
                    ),
                    transportation = SpendingChartData(
                        amount = 4000,
                        percent = 20.0,
                    ),
                    others = SpendingChartData(
                        amount = 4000,
                        percent = 20.0,
                    ),
                ),
            ),
        ),
    )

    VelvetTheme {
        Surface(
            color = Color(0xffF8F9FB),
        ) {
            InvestmentRateContent(
                data = mockData, {}, {},false,false,
            )
        }
    }
}
