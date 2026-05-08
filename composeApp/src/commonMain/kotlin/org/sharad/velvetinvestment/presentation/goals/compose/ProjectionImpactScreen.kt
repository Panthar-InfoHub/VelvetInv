package org.sharad.velvetinvestment.presentation.goals.compose

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import org.sharad.emify.core.ui.theme.Primary
import org.sharad.emify.core.ui.theme.appOrange
import org.sharad.emify.core.ui.theme.bgColor3
import org.sharad.emify.core.ui.theme.bgColor4
import org.sharad.emify.core.ui.theme.greenColor
import org.sharad.emify.core.ui.theme.orangeColor
import org.sharad.emify.core.ui.theme.redColor
import org.sharad.emify.core.ui.theme.titleColor
import org.sharad.velvetinvestment.domain.models.mutualfunds.BundledMutualFundDomain
import org.sharad.velvetinvestment.presentation.explorefunds.uimodel.MutualFundTopPicksUiModel
import org.sharad.velvetinvestment.presentation.goals.viewmodel.ProjectionImpactUiData
import org.sharad.velvetinvestment.presentation.goals.viewmodel.ProjectionImpactViewModel
import org.sharad.velvetinvestment.presentation.mutualfund.compose.BundleCard
import org.sharad.velvetinvestment.presentation.onboarding.compose.personaldetails.NextButtonFooter
import org.sharad.velvetinvestment.shared.CustomProgressFillBar
import org.sharad.velvetinvestment.shared.UiStateContainer
import org.sharad.velvetinvestment.shared.compose.BarHeader
import org.sharad.velvetinvestment.shared.compose.VelvetLoader
import org.sharad.velvetinvestment.shared.genericDropShadow
import org.sharad.velvetinvestment.utils.UiState
import org.sharad.velvetinvestment.utils.formatMoneyAfterL
import org.sharad.velvetinvestment.utils.formatMoneyWithUnits
import org.sharad.velvetinvestment.utils.formatWithCommas
import org.sharad.velvetinvestment.shared.theme.VelvetTheme
import org.sharad.velvetinvestment.shared.theme.subHeading
import org.sharad.velvetinvestment.shared.theme.titlesStyle
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.back_arrow
import velvet.composeapp.generated.resources.feasibility_score
import velvet.composeapp.generated.resources.future_value
import velvet.composeapp.generated.resources.increased_by
import velvet.composeapp.generated.resources.invest_now
import velvet.composeapp.generated.resources.monthly_sip
import velvet.composeapp.generated.resources.progress
import velvet.composeapp.generated.resources.projected_impact
import velvet.composeapp.generated.resources.projection_impact
import velvet.composeapp.generated.resources.rectangle_19
import velvet.composeapp.generated.resources.remove
import velvet.composeapp.generated.resources.req_monthly
import velvet.composeapp.generated.resources.returns_3y
import velvet.composeapp.generated.resources.target
import velvet.composeapp.generated.resources.todays_cost

@Composable
fun ProjectionImpactScreen(
    goalId: String,
    onBack: () -> Unit = {},
    onInvestNow: () -> Unit = {},
    navigateToAllBundles : () -> Unit = {},
    navigateToSpecificBundle: (String) -> Unit = {},
    onEditGoal: (String) -> Unit = {},
    onRemoveGoal: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val viewModel: ProjectionImpactViewModel = koinViewModel { parametersOf(goalId) }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val bundleState by viewModel.bundleData.collectAsStateWithLifecycle()

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
        containerColor = Color.White
    ) { paddingValues ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            UiStateContainer(
                uiState = uiState,
                onRetry = { viewModel.loadGoalDetails() }
            ) { data ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 16.dp)
                ) {
                    item {
                        HorizontalDivider(
                            modifier= Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                            thickness = 2.dp,
                            color = bgColor3
                        )
                    }
                    item{
                        ProjectedImpactCard(data = data)
                    }
                    item{
                        ExploreBundleGoalsSection(
                            onArrowClick = navigateToAllBundles,
                            onBundleClick = navigateToSpecificBundle,
                            state = bundleState
                        )
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
                tint = Color.Black
            )
        }

        Text(
            text = stringResource(Res.string.projection_impact),
            style = MaterialTheme.typography.headlineLarge,
            color = Primary
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
    onArrowClick: () -> Unit ,
    onBundleClick: (String) -> Unit,
    state: UiState<List<BundledMutualFundDomain>>
) {
    when(state){
        is UiState.Error -> {
            Text(
                text = "Unable to load bundles",
                style = MaterialTheme.typography.bodyMedium,
                color = titleColor,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
        UiState.Loading -> {
            VelvetLoader()
        }
        is UiState.Success -> {
            val data = state.data
            Column(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                BarHeader(
                    heading = "Explore Bundle Goals",
                    showArrow = true,
                    onArrowClick = onArrowClick
                )
                data.chunked(2).forEach { chunk->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        chunk.forEach {bundle->
                            BundleCard(
                                title = bundle.categoryName,
                                minAmount = "₹" + formatMoneyAfterL(bundle.minAmount.toLong()),
                                onClick = { onBundleClick(bundle.key) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
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
