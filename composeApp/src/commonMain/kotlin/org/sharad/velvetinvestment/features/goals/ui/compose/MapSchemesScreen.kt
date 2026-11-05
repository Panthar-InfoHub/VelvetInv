package org.sharad.velvetinvestment.features.goals.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import org.jetbrains.compose.resources.painterResource
import org.sharad.emify.core.ui.theme.Primary
import org.sharad.emify.core.ui.theme.Secondary
import org.sharad.emify.core.ui.theme.appGreen
import org.sharad.emify.core.ui.theme.redColor
import org.sharad.emify.core.ui.theme.titleColor
import org.sharad.velvetinvestment.features.goals.domain.models.GoalHoldingDomain
import org.sharad.velvetinvestment.features.goals.ui.viewmodels.MapSchemeEffect
import org.sharad.velvetinvestment.features.goals.ui.viewmodels.MapSchemeEvent
import org.sharad.velvetinvestment.features.goals.ui.viewmodels.MapSchemeUiData
import org.sharad.velvetinvestment.features.goals.ui.viewmodels.MapSchemeUiState
import org.sharad.velvetinvestment.features.goals.ui.viewmodels.SelectableSchemeUiModel
import org.sharad.velvetinvestment.shared.UiStateContainer
import org.sharad.velvetinvestment.shared.compose.AppButton
import org.sharad.velvetinvestment.shared.compose.BackHeader
import org.sharad.velvetinvestment.shared.compose.ErrorScreen
import org.sharad.velvetinvestment.shared.compose.NextButtonFooter
import org.sharad.velvetinvestment.shared.compose.VelvetLoader
import org.sharad.velvetinvestment.shared.genericDropShadow
import org.sharad.velvetinvestment.shared.theme.LocalVelvetShapes
import org.sharad.velvetinvestment.shared.theme.Poppins
import org.sharad.velvetinvestment.shared.theme.VelvetTheme
import org.sharad.velvetinvestment.shared.theme.subHeading
import org.sharad.velvetinvestment.shared.theme.tinyLabel
import org.sharad.velvetinvestment.shared.theme.titlesStyle
import org.sharad.velvetinvestment.utils.UiState
import org.sharad.velvetinvestment.utils.formatWithCommas
import org.sharad.velvetinvestment.utils.trimTo
import org.sharad.velvetinvestment.utils.withInterRupee
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.ic_delete
import velvet.composeapp.generated.resources.plus_icon

/**
 * The holdings mapped to one goal (`GET /user-goal/{id}`), with a sheet to map more of the user's
 * own funds (`POST /user-goal/map`) and a per-fund remove (`POST /user-goal/remove`).
 */
@Composable
fun MapSchemesScreen(
    uiState: MapSchemeUiState,
    effectFlow: Flow<MapSchemeEffect>,
    onEvent: (MapSchemeEvent) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    UiStateContainer(
        uiState = uiState.goalDetailsState,
        onRetry = { onEvent(MapSchemeEvent.RetryGoalDetails) },
    ) { goalData ->
        Scaffold(
            topBar = {
                BackHeader(
                    title = "Map Schemes",
                    showBack = true,
                    onBack = onBack
                )
            },
            bottomBar = {
                NextButtonFooter(
                    onClick = { onEvent(MapSchemeEvent.OpenBottomSheet) },
                    value = if (goalData.holdings.isEmpty()) "Map Schemes to Goal" else "More funds for maps"
                )
            },
            containerColor = Color.White
        ) { paddingValues ->
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                if (goalData.holdings.isEmpty()) {
                    MapSchemesEmptyContent(
                        modifier = Modifier.weight(1f),
                        onClick = { onEvent(MapSchemeEvent.OpenBottomSheet) }
                    )
                } else {
                    MappedHoldingsContent(
                        goalData = goalData,
                        removingHoldingIds = uiState.removingHoldingIds,
                        onRemoveHolding = { onEvent(MapSchemeEvent.RemoveHolding(it)) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            MapSchemesBottomSheetContent(
                portfolioState = uiState.portfolioDataState,
                mapping = uiState.mapping,
                effectFlow = effectFlow,
                onEvent = onEvent
            )
        }
    }
}

@Composable
fun MappedHoldingsContent(
    goalData: MapSchemeUiData,
    removingHoldingIds: Set<String>,
    onRemoveHolding: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            TotalCurrentValueBar(
                totalValue = goalData.totalCurrentValue,
                mappedCount = goalData.holdings.size
            )
        }
        items(items = goalData.holdings, key = { it.holdingId }) { holding ->
            MappedHoldingCard(
                holding = holding,
                removing = holding.holdingId in removingHoldingIds,
                onRemove = { onRemoveHolding(holding.holdingId) }
            )
        }
    }
}

@Composable
fun TotalCurrentValueBar(totalValue: Double, mappedCount: Int) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = Color.LightGray.copy(alpha = 0.5f),
                shape = LocalVelvetShapes.current.roundedDp8
            )
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "Total Current Value",
                    style = titlesStyle,
                    color = titleColor
                )
                Text(
                    text = "$mappedCount Mapped",
                    style = tinyLabel,
                    color = appGreen,
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(appGreen.copy(alpha = 0.12f))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                )
            }
            Text(
                text = "₹${formatWithCommas(totalValue.toLong())}".withInterRupee(),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = Poppins,
                color = appGreen
            )
        }
    }
}

@Composable
fun MapSchemesEmptyContent(modifier: Modifier = Modifier, onClick: () -> Unit) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(Color(0xffEFF4FF), CircleShape)
                .border(1.dp, Color(0xffCBDBF5).copy(0.3f), CircleShape)
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .padding(20.dp)
                    .background(Color.White, CircleShape)
                    .border(1.dp, Color(0xffCBDBF5).copy(0.3f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(Res.drawable.plus_icon),
                    contentDescription = null,
                    modifier = Modifier.padding(16.dp).size(32.dp),
                    tint = Primary
                )
            }
        }
        Column(
            modifier = Modifier.padding(top = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "No schemes mapped yet",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = Primary
                ),
                textAlign = TextAlign.Center
            )
            Text(
                text = "Link your investments to track progress towards this goal",
                style = titlesStyle,
                color = titleColor,
                textAlign = TextAlign.Center
            )
        }
    }
}

/**
 * One mapped fund. [showDelete] is off where the card is only read (the goal's own screen); on
 * the mapping screen removing swaps the icon for a loader so the same row cannot go twice.
 */
@Composable
fun MappedHoldingCard(
    holding: GoalHoldingDomain,
    removing: Boolean,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier,
    showDelete: Boolean = true
) {
    val shape = LocalVelvetShapes.current.roundedDp12
    Box(
        modifier = modifier
            .fillMaxWidth()
            .genericDropShadow(shape)
            .clip(shape)
            .border(1.dp, Color.LightGray.copy(alpha = 0.3f), shape)
            .background(Color.White)
            .padding(20.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = holding.fundName,
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.weight(1f)
                )
                if (showDelete) {
                    Box(
                        modifier = Modifier.padding(start = 20.dp, top = 4.dp).size(20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if (removing) {
                            CircularProgressIndicator(
                                strokeWidth = 2.dp,
                                color = Primary,
                                modifier = Modifier.size(16.dp)
                            )
                        } else {
                            Icon(
                                painter = painterResource(Res.drawable.ic_delete),
                                contentDescription = "Remove ${holding.fundName}",
                                modifier = Modifier
                                    .size(20.dp)
                                    .clickable(
                                        onClick = onRemove,
                                        indication = null,
                                        interactionSource = remember { MutableInteractionSource() }
                                    ),
                                tint = redColor
                            )
                        }
                    }
                }
            }

            Row(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "FOLIO", style = tinyLabel)
                    Text(text = holding.folioNumber, style = subHeading, color = Primary)
                }
                Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.End) {
                    Text(text = "UNITS", style = tinyLabel)
                    Text(text = holding.units.trimTo(2), style = subHeading, color = Primary)
                }
            }

            Row(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "NAV", style = tinyLabel)
                    Text(
                        text = "₹${holding.nav.trimTo(2)}".withInterRupee(),
                        style = subHeading,
                        color = Primary
                    )
                }
                Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.End) {
                    Text(text = "CURRENT VALUE", style = tinyLabel)
                    Text(
                        text = "₹${formatWithCommas(holding.currentValue.toLong())}".withInterRupee(),
                        style = subHeading,
                        color = appGreen
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapSchemesBottomSheetContent(
    portfolioState: UiState<List<SelectableSchemeUiModel>>,
    mapping: Boolean,
    effectFlow: Flow<MapSchemeEffect>,
    onEvent: (MapSchemeEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    var showSheet by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        effectFlow.collect { effect ->
            when (effect) {
                MapSchemeEffect.OpenBottomSheet -> showSheet = true
                MapSchemeEffect.CloseBottomSheet -> showSheet = false
            }
        }
    }

    if (showSheet) {
        val sheetState = rememberModalBottomSheetState()
        ModalBottomSheet(
            onDismissRequest = { onEvent(MapSchemeEvent.CloseBottomSheet) },
            sheetState = sheetState,
            containerColor = Color.White
        ) {
            MapSchemesSheetBody(
                portfolioState = portfolioState,
                mapping = mapping,
                onEvent = onEvent,
                modifier = modifier
            )
        }
    }
}

@Composable
fun MapSchemesSheetBody(
    portfolioState: UiState<List<SelectableSchemeUiModel>>,
    onEvent: (MapSchemeEvent) -> Unit,
    modifier: Modifier = Modifier,
    mapping: Boolean = false
) {
    when (portfolioState) {
        is UiState.Error -> ErrorScreen(
            portfolioState.message,
            onRetryClick = { onEvent(MapSchemeEvent.RetryPortfolio) }
        )

        UiState.Loading -> Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            VelvetLoader()
        }

        is UiState.Success -> {
            val data = portfolioState.data
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .background(Color.White, LocalVelvetShapes.current.roundedDp15)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Map Schemes",
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = "Choose the fund where you have invested to set a goal",
                    style = titlesStyle,
                    color = titleColor,
                    modifier = Modifier.fillMaxWidth().padding(top = 4.dp, bottom = 24.dp)
                )

                LazyColumn(
                    modifier = Modifier.weight(1f, fill = false),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(data, key = { it.holdingId }) { scheme ->
                        SelectableSchemeItem(
                            scheme = scheme,
                            onToggle = { onEvent(MapSchemeEvent.ToggleSelection(scheme.holdingId)) }
                        )
                    }
                }

                if (data.isEmpty()) {
                    Text(
                        text = "Purchase Funds to map them with goals",
                        style = titlesStyle,
                        color = titleColor,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
                AppButton(
                    onClick = { onEvent(MapSchemeEvent.MapSelectedHoldings) },
                    text = "Confirm Selection",
                    enabled = data.any { it.isSelected },
                    loading = mapping
                )
            }
        }
    }
}

@Composable
fun SelectableSchemeItem(
    scheme: SelectableSchemeUiModel,
    onToggle: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = Color.LightGray.copy(alpha = 0.5f),
                shape = LocalVelvetShapes.current.roundedDp12
            )
            .clip(LocalVelvetShapes.current.roundedDp12)
            .background(if (scheme.isSelected) Color(0xffEFF6FF) else Color.White)
            .clickable(
                onClick = onToggle,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = scheme.name,
                style = titlesStyle.copy(fontWeight = FontWeight.SemiBold)
            )
            Text(
                text = ("Units: ${scheme.units} | Value: ₹${formatWithCommas(scheme.value.toLong())}").withInterRupee(),
                style = MaterialTheme.typography.displaySmall,
                color = titleColor
            )
        }
        Checkbox(
            checked = scheme.isSelected,
            onCheckedChange = { onToggle() },
            colors = CheckboxDefaults.colors(
                checkedColor = Secondary,
                uncheckedColor = Color.LightGray.copy(alpha = 0.5f)
            )
        )
    }
}

private val previewHoldings = listOf(
    GoalHoldingDomain(
        holdingId = "holding-1",
        fundName = "HDFC Top 100 Fund – Growth",
        folioNumber = "123456",
        units = 150.25,
        nav = 332.8,
        currentValue = 50_000.0,
        imageUrl = null
    ),
    GoalHoldingDomain(
        holdingId = "holding-2",
        fundName = "Parag Parikh Flexi Cap Fund",
        folioNumber = "441092",
        units = 42.10,
        nav = 712.5,
        currentValue = 30_000.0,
        imageUrl = null
    )
)

@Preview
@Composable
private fun MapSchemesScreenPreview() {
    VelvetTheme {
        MapSchemesScreen(
            uiState = MapSchemeUiState(
                goalDetailsState = UiState.Success(
                    MapSchemeUiData(
                        goalId = "goal-1",
                        goalName = "Aarav's Education",
                        holdings = previewHoldings,
                        totalCurrentValue = 80_000.0
                    )
                ),
                removingHoldingIds = setOf("holding-2")
            ),
            effectFlow = emptyFlow(),
            onEvent = {},
            onBack = {}
        )
    }
}

@Preview
@Composable
private fun MapSchemesSheetPreview() {
    VelvetTheme {
        MapSchemesSheetBody(
            portfolioState = UiState.Success(
                listOf(
                    SelectableSchemeUiModel(
                        holdingId = "holding-4",
                        name = "SBI Bluechip Fund - Direct Growth",
                        units = "150.5432",
                        value = 68620.0,
                        folio = "123456789",
                        isSelected = true
                    ),
                    SelectableSchemeUiModel(
                        holdingId = "holding-5",
                        name = "HDFC Mid-Cap Opportunities Fund - Direct Growth",
                        units = "82.1145",
                        value = 124350.0,
                        folio = "987654321"
                    )
                )
            ),
            onEvent = {}
        )
    }
}
