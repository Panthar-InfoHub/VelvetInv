package org.sharad.velvetinvestment.presentation.mutualfund.compose

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import org.sharad.emify.core.ui.theme.Primary
import org.sharad.emify.core.ui.theme.Secondary
import org.sharad.emify.core.ui.theme.appGreen
import org.sharad.emify.core.ui.theme.bgColor3
import org.sharad.emify.core.ui.theme.bgColor4
import org.sharad.emify.core.ui.theme.titleColor
import org.sharad.velvetinvestment.domain.models.mutualfunds.AssetsAllocationDomain
import org.sharad.velvetinvestment.domain.models.mutualfunds.MutualFundDomain
import org.sharad.velvetinvestment.presentation.mutualfund.DetailsState
import org.sharad.velvetinvestment.presentation.mutualfund.GraphDurationSelection
import org.sharad.velvetinvestment.presentation.mutualfund.GraphState
import org.sharad.velvetinvestment.presentation.mutualfund.toPieChartEntries
import org.sharad.velvetinvestment.presentation.mutualfund.toUI
import org.sharad.velvetinvestment.presentation.mutualfund.viewmodel.MutualFundDetailsScreenViewModel
import org.sharad.velvetinvestment.shared.PieChart
import org.sharad.velvetinvestment.shared.compose.BarHeader
import org.sharad.velvetinvestment.shared.compose.ContinueBackButtonFooter
import org.sharad.velvetinvestment.shared.compose.ErrorScreen
import org.sharad.velvetinvestment.shared.compose.LoaderScreen
import org.sharad.velvetinvestment.shared.compose.ShadowCard
import org.sharad.velvetinvestment.utils.formatMoneyAfterL
import org.sharad.velvetinvestment.utils.formatWithCommas
import org.sharad.velvetinvestment.utils.genericDropShadow
import org.sharad.velvetinvestment.utils.isoUtcToDisplayDate
import org.sharad.velvetinvestment.utils.theme.Poppins
import org.sharad.velvetinvestment.utils.theme.titlesStyle
import org.sharad.velvetinvestment.utils.trimDoubleTo
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.back_arrow
import velvet.composeapp.generated.resources.fd_placeholder
import velvet.composeapp.generated.resources.icon_share
import velvet.composeapp.generated.resources.icon_star
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
                        selectedYear = selectedYear,
                        onSelectedYearChange = viewModel::onSelectedYearChange,
                        pv=pv,
                        onFundTopClick=onTopFundClick,
                        onFundClick = onFundClick,
                        onMonthlySipClick = { onMonthlySipClick(id) },
                        onOneTimeSipClick = { onOneTimeSipClick(id)
                        viewModel.showBottomSheet()}
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
    onOneTimeSipClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    )
    {
        LazyColumn(
            modifier = Modifier.weight(1f).fillMaxSize(),
            contentPadding = PaddingValues(top = 8.dp)
        ) {
            item {
                InfoCard(detailsState)
            }
            item { Spacer(Modifier.height(20.dp)) }
            item {
                GraphCard(
                    graphState = graphState,
                    selectedYear = selectedYear,
                    onSelectedYearChange = onSelectedYearChange
                )
            }
            item {
                FundInfo(
                    date = detailsState.data.today,
                    nav = detailsState.data.todayNav,
                    minAmount = detailsState.data.minAmount,
                    fundSize = detailsState.data.fundSize,
                    rating = detailsState.data.rating
                )
            }
            item {
                BarHeader(
                    heading = "Asset Allocation",
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
            item { Spacer(Modifier.height(20.dp)) }
            item {
                AssetAllocationCard(assets = detailsState.data.assets)
            }
            item { Spacer(Modifier.height(20.dp)) }
            item {
                BarHeader(
                    heading = "Other Top Rated Funds",
                    modifier = Modifier.padding(horizontal = 16.dp),
                    showArrow = true,
                    onArrowClick = { onFundTopClick("topRated") }
                )
            }

            item {
                TopRatedFunds(
                    topFunds = detailsState.data.topFunds,
                    onFundClick = onFundClick
                )
            }

            item {
                InvestmentRiskCard()
            }

            item {
                Spacer(Modifier.height( 20.dp))
            }
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
            MutualFundGridCard(fund.toUI(), onClick = { onFundClick(fund.id) })
        }
    }
}

@Composable
fun AssetAllocationCard(assets: AssetsAllocationDomain) {

        ShadowCard(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
        ) {
            Row(
                modifier = Modifier.padding(vertical = 32.dp).fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier.weight(0.6f),
                    contentAlignment = Alignment.Center
                ) {
                    PieChart(
                        modifier = Modifier.size(156.dp),
                        data = assets.toPieChartEntries()
                    )
                }
                Box(
                    modifier = Modifier.weight(0.4f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        AssetsAllocationInfoRow(
                            Primary,
                            "Equity",
                            assets.equity.trimDoubleTo(1).toString()
                        )
                        AssetsAllocationInfoRow(
                            Secondary,
                            "Debt",
                            assets.debt.trimDoubleTo(1).toString()
                        )
                        AssetsAllocationInfoRow(
                            bgColor4,
                            "Cash",
                            assets.cash.trimDoubleTo(1).toString()
                        )
                    }
                }
            }
        }
}

@Composable
private fun AssetsAllocationInfoRow(
    color: Color,
    title: String,
    value: String
){

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = title,
            style = titlesStyle,
            color=Color.Black
        )
        Spacer(Modifier.width(12.dp))
        Text(
            text = "$value%",
            style = titlesStyle,
            color=Color.Black
        )
    }

}

@Composable
fun FundInfo(date: String, nav: Double, minAmount: Long, fundSize: Long, rating: Int?) {

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
                    text="₹ "+ nav.trimDoubleTo(2).toString(),
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color.Black
                )
            }
        }
        item{
            FundInfoCard(
                title = "Rating",
            ){
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ){
                    Text(
                        text = rating.toString(),
                        style = MaterialTheme.typography.headlineLarge,
                        color = Color.Black
                    )
                    Icon(
                        painter = painterResource(Res.drawable.icon_star),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = Secondary
                    )
                }
            }
        }

        item{
            FundInfoCard(
                title = "Min. amount",
            ){
                Text(
                    text= "₹ "+ formatWithCommas(minAmount),
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
                    text= "₹ "+ formatMoneyAfterL(fundSize),
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color.Black
                )
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
            modifier=Modifier.fillMaxWidth().height(78.dp).padding(16.dp),
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
    onSelectedYearChange: (GraphDurationSelection) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ){
        Box(
            modifier = Modifier.fillMaxWidth()
                .height(248.dp)
        ) {
            when (graphState) {
                is GraphState.Error -> {
                    ErrorScreen(graphState.error)
                }

                GraphState.Loading -> {
                    LoaderScreen()
                }

                is GraphState.Success -> {

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
fun InfoCard(detailsState: DetailsState.Success) {

    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
    ) {

        Row(
            modifier=Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            AsyncImage(
                model = detailsState.data.icon,
                contentDescription = null,
                modifier = Modifier.size(48.dp).clip(RoundedCornerShape(15.dp)),
                placeholder = painterResource(Res.drawable.fd_placeholder),
                error = painterResource(Res.drawable.fd_placeholder),
                fallback = painterResource(Res.drawable.fd_placeholder)
            )

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
            text=detailsState.data.name,
            style = MaterialTheme.typography.headlineLarge,
            color = Color.Black
        )

        Text(
            text= detailsState.data.category + (detailsState.data.risk?.let { it1 -> " . $it1" }?:"") + detailsState.data.type,
            style = titlesStyle,
            color = Color.Black
        )

        Row(
            modifier=Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text=detailsState.data.percentage.toString()+"%",
                fontFamily = Poppins,
                fontSize = 26.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (detailsState.data.percentage>0) appGreen else Color.Red
            )

            Text(
                text = detailsState.data.returnYear.toString()+"Y annualised",
                style = titlesStyle,
                color = titleColor
            )

        }

        Row(
            modifier=Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text= (if (detailsState.data.oneDayPercentage>0) "+" else "") + detailsState.data.oneDayPercentage.toString()+"%",
                style = titlesStyle,
                color = if (detailsState.data.oneDayPercentage>0) appGreen else Color.Red
            )
            Text(
                text= "1D",
                style = titlesStyle,
                color = titleColor
            )
        }

    }

}