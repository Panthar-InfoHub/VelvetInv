package org.sharad.velvetinvestment.presentation.firereport.compose

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import org.sharad.emify.core.ui.theme.Primary
import org.sharad.emify.core.ui.theme.Secondary
import org.sharad.emify.core.ui.theme.titleColor
import org.sharad.velvetinvestment.presentation.firereport.uimodels.FirePercentagePointUiModel
import org.sharad.velvetinvestment.presentation.firereport.uimodels.FireReportUiModel
import org.sharad.velvetinvestment.presentation.firereport.uimodels.PortfolioProjectionPointUiModel
import org.sharad.velvetinvestment.presentation.firereport.uimodels.toFireMapPoints
import org.sharad.velvetinvestment.presentation.firereport.uimodels.toMapPoints
import org.sharad.velvetinvestment.presentation.firereport.viewmodel.FireReportViewModel
import org.sharad.velvetinvestment.presentation.firereport.viewmodel.SelectedYear
import org.sharad.velvetinvestment.shared.compose.BarHeader
import org.sharad.velvetinvestment.shared.compose.ErrorScreen
import org.sharad.velvetinvestment.shared.compose.LoaderScreen
import org.sharad.velvetinvestment.shared.compose.ShadowCard
import org.sharad.velvetinvestment.shared.compose.ToggleSwitch
import org.sharad.velvetinvestment.utils.UiState
import org.sharad.velvetinvestment.utils.theme.titlesStyle
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.back_arrow
import velvet.composeapp.generated.resources.icon_download

@Composable
fun FireReportScreen(
    onBack: () -> Unit
){

    val viewModel: FireReportViewModel = koinViewModel()
    val uiState by viewModel.fireReportUiModel.collectAsStateWithLifecycle()
    val emiIncluded by viewModel.emiIncluded.collectAsStateWithLifecycle()
    val selectedYear by viewModel.selectedYear.collectAsStateWithLifecycle()
    val downloading by viewModel.downloadingReport.collectAsStateWithLifecycle()



    Column(
        modifier = Modifier.fillMaxSize(),
    ){

        FireBackHeader(
            heading = "F.I.R.E Report",
            showBack = true,
            onBackClick = onBack,
            onIconClick = { viewModel.downloadFireReport() },
            downloading = downloading
        )
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
                        fireState = (uiState as UiState.Success<FireReportUiModel>).data,
                        emiIncluded =emiIncluded,
                        selectedYear =selectedYear,
                        onEmiSwitchClick = {
                            viewModel.toggleEmi()
                        },
                        selectedYearChange = {
                            viewModel.onSelectedYearChange(it)
                        }
                    )
                }
            }
        }
    }

}

@Composable
fun FireReportContent(
    fireState: FireReportUiModel,
    emiIncluded: Boolean,
    onEmiSwitchClick: () -> Unit,
    selectedYear: SelectedYear,
    selectedYearChange: (SelectedYear) -> Unit
) {

    val progressAnim = remember { Animatable(0f) }

    LaunchedEffect(selectedYear) {
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

    LazyColumn(
        modifier=Modifier.fillMaxSize().padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        contentPadding = PaddingValues(top = 24.dp)
    ) {
        item{
            FireReportHeadSwitcher(
                emiIncluded = emiIncluded,
                selectedYear = selectedYear,
                onSelectedYearChange=selectedYearChange,
                onEmiIncludedClick = onEmiSwitchClick,
            )
        }
        item() {
            PortFolioProjectionChart(
                data = fireState.portfolioChart,
                startYear = fireState.startYear,
                endYear = fireState.endYear,
                selectedYear=selectedYear,
                progress=progress
            )
        }
        item() {
            FireProjectionChart(
                data = fireState.firePercentageChart,
                startYear = fireState.startYear,
                endYear = fireState.endYear,
                selectedYear=selectedYear,
                progress=progress
            )
        }
        item {
            BarHeader(
                heading = when(selectedYear){
                    SelectedYear.FIVE_YEARS -> "5"
                    SelectedYear.TEN_YEARS -> "10"
                    SelectedYear.TWENTY_YEARS -> "20"
                }+" Years Projections"
            )
        }
        items(items = fireState.projectionRows, key = {it.year},
            contentType = { "projection_card" }){
            ProjectionCard(projection = it)
        }
        item {
            Spacer(
                modifier = Modifier.height(20.dp)
            )
        }
    }
}

@Composable
fun PortFolioProjectionChart(
    data: List<PortfolioProjectionPointUiModel>,
    startYear: Int,
    endYear: Int,
    selectedYear: SelectedYear,
    progress: Float
) {
    val chartData = remember(data) { data.toMapPoints() }
    ShadowCard{
        Column(
            modifier=Modifier.fillMaxWidth().padding(vertical = 28.dp, horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier=Modifier.fillMaxWidth()
            ){
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(
                        text = "Portfolio Projection "+ when(selectedYear){
                            SelectedYear.FIVE_YEARS -> "(5 yrs)"
                            SelectedYear.TEN_YEARS -> "(10 yrs)"
                            SelectedYear.TWENTY_YEARS -> "(20 yrs)"
                        },
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Black,
                    )
                    Text(
                        text = "$startYear - $endYear",
                        style = titlesStyle,
                        color = titleColor
                    )
                }
                Text(
                    text = "Amount (₹)",
                    style = titlesStyle,
                    color = titleColor
                )
            }
                LineChart(
                    data = chartData,
                    modifier = Modifier.fillMaxWidth().height(140.dp),
                    progress = progress
                )

        }
    }
}
@Composable
fun FireProjectionChart(
    data: List<FirePercentagePointUiModel>,
    startYear: Int,
    endYear: Int,
    selectedYear: SelectedYear,
    progress: Float
) {
    val chartData = remember(data) { data.toFireMapPoints() }
    ShadowCard{
        Column(
            modifier=Modifier.fillMaxWidth().padding(vertical = 28.dp, horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier=Modifier.fillMaxWidth()
            ){
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(
                        text = "FIRE % "+  when(selectedYear){
                            SelectedYear.FIVE_YEARS -> "(5 yrs)"
                            SelectedYear.TEN_YEARS -> "(10 yrs)"
                            SelectedYear.TWENTY_YEARS -> "(20 yrs)"
                        },
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Black,
                    )
                    Text(
                        text = "$startYear - $endYear",
                        style = titlesStyle,
                        color = titleColor
                    )
                }
                Text(
                    text = "%",
                    style = titlesStyle,
                    color = titleColor
                )
            }
            LineChart(
                data =chartData,
                modifier = Modifier.fillMaxWidth().height(140.dp),
                progress = progress,
                color = Secondary
            )
        }
    }
}


@Composable
fun FireReportHeadSwitcher(
    onEmiIncludedClick: () -> Unit,
    emiIncluded: Boolean,
    selectedYear: SelectedYear,
    onSelectedYearChange: (SelectedYear) -> Unit
) {
    ShadowCard {
        Column(
            modifier=Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "FIRE Report",
                    style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.SemiBold),
                    color = Color.Black
                )
                Text(
                    text = "Projected Financial Independence",
                    color = titleColor,
                    style = titlesStyle
                )
            }
            YearSelector(selectedYear,onSelectedYearChange)
            IncludeEmi(emiIncluded,onEmiIncludedClick)
        }
    }
}

@Composable
fun YearSelector(
    selectedYear: SelectedYear,
    onSelectedYearChange: (SelectedYear) -> Unit
) {

    val animatedOffset by animateDpAsState(
        targetValue = when (selectedYear) {
            SelectedYear.FIVE_YEARS -> 0.dp
            SelectedYear.TEN_YEARS -> 58.dp
            SelectedYear.TWENTY_YEARS -> 58.dp *2
        }
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Text(
            text = "Projection Horizon",
            style = titlesStyle.copy(fontWeight = FontWeight.SemiBold),
            color = Color(0xff314158)
        )

        Box(
            modifier = Modifier
                .height(34.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xffF1F5F9))
        ) {

            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(58.dp)
                    .padding(5.dp)
                    .offset(x = animatedOffset)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Secondary)
            )

            Row(
                modifier = Modifier.fillMaxHeight()
            ) {
                SelectedYear.entries.forEach { year ->
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(58.dp)
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) {
                                onSelectedYearChange(year)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = year.value,
                            style = MaterialTheme.typography.bodySmall,
                            color = if (selectedYear == year) Color.White else Color(0xff45556C)
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun IncludeEmi(emiIncluded: Boolean, onEmiIncludedClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Include EMI",
            style = titlesStyle.copy(fontWeight = FontWeight.SemiBold),
            color = Color(0xff314158)

        )

        ToggleSwitch(
            checked = emiIncluded,
            onCheckedChange = { onEmiIncludedClick() },
            width = 48.dp,
            height = 24.dp,
            thumbSize = 20.dp,
            checkedTrackColor = Color(0xff10B981),
            uncheckedTrackColor = Color.White,
            checkedThumbColor = Color.White,
            uncheckedThumbColor =Color(0xff10B981)
        )
    }
}

@Composable
fun FireBackHeader(
    heading: String,
    showBack: Boolean = false,
    modifier: Modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp, horizontal = 12.dp),
    onBackClick: () -> Unit = {},
    onIconClick: () -> Unit,
    downloading: Boolean
){
    Box(
        modifier=modifier,
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = heading,
            style = MaterialTheme.typography.headlineLarge,
            color = Primary
        )

        if (showBack){
            Icon(
                painter = painterResource(Res.drawable.back_arrow),
                contentDescription = null,
                modifier = Modifier.size(22.dp).clickable(
                    onClick = onBackClick,
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ).align(Alignment.CenterStart)
            )
        }

        if (downloading){
            CircularProgressIndicator(
                modifier = Modifier.padding(end = 4.dp).size(22.dp)
                    .align(Alignment.CenterEnd),
                color = Secondary
            )
        }
        else{
            Icon(
                painter = painterResource(Res.drawable.icon_download),
                contentDescription = null,
                tint = Secondary,
                modifier = Modifier.padding(end = 4.dp).size(22.dp).clickable(
                    onClick = { onIconClick() },
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ).align(Alignment.CenterEnd)
            )
        }
    }
}