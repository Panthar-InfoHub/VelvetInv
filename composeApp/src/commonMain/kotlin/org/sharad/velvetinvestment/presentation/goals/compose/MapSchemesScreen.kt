package org.sharad.velvetinvestment.presentation.goals.compose

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.painterResource
import org.sharad.emify.core.ui.theme.Primary
import org.sharad.emify.core.ui.theme.Secondary
import org.sharad.emify.core.ui.theme.appGreen
import org.sharad.emify.core.ui.theme.appRed
import org.sharad.emify.core.ui.theme.titleColor
import org.sharad.velvetinvestment.domain.models.goals.GoalSchemeDomain
import org.sharad.velvetinvestment.presentation.goals.viewmodel.PortfolioSideEffect
import org.sharad.velvetinvestment.presentation.goals.viewmodel.ProjectionImpactViewModel
import org.sharad.velvetinvestment.presentation.goals.viewmodel.SelectableSchemeUiModel
import org.sharad.velvetinvestment.presentation.onboarding.compose.personaldetails.NextButtonFooter
import org.sharad.velvetinvestment.shared.UiStateContainer
import org.sharad.velvetinvestment.shared.compose.AppButton
import org.sharad.velvetinvestment.shared.compose.BackHeader
import org.sharad.velvetinvestment.shared.compose.ErrorScreen
import org.sharad.velvetinvestment.shared.compose.VelvetLoader
import org.sharad.velvetinvestment.shared.genericDropShadow
import org.sharad.velvetinvestment.shared.theme.LocalVelvetShapes
import org.sharad.velvetinvestment.shared.theme.Poppins
import org.sharad.velvetinvestment.shared.theme.subHeading
import org.sharad.velvetinvestment.shared.theme.tinyLabel
import org.sharad.velvetinvestment.shared.theme.titlesStyle
import org.sharad.velvetinvestment.utils.UiState
import org.sharad.velvetinvestment.utils.formatWithCommas
import org.sharad.velvetinvestment.utils.withInterRupee
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.plus_icon

@Composable
fun MapSchemesScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProjectionImpactViewModel
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    UiStateContainer(
        uiState = uiState,
        onRetry = {viewModel.loadGoalDetails()},
    ){
        Scaffold(
            topBar = {
                BackHeader(
                    heading = "Map Schemes",
                    showBack = true,
                    onBackClick = onBack
                )
            },
            bottomBar = {
                NextButtonFooter(
                    onClick = { viewModel.openBottomSheet() },
                    pv = PaddingValues(0.dp),
                    value = if (it.schemes.isEmpty()) "Map Schemes to Goal" else "More funds for maps"
                )
            },
            containerColor = Color.White
        ) { paddingValues ->
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                if (it.schemes.isEmpty()) {
                    MapSchemesEmptyContent(modifier = Modifier.weight(1f), onClick={viewModel.openBottomSheet()})
                } else {
                    MapSchemesFilledContent(
                        mappedSchemes = it.schemes,
                        onRemoveScheme = { viewModel.unMapGoal(it.goalId) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            MapSchemesBottomSheetContent(viewModel = viewModel)
        }
    }
}

@Composable
fun TotalCurrentValueBar(totalValue: Double) {
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
            Text(
                text = "Total Current Value",
                style = titlesStyle,
                color = titleColor
            )
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
                .clickable(onClick=onClick),
            contentAlignment = Alignment.Center
        ){
        Box(
            modifier = Modifier
                .padding(20.dp)
                .background(Color.White, CircleShape)
                .border(1.dp,Color(0xffCBDBF5).copy(0.3f), CircleShape),
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
        ){
            Text(
                text = "No schemes mapped yet",
                style = MaterialTheme.typography.titleLarge.copy(
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

@Composable
fun MapSchemesFilledContent(
    mappedSchemes: List<GoalSchemeDomain>,
    onRemoveScheme: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item{
            TotalCurrentValueBar(
                totalValue = mappedSchemes.sumOf { it.currentVal.toDoubleOrNull() ?: 0.0 }
            )
        }
        items(mappedSchemes) { scheme ->
            MappedSchemeCard(
                scheme = scheme,
            )
        }
        item{
            Text(
                text= "Remove Mapping",
                modifier = Modifier.fillMaxWidth()
                    .clickable(
                        onClick = { onRemoveScheme() },
                    ),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineSmall,
                color = appRed
            )
        }
    }
}

@Composable
fun MappedSchemeCard(
    scheme: GoalSchemeDomain,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .genericDropShadow(LocalVelvetShapes.current.roundedDp12)
            .clip(LocalVelvetShapes.current.roundedDp12)
            .border(1.dp, Color.LightGray.copy(alpha = 0.3f), LocalVelvetShapes.current.roundedDp12)
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
                    text = scheme.schemeName,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                    ),
                    modifier = Modifier.weight(1f)
                )
//                Icon(
//                    painter = painterResource(Res.drawable.ic_delete),
//                    contentDescription = "Remove",
//                    modifier = Modifier
//                        .padding(start = 20.dp, top = 4.dp)
//                        .size(20.dp)
//                        .clickable(
//                            onClick = onRemove,
//                            indication = null,
//                            interactionSource = remember { MutableInteractionSource() }
//                        ),
//                    tint = redColor
//                )
            }

            Row(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "FOLIO", style = tinyLabel)
                    Text(
                        text = scheme.folio,
                        style = subHeading,
                        color = Primary
                    )
                }
                Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.End) {
                    Text(text = "UNITS", style = tinyLabel)
                    Text(
                        text = scheme.balUnits,
                        style = subHeading,
                        color = Primary
                    )
                }
            }

            Row(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "NAV",  style = tinyLabel)
                    Text(
                        text = "₹${scheme.nav}".withInterRupee(),
                        style = subHeading,
                        color = Primary
                    )
                }
                Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.End) {
                    Text(text = "CURRENT VALUE",  style = tinyLabel)
                    Text(
                        text = "₹${formatWithCommas(scheme.currentVal.toDoubleOrNull()?.toLong() ?: 0L)}".withInterRupee(),
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
    viewModel: ProjectionImpactViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.portfolioData.collectAsStateWithLifecycle()
    val sheetState = rememberModalBottomSheetState()
    var showSheet by rememberSaveable{ mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.projectionSideEffect.collect { effect ->
            when (effect) {
                PortfolioSideEffect.OpenBottomSheet -> {
                    showSheet = true
                }
                PortfolioSideEffect.CloseBottomSheet -> {
                    showSheet=false
                }
            }
        }
    }


    if (showSheet){
        ModalBottomSheet(
            onDismissRequest = {
                viewModel.closeBottomSheet()
            },
            sheetState = sheetState,
            containerColor = Color.White
        ) {
            when(uiState){
                is UiState.Error -> {
                    ErrorScreen((uiState as UiState.Error).message, onRetryClick = {viewModel.loadPortfolio()})
                }
                UiState.Loading -> {
                    Box(modifier= Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center){
                        VelvetLoader()
                    }
                }
                is UiState.Success -> {
                    val data = (uiState as UiState.Success).data
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
                            items(data) { scheme ->
                                SelectableSchemeItem(
                                    scheme = scheme,
                                    onToggle = { viewModel.toggleSelection(scheme.folio) }
                                )
                            }
                        }

                        if (data.isEmpty()){
                            Text(
                                text = "Purchase Funds to map them with goals",
                                style = titlesStyle,
                                color = titleColor,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth().padding(vertical =  24.dp)
                            )

                        }

                        Spacer(modifier = Modifier.height(24.dp))
                        AppButton(
                            onClick = { viewModel.mapGoal() },
                            text = "Confirm Selection",
                            enabled = data.any { it.isSelected }
                        )
                    }
                }
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
            .background(
                if (scheme.isSelected) Color(0xffEFF6FF) else Color.White,
            )
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
