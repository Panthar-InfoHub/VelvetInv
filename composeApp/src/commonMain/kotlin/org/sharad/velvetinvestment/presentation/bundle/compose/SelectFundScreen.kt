package org.sharad.velvetinvestment.presentation.bundle.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.painterResource
import org.sharad.emify.core.ui.theme.Secondary
import org.sharad.emify.core.ui.theme.appGreen
import org.sharad.emify.core.ui.theme.titleColor
import org.sharad.velvetinvestment.domain.models.bundle.BundleCategoryDomain
import org.sharad.velvetinvestment.domain.models.bundle.FundDomain
import org.sharad.velvetinvestment.domain.models.bundle.PortfolioSlotDomain
import org.sharad.velvetinvestment.presentation.bundle.viewmodel.BundleDetailsViewModel
import org.sharad.velvetinvestment.presentation.onboarding.compose.personaldetails.NextButtonFooter
import org.sharad.velvetinvestment.shared.UiStateContainer
import org.sharad.velvetinvestment.shared.compose.BackHeader
import org.sharad.velvetinvestment.shared.compose.ShadowCard
import org.sharad.velvetinvestment.shared.theme.LocalVelvetShapes
import org.sharad.velvetinvestment.shared.theme.backgroundLight
import org.sharad.velvetinvestment.shared.theme.titlesStyle
import androidx.compose.ui.tooling.preview.Preview
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import org.sharad.emify.core.ui.theme.Primary
import org.sharad.emify.core.ui.theme.lightGray
import org.sharad.velvetinvestment.shared.theme.VelvetTheme
import org.sharad.velvetinvestment.domain.models.bundle.FundMetricsDomain
import org.sharad.velvetinvestment.presentation.mutualfund.compose.MutualFundIcon
import org.sharad.velvetinvestment.shared.dottedBorder
import org.sharad.velvetinvestment.shared.theme.buttonTextStyle
import org.sharad.velvetinvestment.shared.theme.subHeading
import org.sharad.velvetinvestment.utils.toTitleCase
import org.sharad.velvetinvestment.utils.trimTo
import org.sharad.velvetinvestment.utils.withInterRupee
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.icon_warning
import velvet.composeapp.generated.resources.plus_icon

@Composable
fun SelectFundScreenRoot(
    viewModel: BundleDetailsViewModel,
    categoryId: String,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
    onExploreMoreClick: (String, String, String, String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    UiStateContainer(
        uiState = uiState.toUiState(),
        onRetry = { viewModel.loadBundleDetails() }
    ) { bundle ->
        val category = bundle.categories.find { it.id == categoryId }
        if (category != null) {
            SelectFundScreen(
                category = category,
                onBackClick = onBackClick,
                onSaveCategory = { slots ->
                    viewModel.updateSlots(categoryId, slots)
                },
                onFinalSave = onSaveClick,
                onExploreMoreClick = onExploreMoreClick
            )
        }
    }
}

@Composable
fun SelectFundScreen(
    category: BundleCategoryDomain,
    onBackClick: () -> Unit,
    onSaveCategory: (List<PortfolioSlotDomain>) -> Unit,
    onFinalSave: () -> Unit,
    onExploreMoreClick: (String, String, String, String) -> Unit
){
    var pendingSelections by remember(category.id) {
        mutableStateOf<Map<String, FundDomain>>(emptyMap())
    }
    var activeSlotId by remember { mutableStateOf(category.slots.firstOrNull()?.id ?: "") }
    val activeSlot = category.slots.find { it.id == activeSlotId }

    val selectedFundForActiveSlot =
        pendingSelections[activeSlotId]
            ?: activeSlot?.selectedFund

    Column(
    ) {
        BackHeader(
            onBackClick = onBackClick,
            heading = "",
            showBack = true
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                FundCategoryHeader(category)
            }

            item {
                InfoBox("These are category-based fund options. This is not personal investment advice.")
            }

            item {
                SlotsRow(
                    slots = category.slots,
                    activeSlotId = activeSlotId,
                    onSlotClick = { activeSlotId = it }
                )
            }

            if (activeSlot != null) {

                item {
                    Text(
                        text = "PRE-SELECTED OPTION",
                        style = titlesStyle,
                        color = titleColor
                    )
                }
                val originalFundSelectedElsewhere = category.slots
                    .filter { it.id != activeSlotId }
                    .any { slot ->
                        val effectiveFund =
                            pendingSelections[slot.id] ?: slot.selectedFund

                        effectiveFund.id == activeSlot.selectedFund.id
                    }

                item {
                    FundCard(
                        fund = activeSlot.selectedFund,
                        isSelected =
                            selectedFundForActiveSlot?.id == activeSlot.selectedFund.id,
                        isDisabled = originalFundSelectedElsewhere,
                        enabled = !originalFundSelectedElsewhere,
                        onSelect = {
                            pendingSelections =
                                pendingSelections + (
                                        activeSlotId to activeSlot.selectedFund
                                        )
                        }
                    )
                }

                item {
                    Text(
                        text = "SIMILAR FUNDS - SAME SUB-CATEGORY",
                        style = titlesStyle,
                        color = titleColor,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }

                val selectedFundIdsInOtherSlots = category.slots
                    .filter { it.id != activeSlotId }
                    .map { slot ->
                        pendingSelections[slot.id]?.id ?: slot.selectedFund.id
                    }
                    .toSet()
                items(
                    items = category.funds.filter { fund ->
                        fund.id != activeSlot.selectedFund.id &&
                                fund.id !in selectedFundIdsInOtherSlots
                    },
                    key = { it.id }
                ){ fund ->

                    FundCard(
                        fund = fund,
                        isSelected = fund.id == selectedFundForActiveSlot?.id,
                        isDisabled = false,
                        onSelect = {
                            pendingSelections =
                                pendingSelections + (activeSlotId to fund)
                        }
                    )
                }
            }

            item {
                ExploreMoreCard(
                    categoryName = category.displayName,
                    onClick = {
                        onExploreMoreClick(category.id, category.categoryName, activeSlotId, category.displayName)
                    }
                )
            }
        }
        NextButtonFooter(
            onClick = {

                val updatedSlots =
                    category.slots.map { slot ->
                        slot.copy(
                            selectedFund =
                                pendingSelections[slot.id]
                                    ?: slot.selectedFund
                        )
                    }

                onSaveCategory(updatedSlots)
                onFinalSave()
            },
            value = "Save Fund"
        )
    }
}

@Composable
fun FundCategoryHeader(category: BundleCategoryDomain) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Box(Modifier.size(8.dp).clip(CircleShape).background(Color.Black))
            Text(
                text = category.displayName,
                style = MaterialTheme.typography.headlineLarge,
            )
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = "${category.allocationPercentage.toInt()}%",
                style = MaterialTheme.typography.headlineLarge,
                color = Primary
            )
            Text(
                text = "OF PORTFOLIO",
                style = titlesStyle,
                color = Secondary
            )
        }
    }
}

@Composable
fun InfoBox(text: String) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFFFFF7E6)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                painter = painterResource(Res.drawable.icon_warning),
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = titleColor
            )
            Text(
                text = text,
                style = titlesStyle.copy(fontSize = 13.sp),
                color = titleColor
            )
        }
    }
}

@Composable
fun SlotsRow(
    slots: List<PortfolioSlotDomain>,
    activeSlotId: String,
    onSlotClick: (String) -> Unit
) {
    val useWeight = slots.size > 1

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        slots.forEach { slot ->
            val isActive = slot.id == activeSlotId

            ShadowCard(
                modifier = Modifier
                    .then(
                        if (useWeight) Modifier.weight(1f)
                        else Modifier.wrapContentWidth()
                    )
                    .then(
                        if (isActive) {
                            Modifier.border(
                                0.7.dp,
                                Color.Black,
                                LocalVelvetShapes.current.roundedDp12
                            )
                        } else {
                            Modifier
                        }
                    ),
                shape = LocalVelvetShapes.current.roundedDp12,
                clickable = true,
                onClick = {
                    onSlotClick(slot.id)
                }
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "FUND ${slot.rank} • ${slot.allocationPercentage.toInt()}%",
                        style = titlesStyle.copy(
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        color = titleColor.copy(alpha = 0.6f)
                    )

                    Spacer(Modifier.height(4.dp))

                    Text(
                        text = slot.selectedFund.displayName1,
                        style = titlesStyle.copy(fontWeight = FontWeight.SemiBold),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
fun FundCard(
    fund: FundDomain,
    isSelected: Boolean,
    isDisabled: Boolean,
    enabled: Boolean = true,
    onSelect: () -> Unit
) {
    ShadowCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(16.dp),
        clickable = enabled && !isDisabled,
        onClick = {
            if (enabled && !isDisabled) onSelect()
        }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.weight(1f)) {
                        SubcomposeAsyncImage(
                            modifier = Modifier.size(38.dp)
                                .shadow(
                                    elevation = 2.dp,
                                    shape = LocalVelvetShapes.current.roundedDp12
                                )
                                .clip(LocalVelvetShapes.current.roundedDp12)
                                .background(Color.White),
                            model = fund.imageUrl,
                            contentDescription = null,
                            loading = {
                                MutualFundIcon(
                                    schemeName = fund.displayName1,
                                    size = 38.dp
                                )
                            },
                            error = {
                                MutualFundIcon(
                                    schemeName = fund.displayName1,
                                    size = 38.dp
                                )
                            },
                            success = {
                                SubcomposeAsyncImageContent()
                            }
                        )
                    Column {
                        Text(
                            text = fund.schemeName.toTitleCase(),
                            style = MaterialTheme.typography.labelSmall,
                            maxLines = 1
                        )
                        Text(
                            text = fund.amcName,
                            style = titlesStyle.copy(fontSize = 12.sp),
                            color = titleColor.copy(alpha = 0.6f),
                            maxLines = 1
                        )
                    }
                }
                
                if (isSelected) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .border(1.dp, Color(0xff008E23), CircleShape)
                            .background(Color(0xff008E23), CircleShape),
                        contentAlignment = Alignment.Center
                    ){
                        Text(
                            text = "✓",
                            color = Color.White,
                            fontSize = 12.sp
                        )
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .border(1.dp, titleColor.copy(alpha = 0.2f), CircleShape)
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FundTag(fund.assetType, backgroundLight)
                FundTag(fund.riskName, Color(0xFFFFEBEE))
            }

            Spacer(Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                MetricItem("3Y RETURN*", "${fund.metrics.return3Y}%", appGreen)
                MetricItem("Risk Level", fund.riskLevel.toString(), titleColor) // Placeholder expense
                MetricItem("NAV", "₹${ fund.latestNav.toDouble().trimTo(2) }", titleColor) // Placeholder AUM
            }
        }
    }
}

@Composable
fun FundTag(text: String, color: Color) {
    Surface(
        shape = RoundedCornerShape(4.dp),
        color = color
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = titlesStyle.copy(fontSize = 10.sp, fontWeight = FontWeight.Bold)
        )
    }
}

@Composable
fun MetricItem(label: String, value: String, valueColor: Color) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = label, style = titlesStyle.copy(fontSize = 10.sp), color = titleColor)
        Text(text = value.withInterRupee(), style = buttonTextStyle, color = valueColor)
    }
}

@Composable
fun ExploreMoreCard(
    categoryName: String,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth()
            .dottedBorder(
                color = lightGray,
                cornerRadius = 16.dp,
                strokeWidth = 1.dp
            )
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        color = Color.Transparent
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Surface(
                modifier = Modifier.size(36.dp),
                shape = RoundedCornerShape(8.dp),
                color = Primary.copy(0.05f)
            ) {
                Icon(
                    painter = painterResource(Res.drawable.plus_icon),
                    contentDescription = null,
                    modifier = Modifier.padding(8.dp),
                    tint = Primary
                )
            }
            Column {
                Text(
                    text = "Explore more $categoryName Funds",
                    style = subHeading
                )
                Text(
                    text = "Search the full category list with filters",
                    style = titlesStyle.copy(fontSize = 12.sp),
                    color = titleColor
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SelectFundScreenPreview() {
    val sampleFund = FundDomain(
        id = "fund1",
        schemeId = "s1",
        isin = "isin1",
        imageUrl = "",
        mappingCode = "m1",
        schemeName = "Parag Parikh Flexi Cap",
        displayName1 = "Parag Parikh Flexi Cap",
        displayName2 = "Growth",
        amcId = "amc1",
        amcCode = "amcc1",
        amcName = "PPFAS",
        assetType = "Flexi Cap",
        schemeType = "Open Ended",
        structure = "Growth",
        riskName = "Very High risk",
        riskLevel = 5,
        latestNav = "50.5",
        latestNavDate = "2023-10-10",
        purchaseAllowed = true,
        sipAllowed = true,
        redemptionAllowed = true,
        switchAllowed = true,
        maturityDate = null,
        nfoEndDate = null,
        createdAt = "2023-01-01",
        updatedAt = "2023-01-01",
        nseSchemeCode = "NSE123",
        platformCode = "PLAT123",
        metrics = FundMetricsDomain(
            return1Y = 15.0,
            return3Y = 20.2,
            return5Y = 14.0,
            return6M = 7.0,
            return90D = 3.0
        ),
        transactionRules = org.sharad.velvetinvestment.domain.models.bundle.TransactionRulesDomain(
            id = "", mfProductId = "", minSipAmount = 0, minLumpSumAmount = 0, minInvestmentAmount = 0,
            minLumpsumAddOnAmount = 0, minRedemptionQty = 0, minRedemptionAmount = 0,
            minDailySipAmount = 0, minWeeklySipAmount = 0, minFortnightlySipAmount = 0,
            minMonthlySipAmount = 0, minQuarterlySipAmount = 0, minSemiAnnualSipAmount = 0,
            minAnnualSipAmount = 0, sipAllowedDates = emptyList(), sipFrequencies = emptyList(),
            createdAt = "", updatedAt = ""
        )
    )

    val sampleCategory = BundleCategoryDomain(
        id = "cat1",
        categoryName = "Flexi Cap",
        displayName = "Flexi Cap",
        allocationPercentage = 30.0,
        funds = listOf(sampleFund),
        slots = listOf(
            PortfolioSlotDomain(
                id = "slot1",
                allocationPercentage = 15.0,
                rank = 1,
                selectedFund = sampleFund
            ),
            PortfolioSlotDomain(
                id = "slot2",
                allocationPercentage = 15.0,
                rank = 2,
                selectedFund = sampleFund
            )
        )
    )

    VelvetTheme {
        SelectFundScreen(
            category = sampleCategory,
            onBackClick = {},
            onSaveCategory = { },
            onFinalSave = {},
            onExploreMoreClick = { _, _, _ ,_-> }
        )
    }
}
