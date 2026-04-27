package org.sharad.velvetinvestment.presentation.goals.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import org.sharad.emify.core.ui.theme.*
import org.sharad.velvetinvestment.presentation.goals.viewmodel.ProjectionImpactUiData
import org.sharad.velvetinvestment.presentation.goals.viewmodel.ProjectionImpactViewModel
import org.sharad.velvetinvestment.shared.CustomProgressFillBar
import org.sharad.velvetinvestment.shared.compose.ErrorScreen
import org.sharad.velvetinvestment.shared.compose.LoaderScreen
import org.sharad.velvetinvestment.shared.genericDropShadow
import org.sharad.velvetinvestment.utils.UiState
import org.sharad.velvetinvestment.presentation.explorefunds.uimodel.MutualFundTopPicksUiModel
import org.sharad.velvetinvestment.utils.formatWithCommas
import coil3.compose.AsyncImage
import org.sharad.velvetinvestment.presentation.onboarding.compose.personaldetails.NextButtonFooter
import org.sharad.velvetinvestment.utils.formatMoneyWithUnits
import org.sharad.velvetinvestment.utils.theme.VelvetTheme
import org.sharad.velvetinvestment.utils.theme.subHeading
import org.sharad.velvetinvestment.utils.theme.titlesStyle
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.*

@Composable
fun ProjectionImpactScreen(
    goalId: String,
    onBack: () -> Unit = {},
    onInvestNow: () -> Unit = {},
    onEditGoal: (String) -> Unit = {},
    onRemoveGoal: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val viewModel: ProjectionImpactViewModel = koinViewModel { parametersOf(goalId) }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var showMenu by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            ProjectionImpactHeader(
                onBack = onBack,
                onMoreClick = { showMenu = true },
                showMenu = showMenu,
                onDismissMenu = { showMenu = false },
                onEdit = { 
                    showMenu = false
                    onEditGoal(goalId) 
                },
                onRemove = { 
                    showMenu = false
                    viewModel.deleteGoal(goalId, onSuccess = onBack)
                }
            )
        },
        bottomBar = {
            NextButtonFooter(
                onClick = onInvestNow,
                pv = PaddingValues(0.dp),
                value = "Invest Now",
            )
        },
        containerColor = Color(0xFFF8F9FE)
    ) { paddingValues ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val state = uiState) {
                is UiState.Loading -> LoaderScreen()
                is UiState.Error -> ErrorScreen(
                    errorMessage = state.message,
                    onRetryClick = { viewModel.loadGoalDetails() }
                )
                is UiState.Success -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(bottom = 16.dp)
                    ) {
                        ProjectedImpactCard(data = state.data)

//                        ExploreBundleGoalsSection(
//                            onInvestNow = onInvestNow
//                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProjectionImpactHeader(
    onBack: () -> Unit,
    onMoreClick: () -> Unit,
    showMenu: Boolean,
    onDismissMenu: () -> Unit,
    onEdit: () -> Unit = {},
    onRemove: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        IconButton(
            onClick = onBack,
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            Icon(
                painter = painterResource(Res.drawable.back_arrow),
                contentDescription = "Back",
                modifier = Modifier.size(24.dp),
                tint = Primary
            )
        }

        Text(
            text = stringResource(Res.string.projection_impact),
            style = MaterialTheme.typography.headlineSmall,
            color = Primary,
            fontWeight = FontWeight.Bold
        )

        Box(modifier = Modifier.align(Alignment.CenterEnd)) {
            IconButton(onClick = onMoreClick) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .padding(vertical = 2.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(2.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        repeat(3) {
                            Box(
                                modifier = Modifier
                                    .size(4.dp)
                                    .background(Primary, CircleShape)
                            )
                        }
                    }
                }
            }

            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = onDismissMenu,
                modifier = Modifier.background(Color.White)
            ) {
//                DropdownMenuItem(
//                    text = {
//                        Text(
//                            stringResource(Res.string.edit),
//                            style = MaterialTheme.typography.titleMedium
//                        )
//                    },
//                    onClick = onEdit
//                )
//                HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
                DropdownMenuItem(
                    text = {
                        Text(
                            stringResource(Res.string.remove),
                            style = MaterialTheme.typography.titleMedium
                        )
                    },
                    onClick = onRemove
                )
            }
        }
    }
}

@Composable
fun ProjectedImpactCard(
    data: ProjectionImpactUiData,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(Res.string.projected_impact),
            style = MaterialTheme.typography.titleLarge,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .genericDropShadow(RoundedCornerShape(24.dp))
                .clip(RoundedCornerShape(24.dp))
                .background(Color.White)
                .padding(20.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(
                    text = data.goalName,
                    style = MaterialTheme.typography.titleMedium,
                    color = orangeColor,
                    fontWeight = FontWeight.Bold
                )

                Row(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = stringResource(Res.string.todays_cost),
                            style = titlesStyle,
                            color = titleColor
                        )
                        Text(
                            text = "₹ ${formatCurrency(data.todaysCost)}",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = stringResource(Res.string.future_value),
                            style = titlesStyle,
                            color = titleColor
                        )
                        Text(
                            text = "₹ ${formatCurrency(data.futureValue.toLong())}",
                            style = MaterialTheme.typography.titleMedium,
                            color = orangeColor,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Row(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = stringResource(Res.string.target),
                            style = titlesStyle,
                            color = titleColor
                        )
                        Text(
                            text = "${data.targetYear}",
                            style = MaterialTheme.typography.titleMedium,
                            color = redColor,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = stringResource(Res.string.monthly_sip),
                            style = titlesStyle,
                            color = titleColor
                        )
                        Text(
                            text = "₹ ${formatCurrency(data.monthlySip.toLong())}",
                            style = MaterialTheme.typography.titleMedium,
                            color = greenColor,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Column {
                    Text(
                        text = stringResource(Res.string.feasibility_score),
                        style = titlesStyle,
                        color = Color.Black,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    CustomProgressFillBar(
                        progress = data.feasibilityScore,
                        progressColor = greenColor,
                        trackColor = Color(0xFFE0E0E0),
                        thickness = 10.dp,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = stringResource(Res.string.progress),
                            style = titlesStyle,
                            color = Color.Black,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "₹ ${formatCurrency(data.currentSaved)} / ₹ ${formatCurrency(data.targetAmount)}",
                            style = titlesStyle,
                            color = Color.Black,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    CustomProgressFillBar(
                        progress = if (data.targetAmount > 0) (data.currentSaved.toFloat() / data.targetAmount) else 0f,
                        progressColor = appOrange,
                        trackColor = Color(0xFFE0E0E0),
                        thickness = 10.dp,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(Res.string.increased_by)+"₹ ${formatMoneyWithUnits(data.increasedBy.toLong())}",
                        style = titlesStyle,
                        color = orangeColor,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = stringResource(Res.string.req_monthly)+"₹ ${formatMoneyWithUnits(data.requiredMonthly.toLong())}",
                        style = titlesStyle,
                        color = greenColor,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

private fun formatCurrency(amount: Long): String {
    return formatWithCommas(amount)
}


@Composable
fun ExploreBundleGoalsSection(
    onInvestNow: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val samplePicks = remember {
        listOf(
            MutualFundTopPicksUiModel(
                icon = "",
                name = "SBI Gold Fund",
                metadata = "High Risk . Commodities . Gold",
                returnYears = 3,
                percentage = 18.5,
                id = "1"
            ),
            MutualFundTopPicksUiModel(
                icon = "",
                name = "HDFC Index Fund",
                metadata = "Moderate Risk . Equity . Large Cap",
                returnYears = 3,
                percentage = 15.2,
                id = "2"
            ),
            MutualFundTopPicksUiModel(
                icon = "",
                name = "ICICI Prudential Bluechip",
                metadata = "Moderate Risk . Equity . Large Cap",
                returnYears = 3,
                percentage = 16.8,
                id = "3"
            )
        )
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(20.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Secondary)
            )
            Text(
                text = stringResource(Res.string.explore_bundle_goals),
                style = MaterialTheme.typography.titleMedium,
                color = Primary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .weight(1f)
            )
            Icon(
                painter = painterResource(Res.drawable.back_arrow),
                contentDescription = null,
                modifier = Modifier
                    .size(20.dp)
                    .clickable { },
                tint = Primary
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(samplePicks) { pick ->
                BundleGoalCard(
                    data = pick,
                    onInvestNow = onInvestNow
                )
            }
        }
    }
}

@Composable
fun BundleGoalCard(
    data: MutualFundTopPicksUiModel,
    onInvestNow: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .width(220.dp)
            .genericDropShadow(RoundedCornerShape(20.dp))
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .padding(16.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                AsyncImage(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(bgColor4),
                    model = data.icon,
                    contentDescription = null,
                    fallback = painterResource(Res.drawable.rectangle_19),
                    error = painterResource(Res.drawable.rectangle_19),
                    placeholder = painterResource(Res.drawable.rectangle_19)
                )

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = stringResource(Res.string.returns_3y), // Note: Using 3y from string resources for now
                        style = titlesStyle.copy(fontSize = 10.sp),
                        color = titleColor
                    )
                    Text(
                        text = "${data.percentage}% p.a.",
                        style = titlesStyle,
                        color = greenColor,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Text(
                text = data.name,
                style = subHeading,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )

            Text(
                text = data.metadata,
                style = titlesStyle.copy(fontSize = 11.sp),
                color = titleColor,
                maxLines = 1
            )

            OutlinedButton(
                onClick = onInvestNow,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp),
                shape = RoundedCornerShape(22.dp),
                border = ButtonDefaults.outlinedButtonBorder(enabled = true).copy(width = 1.dp, brush = SolidColor(Primary))
            ) {
                Text(
                    text = stringResource(Res.string.invest_now),
                    style = titlesStyle,
                    color = Primary,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}


@Composable
@Preview
fun ProjectionImpactScreenPreview() {
    VelvetTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            ProjectedImpactCard(
                data = ProjectionImpactUiData(
                    goalName = "Education",
                    todaysCost = 1000000,
                    futureValue = 2500000.0,
                    targetYear = 2035,
                    monthlySip = 12000.0,
                    feasibilityScore = 0.8f,
                    currentSaved = 200000,
                    targetAmount = 2500000,
                    increasedBy = 1500000.0,
                    requiredMonthly = 12000.0
                )
            )
        }
    }
}
