package org.sharad.velvetinvestment.features.goals.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.sharad.emify.core.ui.theme.Primary
import org.sharad.emify.core.ui.theme.bgColor4
import org.sharad.velvetinvestment.features.goals.domain.models.GoalHoldingDomain
import org.sharad.velvetinvestment.features.goals.ui.viewmodels.ProjectedImpactEvent
import org.sharad.velvetinvestment.features.goals.ui.viewmodels.ProjectedImpactUiData
import org.sharad.velvetinvestment.shared.CustomProgressFillBar
import org.sharad.velvetinvestment.shared.UiStateContainer
import org.sharad.velvetinvestment.shared.compose.BarHeader
import org.sharad.velvetinvestment.shared.compose.NextButtonFooter
import org.sharad.velvetinvestment.shared.theme.Poppins
import org.sharad.velvetinvestment.shared.theme.VelvetTheme
import org.sharad.velvetinvestment.shared.theme.titlesStyle
import org.sharad.velvetinvestment.utils.UiState
import org.sharad.velvetinvestment.utils.withInterRupee
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.back_arrow

/**
 * One goal's projection, as `GET /user-goal/{id}` reports it. Velvet's Goal Projection screen
 * (header with a Remove menu, projection card, Map Scheme pill, Invest Now footer) carrying JN's
 * behaviour: the server's own figures and progress, the goal type, the mapped funds, and a remove
 * that cannot fire twice while it is in flight.
 */
@Composable
fun ProjectedImpactScreen(
    state: UiState<ProjectedImpactUiData>,
    handleEvent: (ProjectedImpactEvent) -> Unit,
    modifier: Modifier = Modifier,
    deleting: Boolean = false
) {
    var showMenu by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            ProjectionImpactHeader(
                onBack = { handleEvent(ProjectedImpactEvent.OnBackClicked) },
                onMoreClick = { showMenu = true },
                showMenu = showMenu,
                deleting = deleting,
                // Nothing to remove until the goal has loaded.
                menuEnabled = state is UiState.Success,
                onDismissMenu = { showMenu = false },
                onRemove = {
                    showMenu = false
                    handleEvent(ProjectedImpactEvent.DeleteGoal)
                }
            )
        },
        bottomBar = {
            NextButtonFooter(
                onClick = { handleEvent(ProjectedImpactEvent.OnInvestNowClicked) },
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
                uiState = state,
                onRetry = { handleEvent(ProjectedImpactEvent.LoadGoalDetails) }
            ) { data ->
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 20.dp)
                ) {
                    item {
                        ProjectedImpactCard(
                            data = data,
                            onMapClick = { handleEvent(ProjectedImpactEvent.OnMapSchemesClick) }
                        )
                    }
                    if (data.holdings.isNotEmpty()) {
                        item {
                            BarHeader(
                                heading = "Mapped Funds",
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                            )
                        }
                        items(data.holdings, key = { it.holdingId }) { holding ->
                            MappedHoldingCard(
                                holding = holding,
                                removing = false,
                                onRemove = {},
                                showDelete = false,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ProjectionImpactHeader(
    onBack: () -> Unit,
    onMoreClick: () -> Unit,
    showMenu: Boolean,
    deleting: Boolean,
    menuEnabled: Boolean,
    onDismissMenu: () -> Unit,
    onRemove: () -> Unit,
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
            text = "Goal Projection",
            style = MaterialTheme.typography.headlineLarge,
            color = Primary
        )

        Box(modifier = Modifier.align(Alignment.CenterEnd)) {
            // While the delete is in flight the menu becomes its own loader, so the goal cannot
            // be removed twice on a slow response.
            if (deleting) {
                Box(modifier = Modifier.size(48.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        strokeWidth = 2.dp,
                        color = Primary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            } else {
                IconButton(onClick = onMoreClick, enabled = menuEnabled) {
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
            }

            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = onDismissMenu,
                modifier = Modifier.background(Color.White)
            ) {
                DropdownMenuItem(
                    text = {
                        Text(
                            text = "Remove",
                            style = titlesStyle,
                            fontWeight = FontWeight.SemiBold
                        )
                    },
                    onClick = onRemove
                )
            }
        }
    }
}

@Composable
private fun ProjectedImpactCard(
    data: ProjectedImpactUiData,
    onMapClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = data.goalName,
                style = MaterialTheme.typography.headlineLarge,
                fontFamily = Poppins,
                color = Color.Black,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .clip(CircleShape)
                    .background(bgColor4.copy(0.1f))
                    .clickable(onClick = onMapClick)
                    .padding(vertical = 6.dp, horizontal = 12.dp)
            ) {
                Text(
                    text = "Map Scheme",
                    style = MaterialTheme.typography.bodySmall,
                    color = Primary
                )
                Icon(
                    painter = painterResource(Res.drawable.back_arrow),
                    contentDescription = null,
                    tint = Primary,
                    modifier = Modifier.size(14.dp).rotate(180f)
                )
            }
        }

        GoalProjectionCard(
            title = data.goalTypeName,
            isFixedCorpus = data.isFixedCorpus,
            todaysCost = data.todaysCost,
            futureValue = data.futureValue,
            targetText = data.targetYear?.toString() ?: "${data.yearsRemaining} yrs",
            monthlySip = data.monthlySip,
            footer = {
                GoalProgress(
                    saved = data.currentSavings,
                    target = data.futureValue,
                    progressPercent = data.progressPercent
                )
            }
        )
    }
}

/** Saved so far against the projected target; the bar is the server's `progress_percent`. */
@Composable
private fun GoalProgress(saved: Double, target: Double, progressPercent: Int) {
    val progress = (progressPercent / 100f).coerceIn(0f, 1f)
    val progressColor = when {
        progress < 0.25f -> Color(0xFFF44336) // Red
        progress < 0.50f -> Color(0xFFFF9800) // Orange
        progress < 0.75f -> Color(0xFFFFC107) // Amber
        else -> Color(0xFF4CAF50) // Green
    }
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Progress ($progressPercent%)",
                style = titlesStyle,
                color = Color.Black,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "${saved.asRupees()} / ${target.asRupees()}".withInterRupee(),
                style = titlesStyle,
                color = Color.Black,
                fontWeight = FontWeight.SemiBold
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        CustomProgressFillBar(
            progress = progress,
            progressColor = progressColor,
            trackColor = Color(0xFFE0E0E0),
            thickness = 10.dp,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ProjectedImpactScreenPreview() {
    VelvetTheme {
        ProjectedImpactScreen(
            state = UiState.Success(
                ProjectedImpactUiData(
                    goalId = "940a7f46-212e-4c53-a5bd-09399cb2bad2",
                    goalTypeId = 4,
                    goalName = "My New Car",
                    goalTypeName = "Buy a Vehicle",
                    todaysCost = 1_000_000.0,
                    futureValue = 1_276_281.56,
                    currentSavings = 100_000.0,
                    fvCurrentSavings = 161_051.0,
                    netRequiredCorpus = 1_115_230.56,
                    monthlySip = 14_401.77,
                    lumpsumToday = 692_470.43,
                    yearsRemaining = 5,
                    targetYear = 2031,
                    progressPercent = 8,
                    feasibilityScore = 0.13f,
                    increasedBy = 276_281.56,
                    isFixedCorpus = false,
                    holdings = listOf(
                        GoalHoldingDomain(
                            holdingId = "holding-1",
                            fundName = "HDFC Top 100 Fund – Growth",
                            folioNumber = "123456",
                            units = 150.25,
                            nav = 332.8,
                            currentValue = 50_000.0,
                            imageUrl = null
                        )
                    )
                )
            ),
            handleEvent = {}
        )
    }
}
