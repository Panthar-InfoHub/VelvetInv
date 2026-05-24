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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import org.sharad.velvetinvestment.shared.UiStateContainer
import org.sharad.velvetinvestment.shared.compose.BarHeader
import org.sharad.velvetinvestment.shared.compose.ShadowCard
import org.sharad.velvetinvestment.shared.compose.ToggleSwitch
import org.sharad.velvetinvestment.shared.theme.LocalVelvetShapes
import org.sharad.velvetinvestment.shared.theme.Poppins
import org.sharad.velvetinvestment.shared.theme.titlesStyle
import org.sharad.velvetinvestment.utils.AppEvent
import org.sharad.velvetinvestment.utils.AppEventsController
import org.sharad.velvetinvestment.utils.withInterRupee
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.back_arrow
import velvet.composeapp.generated.resources.download_ic
import velvet.composeapp.generated.resources.ic_pencil

@Composable
fun FireReportScreen(
    onBack: () -> Unit,
    onUpdateClick: () -> Unit
) {

    val viewModel: FireReportViewModel = koinViewModel()
    val uiState by viewModel.fireReportUiModel.collectAsStateWithLifecycle()
    val emiIncluded by viewModel.emiIncluded.collectAsStateWithLifecycle()
    val selectedYear by viewModel.selectedYear.collectAsStateWithLifecycle()
    val downloading by viewModel.downloadingReport.collectAsStateWithLifecycle()
    val showProjected by viewModel.showProjected.collectAsStateWithLifecycle()

    val onBackRemembered = remember(onBack) { onBack }
    val onUpdateClickRemembered = remember(onUpdateClick) { onUpdateClick }
    val onEmiSwitchClick = remember(viewModel) { { viewModel.toggleEmi() } }
    val onProjectedSwitchClick = remember(viewModel) { { viewModel.toggleProjected() } }
    val selectedYearChange = remember(viewModel) { { it: SelectedYear -> viewModel.onSelectedYearChange(it) } }
    val onIconClick = remember(viewModel) { { viewModel.downloadFireReport() } }
    val onRetry = remember(viewModel) { { viewModel.loadData() } }

    LaunchedEffect(Unit){
        AppEventsController.appEvent
            .collect {
                when(it){
                    AppEvent.FireRefreshEvent -> {
                        viewModel.loadData()
                        AppEventsController.sendHomeRefreshEvent()
                    }
                    AppEvent.GoalEventRefresh ->{
                        viewModel.loadData()
                        AppEventsController.sendHomeRefreshEvent()
                    }

                    else -> {}
                }
            }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {

        FireBackHeader(
            heading = "F.I.R.E Report",
            showBack = true,
            onBackClick = onBackRemembered,
            onIconClick = onIconClick,
            downloading = downloading
        )
        UiStateContainer(
            modifier = Modifier.weight(1f).fillMaxSize(),
            uiState = uiState,
            onRetry = onRetry
        ) { data ->
            FireReportContent(
                fireState = data,
                emiIncluded = emiIncluded,
                showProjected = showProjected,
                selectedYear = selectedYear,
                onEmiSwitchClick = onEmiSwitchClick,
                onProjectedSwitchClick = onProjectedSwitchClick,
                selectedYearChange = selectedYearChange,
                onUpdateClick = onUpdateClickRemembered
            )
        }
    }

}

@Composable
fun FireReportContent(
    fireState: FireReportUiModel,
    emiIncluded: Boolean,
    onEmiSwitchClick: () -> Unit,
    selectedYear: SelectedYear,
    selectedYearChange: (SelectedYear) -> Unit,
    onUpdateClick: () -> Unit,
    showProjected: Boolean,
    onProjectedSwitchClick: () -> Unit
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

    val progressLambda = remember(progressAnim) { { progressAnim.value } }

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
                onUpdateClick=onUpdateClick,
                onProjectedClick = onProjectedSwitchClick,
                showProjected = !showProjected
            )
        }
        item {
            PortFolioProjectionChart(
                data = fireState.portfolioChart,
                startYear = fireState.startYear,
                endYear = fireState.endYear,
                selectedYear=selectedYear,
                progress=progressLambda
            )
        }
        item {
            FireProjectionChart(
                data = fireState.firePercentageChart,
                startYear = fireState.startYear,
                endYear = fireState.endYear,
                selectedYear=selectedYear,
                progress=progressLambda
            )
        }
        item {
            val heading = remember(selectedYear) {
                val yearPrefix = when(selectedYear){
                    SelectedYear.FIVE_YEARS -> "5"
                    SelectedYear.TEN_YEARS -> "10"
                    SelectedYear.TWENTY_YEARS -> "20"
                }
                "$yearPrefix Years Projections"
            }
            BarHeader(heading = heading)
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
    progress: () -> Float
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
                    text = "Amount (₹)".withInterRupee(),
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
    progress: () -> Float
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
    onSelectedYearChange: (SelectedYear) -> Unit,
    onUpdateClick: () -> Unit,
    onProjectedClick: () -> Unit,
    showProjected: Boolean
) {
    ShadowCard {
        Column(
            modifier=Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    modifier=Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Text(
                        text = "FIRE Report",
                        style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.SemiBold),
                        color = Color.Black
                    )

                    UpdateDetailsButton(
                        onClick = onUpdateClick
                    )

                }
                Text(
                    text = "Projected Financial Independence",
                    color = titleColor,
                    style = titlesStyle
                )
            }
            YearSelector(selectedYear,onSelectedYearChange)
            FireToggle(text="Include EMI",emiIncluded,onEmiIncludedClick)
            FireToggle(text="Projected Vs Actual",showProjected,onProjectedClick)
        }
    }
}

@Composable
fun UpdateDetailsButton(onClick: () -> Unit) {
    val shapes = LocalVelvetShapes.current
    val tintColor= Color(0xff0284C7)
    Box(
        modifier = Modifier
            .clip(shapes.circle)
            .shadow(2.dp)
            .background(
                Color(0xffE0F2FE)
            )
            .clickable(
                onClick = onClick
            )

    ){
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 2.dp)
        ) {
            Icon(
                painter=painterResource(Res.drawable.ic_pencil),
                contentDescription = null,
                tint = tintColor,
                modifier = Modifier.size(10.dp)
            )
            Text(
                text = "Update Details",
                color = tintColor,
                fontFamily = Poppins,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun YearSelector(
    selectedYear: SelectedYear,
    onSelectedYearChange: (SelectedYear) -> Unit
) {
    val shapes = LocalVelvetShapes.current
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
                .clip(shapes.roundedDp12)
                .background(Color(0xffF1F5F9))
        ) {

            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(58.dp)
                    .padding(5.dp)
                    .offset(x = animatedOffset)
                    .clip(shapes.roundedDp8)
                    .background(Secondary)
            )

            Row(
                modifier = Modifier.fillMaxHeight()
            ) {
                SelectedYear.entries.forEach { year ->
                    val onClick = remember(year, onSelectedYearChange) {
                        { onSelectedYearChange(year) }
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(58.dp)
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() },
                                onClick = onClick
                            ),
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
fun FireToggle(text: String, value: Boolean, onToggle: () -> Unit) {
    val onToggleRemembered = remember(onToggle) { { _: Boolean -> onToggle() } }
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            style = titlesStyle.copy(fontWeight = FontWeight.SemiBold),
            color = Color(0xff314158)

        )

        ToggleSwitch(
            checked = value,
            onCheckedChange = onToggleRemembered,
            width = 48.dp,
            height = 24.dp,
            thumbSize = 20.dp,
            checkedTrackColor = Color(0xff10B981),
            uncheckedTrackColor = Color.White,
            checkedThumbColor = Color.White,
            uncheckedThumbColor = Color(0xff10B981)
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

        if (downloading) {
            CircularProgressIndicator(
                modifier = Modifier.padding(end = 4.dp).size(22.dp)
                    .align(Alignment.CenterEnd),
                color = Secondary
            )
        } else {
            Icon(
                painter = painterResource(Res.drawable.download_ic),
                contentDescription = null,
                tint = Secondary,
                modifier = Modifier.padding(end = 4.dp).size(22.dp).clickable(
                    onClick = onIconClick,
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ).align(Alignment.CenterEnd)
            )
        }
    }
}