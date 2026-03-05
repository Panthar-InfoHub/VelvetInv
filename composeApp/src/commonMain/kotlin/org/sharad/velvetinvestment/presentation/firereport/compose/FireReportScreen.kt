package org.sharad.velvetinvestment.presentation.firereport.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import org.sharad.emify.core.ui.theme.Primary
import org.sharad.emify.core.ui.theme.Secondary
import org.sharad.emify.core.ui.theme.appGreen
import org.sharad.emify.core.ui.theme.appRed
import org.sharad.emify.core.ui.theme.bgColor1
import org.sharad.emify.core.ui.theme.bgColor3
import org.sharad.emify.core.ui.theme.bgColor4
import org.sharad.emify.core.ui.theme.darkBlue
import org.sharad.emify.core.ui.theme.foodExpenseColor
import org.sharad.emify.core.ui.theme.titleColor
import org.sharad.velvetinvestment.presentation.firereport.uimodels.FireCombinedUIState
import org.sharad.velvetinvestment.presentation.firereport.uimodels.TargetProjectionUi
import org.sharad.velvetinvestment.presentation.firereport.viewmodel.FireReportViewModel
import org.sharad.velvetinvestment.shared.compose.BackHeader
import org.sharad.velvetinvestment.shared.compose.BarHeader
import org.sharad.velvetinvestment.shared.compose.ErrorScreen
import org.sharad.velvetinvestment.shared.compose.LoaderScreen
import org.sharad.velvetinvestment.shared.compose.PercentageCircle
import org.sharad.velvetinvestment.shared.compose.ShadowCard
import org.sharad.velvetinvestment.shared.compose.ToggleSwitch
import org.sharad.velvetinvestment.utils.UiState
import org.sharad.velvetinvestment.utils.formatMoneyAfterL
import org.sharad.velvetinvestment.utils.formatMoneyWithUnits
import org.sharad.velvetinvestment.utils.theme.Poppins
import org.sharad.velvetinvestment.utils.theme.largeTextStyle
import org.sharad.velvetinvestment.utils.theme.subHeading
import org.sharad.velvetinvestment.utils.theme.titlesStyle
import org.sharad.velvetinvestment.utils.trimTo
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.dollar_icon
import velvet.composeapp.generated.resources.fire_icon
import velvet.composeapp.generated.resources.fireprogress
import velvet.composeapp.generated.resources.hugeicons_date_time
import velvet.composeapp.generated.resources.icon_filter
import velvet.composeapp.generated.resources.nav_icon_full_screener

@Composable
fun FireReportScreen(
    onBack: () -> Unit
){

    val viewModel: FireReportViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val emiIncluded by viewModel.emiIncluded.collectAsStateWithLifecycle()


    Column(
        modifier = Modifier.fillMaxSize(),
    ){

        BackHeader(heading = "F.I.R.E Report", showBack = true, onBackClick = onBack,)
        Box(
            modifier=Modifier.weight(1f).fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            when(uiState){
                is UiState.Error -> {
                    ErrorScreen(errorMessage = (uiState as UiState.Error).message, onRetryClick = {
                        viewModel.loadData()
                    })
                }
                UiState.Loading -> {
                    LoaderScreen()
                }
                is UiState.Success -> {
                    FireReportContent(
                        uiState =(uiState as UiState.Success<FireCombinedUIState>).data,
                        emiIncluded =emiIncluded,
                        onEmiSwitchClick = {
                            viewModel.toggleEmi()
                        }
                    )
                }
            }
        }
    }

}

@Composable
fun FireReportContent(
    uiState: FireCombinedUIState,
    emiIncluded: Boolean,
    onEmiSwitchClick: () -> Unit,) {
    val activeState = remember(uiState, emiIncluded) {
        if (emiIncluded) uiState.emiIncluded else uiState.emiExcluded
    }
    LazyColumn(
        modifier=Modifier.fillMaxSize().padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        contentPadding = PaddingValues(top = 24.dp)
    ) {
        item{
            FireReportHeadSwitcher(
                emiIncluded = emiIncluded,
                onEmiIncludedClick = onEmiSwitchClick
            )
        }
        item {
            FireNumberCard(
                fireNumber =activeState.fireNumber,
                annualGrowth =activeState.fireNumberAnnualGrowth
            )
        }
        item {
            FireProgressCard(
                progressPercentage =activeState.fireProgress,
                annualGrowth =activeState.fireProgressThisYear)
        }
        item {
            FireYearCard(
                fireYears =activeState.yearsToFire,
                percentage =activeState.yearsToFirePercentage
            )
        }
        item {
            FireSummaryCard(
                fireProgress= activeState.fireProgress,
                fireNumber=activeState.fireNumber,
                netWorth=activeState.currentNetWorth,
                yearsToFire=activeState.yearsToFire,
                insights=activeState.fireInsights
            )
        }
        item {
            BarHeader(
                heading = "30 Years Projections"
            )
        }
        items(items = activeState.projectionList, key = {it.year}){
            ProjectionCard(it)
        }
        item {
            TargetInfo(target=activeState.targetProjection)
        }
        item {
            Spacer(
                modifier = Modifier.height(20.dp)
            )
        }
    }

}

@Composable
fun TargetInfo(target: TargetProjectionUi) {
    Column(
        modifier = Modifier.fillMaxWidth(),
    ){
        HorizontalDivider(color = titleColor)

        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Target Year",
                    style = titlesStyle,
                    color = Color(0xff6A7282)
                )
                Text(
                    text = target.targetYear.toString(),
                    style = MaterialTheme.typography.labelLarge,
                    color = Primary
                )
            }
            Column {
                Text(
                    text = "Projected Portfolio",
                    style = titlesStyle,
                    color = Color(0xff6A7282)
                )
                Text(
                    text = "₹${formatMoneyWithUnits(target.projectedPortfolio)}",
                    style = MaterialTheme.typography.labelLarge,
                    color = appGreen
                )
            }
            Column {
                Text(
                    text = "FIRE Progress",
                    style = titlesStyle,
                    color = Color(0xff6A7282)
                )
                Text(
                    text = target.fireProgress.toInt().toString(),
                    style = MaterialTheme.typography.labelLarge,
                    color = Primary
                )
            }
        }
    }
}

@Composable
fun FireSummaryCard(
    fireProgress: Double,
    netWorth: Long,
    fireNumber: Long,
    yearsToFire: Int,
    insights: List<String>
) {
    ShadowCard{
        Column(
            modifier=Modifier.fillMaxWidth().padding(vertical = 44.dp, horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProgressIndicatorCard(fireProgress=fireProgress)
            FireInfoSection(fireNumber=fireNumber, netWorth=netWorth, yearsToFire=yearsToFire)
            InsightSection(insights=insights)
        }
    }
}

@Composable
fun InsightSection(insights: List<String>) {
    val colorList=listOf(bgColor1, foodExpenseColor,bgColor3)
    ShadowCard {
        Column(
            modifier = Modifier.fillMaxWidth().padding(vertical = 20.dp, horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "F.I.R.E Insights",
                style = subHeading,
                color = titleColor
            )

            Column {
                insights.forEachIndexed { idx,insight->
                    InsightPoint(
                        text=insight,
                        pointColor=colorList[idx%3]
                    )
                }
            }

        }
    }

}

@Composable
fun InsightPoint(text: String, pointColor: Color) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier.padding(8.dp)
                .size(7.dp)
                .clip(CircleShape)
                .background(pointColor)
        )
        Text(
            text=text,
            fontFamily = Poppins,
            fontSize = 14.sp,
            color = Color.Black
        )

    }
}

@Composable
fun FireInfoSection(fireNumber: Long, netWorth: Long, yearsToFire: Int) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        FireInfoCard(
            icon=Res.drawable.fire_icon,
            heading="F.I.R.E Number",
            subHeading="Target Corpus",
            value= formatMoneyWithUnits(fireNumber),
            color=appGreen
        )
        FireInfoCard(
            icon = Res.drawable.dollar_icon,
            heading = "Current Net Worth",
            subHeading = "Assets – Liabilities",
            value = formatMoneyWithUnits(netWorth),
            color = bgColor4
        )

        FireInfoCard(
            icon = Res.drawable.hugeicons_date_time,
            heading = "Years to FIRE",
            subHeading = "At current rate",
            value = yearsToFire.toString(),
            color = bgColor3
        )

    }
}

@Composable
private fun FireInfoCard(
    icon: DrawableResource,
    heading: String,
    subHeading: String,
    value: String,
    color: Color
) {

    ShadowCard(
        backgroundColor = color.copy(alpha = 0.1f)
    ) {
        Row(
            modifier=Modifier.fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ){

            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(36.dp)
            )

            Column(
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    text = heading,
                    style = MaterialTheme.typography.labelSmall,
                    color=Color.Black
                )
                Text(
                    text = subHeading,
                    style = titlesStyle,
                    color=color
                )
            }

            Text(
                text = value,
                style = MaterialTheme.typography.headlineLarge,
                color=color
            )

        }
    }

}

@Composable
fun ProgressIndicatorCard(fireProgress: Double) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        PercentageCircle(fireProgress.toFloat()/100, modifier = Modifier.size(140.dp))
        Text(
            text = "F.I.R.E Progress",
            style = MaterialTheme.typography.headlineSmall
        )
    }

}

@Composable
fun FireNumberCard(fireNumber: Long, annualGrowth: Double) {
    Box(
        modifier = Modifier.fillMaxWidth()
            .height(216.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(bgColor4.copy(0.1f))
    ){
        Icon(
            painter = painterResource(Res.drawable.fire_icon),
            contentDescription = null,
            tint = Primary.copy(0.1f),
            modifier = Modifier.align(Alignment.BottomEnd).padding(12.dp).size(148.dp)
        )
        Column(
            modifier = Modifier.fillMaxWidth().padding(vertical = 28.dp, horizontal = 24.dp),
        ) {
            Text(
                text = "Current F.I.R.E Number",
                style = MaterialTheme.typography.headlineSmall,
                color = titleColor
            )
            Spacer(Modifier.height(24.dp))
            Text(
                text = "₹ ${formatMoneyAfterL(fireNumber)}",
                style = largeTextStyle,
                color = Primary
            )
            Spacer(Modifier.height(16.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
               Icon(
                   painter=painterResource(Res.drawable.nav_icon_full_screener),
                   contentDescription = null,
                   tint = appGreen,
                   modifier = Modifier.height(10.dp)
               )
                Text(
                    text = if (annualGrowth>=0) "  +$annualGrowth%" else "  $annualGrowth%",
                    style = titlesStyle,
                    color = if (annualGrowth<=0) appRed else appGreen
                )
                Text(
                    text = " annual growth",
                    style = titlesStyle,
                    color = Color.Black
                )
            }
        }
    }
}

@Composable
fun FireProgressCard(progressPercentage: Double, annualGrowth: Double) {
    Box(
        modifier = Modifier.fillMaxWidth()
            .height(216.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(bgColor1.copy(0.1f))
    ){
        Icon(
            painter = painterResource(Res.drawable.fireprogress),
            contentDescription = null,
            tint = bgColor1,
            modifier = Modifier.align(Alignment.CenterEnd).padding(12.dp).size(148.dp)
        )
        Column(
            modifier = Modifier.fillMaxWidth().padding(vertical = 28.dp, horizontal = 24.dp),
        ) {
            Text(
                text = "F.I.R.E Progress",
                style = MaterialTheme.typography.headlineSmall,
                color = titleColor
            )
            Spacer(Modifier.height(24.dp))
            Text(
                text = "$progressPercentage%",
                style = largeTextStyle,
                color = appGreen
            )
            Spacer(Modifier.height(16.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
               Icon(
                   painter=painterResource(Res.drawable.nav_icon_full_screener),
                   contentDescription = null,
                   tint = appGreen,
                   modifier = Modifier.height(10.dp)
               )
                Text(
                    text = if (annualGrowth>=0) "  +$annualGrowth%" else "  $annualGrowth%",
                    style = titlesStyle,
                    color = if (annualGrowth<=0) appRed else appGreen
                )
                Text(
                    text = " this year",
                    style = titlesStyle,
                    color = Color.Black
                )
            }
        }
    }
}
@Composable
fun FireYearCard(fireYears: Int, percentage: Double) {
    Box(
        modifier = Modifier.fillMaxWidth()
            .height(216.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(bgColor3.copy(0.1f))
    ){
        Icon(
            painter = painterResource(Res.drawable.hugeicons_date_time),
            contentDescription = null,
            tint = Secondary,
            modifier = Modifier.align(Alignment.CenterEnd).padding(12.dp).size(148.dp)
        )
        Column(
            modifier = Modifier.fillMaxWidth().padding(vertical = 28.dp, horizontal = 24.dp),
        ) {
            Text(
                text = "Year to F.I.R.E",
                style = MaterialTheme.typography.headlineSmall,
                color = titleColor
            )
            Spacer(Modifier.height(24.dp))
            Text(
                text = "$fireYears Years",
                style = largeTextStyle,
                color = Secondary
            )
            Spacer(Modifier.height(16.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
               Icon(
                   painter=painterResource(Res.drawable.nav_icon_full_screener),
                   contentDescription = null,
                   tint = if (percentage>=0) appGreen else appRed,
                   modifier = Modifier.height(10.dp)
               )
                Text(
                    text = if (percentage>=0) "  +$percentage%" else "  $percentage%",
                    style = titlesStyle,
                    color = if (percentage<=0) appRed else appGreen
                )
                Text(
                    text = " accelerated",
                    style = titlesStyle,
                    color = Color.Black
                )
            }
        }
    }
}


@Composable
fun FireReportHeadSwitcher(onEmiIncludedClick: () -> Unit, emiIncluded: Boolean) {
    ShadowCard {
        Column(
            modifier=Modifier.fillMaxWidth()
        ) {
            TopHalf()
            BottomHalf(emiIncluded,onEmiIncludedClick)
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun BottomHalf(emiIncluded: Boolean, onEmiIncludedClick: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxWidth()
            .background(Secondary.copy(0.1f))
    ){
        Row(
            modifier=Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Include EMI in Outflow:",
                style = titlesStyle,
                color = Primary

            )
            ToggleSwitch(
                checked = emiIncluded,
                onCheckedChange = { onEmiIncludedClick() },
                width = 64.dp,
                height = 28.dp,
                thumbSize = 22.dp,
                checkedTrackColor = Primary,
                uncheckedTrackColor = Color.White,
                checkedThumbColor = Color.White,
                uncheckedThumbColor = Primary
            )
        }
    }
}

@Composable
fun TopHalf() {
    Row(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier.size(44.dp).clip(RoundedCornerShape(15.dp))
                .background(darkBlue.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(Res.drawable.fire_icon),
                contentDescription = null,
                tint = darkBlue,
                modifier = Modifier.size(24.dp)
            )
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {

            Text(
                text = "F.I.R.E Report",
                style = subHeading,
                color = Primary
            )

            Text(
                text="Financial Independence Retire Early",
                style = titlesStyle,
                color = Primary
            )

        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable(
                onClick = {},
                indication = null,
                interactionSource = remember{ MutableInteractionSource() }
            )
        ) {
            Icon(
                painter = painterResource(Res.drawable.icon_filter),
                contentDescription = null,
                tint = Secondary,
                modifier = Modifier.size(19.dp)
            )
            Text(
                text="Filter",
                fontFamily = Poppins,
                fontSize = 14.sp,
                color = Secondary
            )
        }
    }
}