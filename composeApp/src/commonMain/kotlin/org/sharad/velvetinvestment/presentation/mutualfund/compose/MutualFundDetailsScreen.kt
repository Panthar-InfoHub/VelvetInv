package org.sharad.velvetinvestment.presentation.mutualfund.compose

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import org.sharad.emify.core.ui.theme.Primary
import org.sharad.emify.core.ui.theme.Secondary
import org.sharad.emify.core.ui.theme.appGreen
import org.sharad.emify.core.ui.theme.bgColor3
import org.sharad.emify.core.ui.theme.titleColor
import org.sharad.velvetinvestment.domain.models.mutualfunds.MutualFundDomain
import org.sharad.velvetinvestment.domain.models.mutualfunds.MutualFundGraphPointsDomain
import org.sharad.velvetinvestment.presentation.mutualfund.CalculatorInputState
import org.sharad.velvetinvestment.presentation.mutualfund.DetailsState
import org.sharad.velvetinvestment.presentation.mutualfund.GraphDurationSelection
import org.sharad.velvetinvestment.presentation.mutualfund.GraphState
import org.sharad.velvetinvestment.presentation.mutualfund.StableMetricUi
import org.sharad.velvetinvestment.presentation.mutualfund.viewmodel.MutualFundDetailsScreenViewModel
import org.sharad.velvetinvestment.shared.compose.BarHeader
import org.sharad.velvetinvestment.shared.compose.ContinueBackButtonFooter
import org.sharad.velvetinvestment.shared.compose.ErrorScreen
import org.sharad.velvetinvestment.shared.compose.LoaderScreen
import org.sharad.velvetinvestment.shared.compose.NavLineChart
import org.sharad.velvetinvestment.shared.compose.ShadowCard
import org.sharad.velvetinvestment.shared.compose.VelvetLoader
import org.sharad.velvetinvestment.shared.genericDropShadow
import org.sharad.velvetinvestment.utils.isoUtcToDisplayDate
import org.sharad.velvetinvestment.utils.theme.Poppins
import org.sharad.velvetinvestment.utils.theme.titlesStyle
import org.sharad.velvetinvestment.utils.trimTo
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.back_arrow
import velvet.composeapp.generated.resources.icon_share
import velvet.composeapp.generated.resources.icon_warning

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MutualFundDetailsScreenRoot(
    id:String,
    onBackClick: () -> Unit,
    pv: PaddingValues,
    onTopFundClick: (String) -> Unit,
    onFundClick: (String) -> Unit,
    onMonthlySipClick: (String) -> Unit,
    onOneTimeSipClick: (String) -> Unit
){

    val viewModel = koinViewModel<MutualFundDetailsScreenViewModel> {parametersOf(id)}
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val selectedYear by viewModel.selectedYear.collectAsStateWithLifecycle()
    val bottomSheetVisibility by viewModel.bottomSheetVisibility.collectAsStateWithLifecycle()
    val sheetState= rememberModalBottomSheetState()
    val displayPercent by viewModel.stableMetric.collectAsStateWithLifecycle()

    val calculatorState by viewModel.calculatorInput.collectAsStateWithLifecycle()




    Column(modifier = Modifier.fillMaxSize()) {

        Box(
            modifier = Modifier.fillMaxWidth()
        ){
            Icon(
                painter = painterResource(Res.drawable.back_arrow),
                contentDescription = null,
                modifier = Modifier.padding(top = 16.dp, bottom = 4.dp, start = 16.dp).size(24.dp).clickable(
                    onClick = onBackClick,
                    indication = null,
                    interactionSource = remember{MutableInteractionSource()}
                )
            )
        }

        Box(modifier=Modifier.weight(1f).fillMaxSize()){
            when (uiState.detailsState) {
                is DetailsState.Error -> {
                    ErrorScreen((uiState.detailsState as DetailsState.Error).error)
                }

                DetailsState.Loading -> {
                    LoaderScreen()
                }

                is DetailsState.Success -> {
                    MutualFundDetailsScreen(
                        detailsState = uiState.detailsState as DetailsState.Success,
                        graphState = uiState.graphState,
                        graphPoints=uiState.chartPoints,
                        selectedYear = selectedYear,
                        onSelectedYearChange = viewModel::onSelectedYearChange,
                        pv=pv,
                        onFundTopClick=onTopFundClick,
                        onFundClick = onFundClick,
                        displayPercent=displayPercent,
                        onMonthlySipClick = { onMonthlySipClick(id) },
                        onOneTimeSipClick = {
                            onOneTimeSipClick(id)
                            viewModel.showBottomSheet()
                        },

                        //Calculator
                        calculatorState=calculatorState,
                        onCalcInvestmentChange=viewModel::onInvestmentChange,
                        onCalcSipToggle=viewModel::onSipToggle,
                        onCalcTimeChange=viewModel::onTimeChange
                    )
                }
            }
        }
    }
    if (bottomSheetVisibility){
        KYCPopup(
            sheetState = sheetState,
            onDismiss = {
                viewModel.hideBottomSheet() },
            onClick = {}
        )
    }
}

@Composable
fun MutualFundDetailsScreen(
    detailsState: DetailsState.Success,
    graphState: GraphState,
    selectedYear: GraphDurationSelection,
    onSelectedYearChange: (GraphDurationSelection) -> Unit,
    pv: PaddingValues,
    onFundTopClick: (String) -> Unit,
    onFundClick: (String) -> Unit,
    onMonthlySipClick: () -> Unit,
    onOneTimeSipClick: () -> Unit,
    displayPercent: StableMetricUi?,
    graphPoints: List<MutualFundGraphPointsDomain>,
    calculatorState: CalculatorInputState,
    onCalcInvestmentChange: (Long) -> Unit,
    onCalcSipToggle: (Boolean) -> Unit,
    onCalcTimeChange: (Int) -> Unit
) {

    var calculatorExpanded by remember { mutableStateOf(false) }
    var riskExpanded by remember { mutableStateOf(false) }
    var returnsExpanded by remember { mutableStateOf(false) }
    var isShortTerm by remember { mutableStateOf(true) }
    var overviewsExpanded by remember { mutableStateOf(false) }


    Column(
        modifier = Modifier.fillMaxSize()
    )
    {
        LazyColumn(
            modifier = Modifier.weight(1f).fillMaxSize(),
            contentPadding = PaddingValues(top = 8.dp)
        ) {
            item {
                InfoCard(detailsState=detailsState, displayPercent=displayPercent)
            }
            item { Spacer(Modifier.height(20.dp)) }
            item("graph") {
                val progressAnim = remember { Animatable(0f) }

                LaunchedEffect(selectedYear, graphState) {
                    progressAnim.snapTo(0f)
                    progressAnim.animateTo(
                        targetValue = 1f,
                        animationSpec = tween(
                            durationMillis = 1400,
                            easing = FastOutSlowInEasing
                        )
                    )
                }

                val progress = progressAnim.value
                GraphCard(
                    graphState = graphState,
                    graphPoints = graphPoints,
                    selectedYear = selectedYear,
                    onSelectedYearChange = onSelectedYearChange,
                    chartAnimationProgress=progress
                )
            }
            item {
                FundInfo(
                    date = detailsState.data.latest_nav_date,
                    nav = detailsState.data.latest_nav,
                    assetType = detailsState.data.asset_type,
                    riskLevel = detailsState.data.risk_level,
                    navChange = detailsState.data.metrics.nav_change_pct
                )
            }

            item { Spacer(Modifier.height(12.dp)) }

            item {
                WhatIfCalculator(
                    modifier =Modifier.padding(horizontal = 16.dp),
                    expanded =calculatorExpanded,
                    onExpandToggle = {calculatorExpanded=!calculatorExpanded},
                    input = calculatorState,
                    metrics = detailsState.data.metrics,
                    onModeChange = onCalcSipToggle,
                    onInvestmentChange = onCalcInvestmentChange,
                    onTimeChange = onCalcTimeChange
                )
            }

            item {
                HorizontalDivider(color = titleColor.copy(0.6f), modifier = Modifier.padding(horizontal = 16.dp))
            }

            item {
                RiskSection(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    expanded = riskExpanded,
                    onExpandToggle ={ riskExpanded=!riskExpanded},
                    riskLevel = detailsState.data.risk_level,
                    riskText = detailsState.data.risk_name
                )
            }

            item {
                HorizontalDivider(color = titleColor.copy(0.6f), modifier = Modifier.padding(horizontal = 16.dp))
            }

            item {
                ReturnsSection(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    expanded = returnsExpanded,
                    onExpandToggle = { returnsExpanded=!returnsExpanded},
                    isShortTerm = isShortTerm,
                    onShortTermClick = { isShortTerm=true },
                    onLongTermClick = { isShortTerm=false },
                    metrics = detailsState.data.metrics
                )
            }

            item {
                HorizontalDivider(color = titleColor.copy(0.6f), modifier = Modifier.padding(horizontal = 16.dp))
            }

            item {
                OverviewSection(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    expanded =overviewsExpanded,
                    onExpandToggle = { overviewsExpanded=!overviewsExpanded},
                    data = detailsState.data
                )
            }

            item {
                HorizontalDivider(color = titleColor.copy(0.6f), modifier = Modifier.padding(horizontal = 16.dp))
            }

            item { Spacer(Modifier.height(20.dp)) }

            item {
                InvestmentRiskCard()
            }

            item {
                Spacer(Modifier.height( 20.dp))
            }


//            item {
//                BarHeader(
//                    heading = "Asset Allocation",
//                    modifier = Modifier.padding(horizontal = 16.dp)
//                )
//            }

//            item {
//                AssetAllocationCard(assets = detailsState.data.assets)
//            }
//            item { Spacer(Modifier.height(20.dp)) }
//
//            item {
//                BarHeader(
//                    heading = "Other Top Rated Funds",
//                    modifier = Modifier.padding(horizontal = 16.dp),
//                    showArrow = true,
//                    onArrowClick = { onFundTopClick("topRated") }
//                )
//            }

//            item {
//                TopRatedFunds(
//                    topFunds = detailsState.data.topFunds,
//                    onFundClick = onFundClick
//                )
//            }


        }

        ContinueBackButtonFooter(
            continueText = "Monthly SIP",
            onContinue = {onMonthlySipClick()},
            backText = "One Time",
            onBack = {onOneTimeSipClick()},
            pv = pv
        )

    }
}

@Composable
fun InvestmentRiskCard() {
    ShadowCard(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        backgroundColor = bgColor3.copy(alpha = 0.1f)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(20.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                painter = painterResource(Res.drawable.icon_warning),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint=Secondary
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                Text(
                    text = "Investment Risk",
                    style = titlesStyle.copy(fontWeight = FontWeight.SemiBold),
                    color = Primary
                )

                Text(
                    text = "Mutual fund investments are subject to market risks. Past performance is not indicative of future returns. Please read all scheme related documents carefully before investing.",
                    style = titlesStyle.copy(fontSize = 12.sp),
                    color = titleColor
                )

            }
        }
    }
}

@Composable
fun TopRatedFunds(topFunds: List<MutualFundDomain>, onFundClick: (String) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 150.dp),
        modifier = Modifier.fillMaxWidth().heightIn(max = 600.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 20.dp)
    ){
        items(
            items = topFunds,
            key = { it.id }
        ) { fund ->
            MutualFundGridCard(fund, onClick = { onFundClick(fund.id) })
        }
    }
}

//@Composable
//fun AssetAllocationCard(assets: AssetsAllocationDomain) {
//
//        ShadowCard(
//            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
//        ) {
//            Row(
//                modifier = Modifier.padding(vertical = 32.dp).fillMaxWidth(),
//                verticalAlignment = Alignment.CenterVertically,
//            ) {
//                Box(
//                    modifier = Modifier.weight(0.6f),
//                    contentAlignment = Alignment.Center
//                ) {
//                    PieChart(
//                        modifier = Modifier.size(156.dp),
//                        data = assets.toPieChartEntries()
//                    )
//                }
//                Box(
//                    modifier = Modifier.weight(0.4f),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Column(
//                        modifier = Modifier.fillMaxWidth(),
//                        verticalArrangement = Arrangement.spacedBy(8.dp),
//                        horizontalAlignment = Alignment.Start
//                    ) {
//                        AssetsAllocationInfoRow(
//                            Primary,
//                            "Equity",
//                            assets.equity.trimDoubleTo(1).toString()
//                        )
//                        AssetsAllocationInfoRow(
//                            Secondary,
//                            "Debt",
//                            assets.debt.trimDoubleTo(1).toString()
//                        )
//                        AssetsAllocationInfoRow(
//                            bgColor4,
//                            "Cash",
//                            assets.cash.trimDoubleTo(1).toString()
//                        )
//                    }
//                }
//            }
//        }
//}

//@Composable
//private fun AssetsAllocationInfoRow(
//    color: Color,
//    title: String,
//    value: String
//){
//
//    Row(
//        modifier = Modifier.fillMaxWidth(),
//        verticalAlignment = Alignment.CenterVertically,
//    ) {
//        Box(
//            modifier = Modifier
//                .size(20.dp)
//                .clip(CircleShape)
//                .background(color)
//        )
//        Spacer(modifier = Modifier.width(12.dp))
//        Text(
//            text = title,
//            style = titlesStyle,
//            color=Color.Black
//        )
//        Spacer(Modifier.width(12.dp))
//        Text(
//            text = "$value%",
//            style = titlesStyle,
//            color=Color.Black
//        )
//    }
//
//}

@Composable
fun FundInfo(date: String, nav: String, assetType: String, riskLevel: Int, navChange: Double) {

    LazyVerticalGrid(
        columns = GridCells.Adaptive(160.dp),
        modifier=Modifier.fillMaxWidth().heightIn(max=800.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical =20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ){

        item{
            FundInfoCard(
                title = "NAV: ${date.isoUtcToDisplayDate()}",
            ){
                Text(
                    text= "₹ $nav",
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color.Black
                )
            }
        }
        item{
            FundInfoCard(
                title = "Asset Type",
            ){
                Text(
                    text= assetType,
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color.Black
                )
            }
        }

        item{
            FundInfoCard(
                title = "Fund Size",
            ){
                Text(
                    text= riskLevel.toString(),
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color.Black
                )
            }
        }
        item{
            FundInfoCard(
                title = "Nav Change (%)",
            ){
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ){
                    Text(
                        text = navChange.toString(),
                        style = MaterialTheme.typography.headlineLarge,
                        color = Color.Black
                    )
                }
            }
        }



    }
}

@Composable
fun FundInfoCard(title: String,content: @Composable () -> Unit) {

    ShadowCard(
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier=Modifier.fillMaxWidth().height(82.dp).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ){
            Text(
                text=title,
                style = titlesStyle.copy(fontSize = 12.sp),
                color = titleColor
            )
            content()
        }
    }

}

@Composable
fun GraphCard(
    graphState: GraphState,
    selectedYear: GraphDurationSelection,
    onSelectedYearChange: (GraphDurationSelection) -> Unit,
    chartAnimationProgress: Float,
    graphPoints: List<MutualFundGraphPointsDomain>
) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ){
        Box(
            modifier = Modifier.fillMaxWidth()
                .height(248.dp),
            contentAlignment = Alignment.Center
        ) {
            when (graphState) {
                is GraphState.Error -> {
                    ErrorScreen(graphState.error)
                }

                GraphState.Loading -> {
                    VelvetLoader()
                }

                is GraphState.Success -> {
                    NavLineChart(
                        data = graphPoints,
                        modifier = Modifier.fillMaxSize(),
                        progress = chartAnimationProgress
                    )
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {

            GraphDurationSelection.entries.forEach {item->
                GraphDurationSelectionItemLabel(
                    label = item.label,
                    selected = item == selectedYear,
                    onClick = {
                        onSelectedYearChange(item)
                    }
                )
            }
        }
    }
}

@Composable
private fun GraphDurationSelectionItemLabel(
    label:String,
    selected: Boolean,
    onClick: () -> Unit
) {

    Text(
        text = label,
        style = titlesStyle.copy(fontWeight = FontWeight.Bold),
        color = if (selected) Secondary else Primary,
        modifier = Modifier.clickable(onClick = onClick, indication = null, interactionSource = remember{MutableInteractionSource()})
    )

}

@Composable
fun InfoCard(detailsState: DetailsState.Success, displayPercent: StableMetricUi?) {



    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
    ) {

        Row(
            modifier=Modifier.fillMaxWidth().padding(top=8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        )
        {

            Box(
                modifier = Modifier.size(48.dp).clip(RoundedCornerShape(15.dp))
                    .background(Primary),
                contentAlignment = Alignment.Center
            ){
                Text(
                    text= detailsState.data.scheme_name.take(2).capitalize(Locale.current),
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color.White
                )
            }

            Box(
                modifier = Modifier
                    .size(48.dp)
                    .genericDropShadow(CircleShape)
                    .clip(CircleShape)
                    .background(Color.White)
                    .clickable(onClick = {}),
                contentAlignment = Alignment.Center
            ){
                Icon(
                    painter = painterResource(Res.drawable.icon_share),
                    contentDescription = null,
                    modifier = Modifier.size(32.dp),
                    tint = Secondary
                )
            }

        }

        Text(
            text=detailsState.data.scheme_name,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black,
            modifier = Modifier.padding(top=8.dp)
        )

        Text(
            text= detailsState.data.risk_name +" | " + detailsState.data.scheme_type ,
            style = titlesStyle,
            color = Color.Black
        )

        displayPercent?.let{
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = it.value.trimTo(2) + "%",
                    fontFamily = Poppins,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (it.value> 0) appGreen else Color.Red
                )

                Text(
                    text = it.label + " annualised",
                    style = titlesStyle,
                    color = titleColor
                )

            }
        }

        Row(
            modifier=Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text= (if (detailsState.data.metrics.nav_change_pct>0) "+" else "") + detailsState.data.metrics.nav_change_pct.toString()+"%",
                style = titlesStyle,
                color = if (detailsState.data.metrics.nav_change_pct>0) appGreen else Color.Red
            )
            Text(
                text= "1D",
                style = titlesStyle,
                color = titleColor
            )
        }

    }

}