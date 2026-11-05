package org.sharad.velvetinvestment.features.bundles.presentation.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import org.sharad.emify.core.ui.theme.Primary
import org.sharad.emify.core.ui.theme.Secondary
import org.sharad.emify.core.ui.theme.appRed
import org.sharad.emify.core.ui.theme.backgroundGray
import org.sharad.emify.core.ui.theme.lightGray
import org.sharad.emify.core.ui.theme.titleColor
import org.sharad.velvetinvestment.features.bundles.domain.models.AssetAllocationDomain
import org.sharad.velvetinvestment.features.bundles.domain.models.BundleCategoryDomain
import org.sharad.velvetinvestment.features.bundles.domain.models.BundleDetailsDomain
import org.sharad.velvetinvestment.features.bundles.domain.models.BundleMetaDataDomain
import org.sharad.velvetinvestment.features.bundles.domain.models.FundDomain
import org.sharad.velvetinvestment.features.bundles.domain.models.FundMetricsDomain
import org.sharad.velvetinvestment.features.bundles.domain.models.PortfolioSlotDomain
import org.sharad.velvetinvestment.features.bundles.domain.models.supports
import org.sharad.velvetinvestment.features.bundles.domain.models.unavailableNote
import org.sharad.velvetinvestment.features.bundles.presentation.viewmodel.BundleDetailsEvent
import org.sharad.velvetinvestment.features.bundles.presentation.viewmodel.BundleDetailsUiState
import org.sharad.velvetinvestment.features.bundles.presentation.viewmodel.FundSelectionState
import org.sharad.velvetinvestment.features.bundles.presentation.viewmodel.activeSlot
import org.sharad.velvetinvestment.features.bundles.presentation.viewmodel.categoryFor
import org.sharad.velvetinvestment.features.bundles.presentation.viewmodel.effectiveFund
import org.sharad.velvetinvestment.features.core.domain.models.PurchaseMode
import org.sharad.velvetinvestment.shared.compose.NextButtonFooter
import org.sharad.velvetinvestment.shared.compose.BackHeader
import org.sharad.velvetinvestment.shared.compose.ErrorScreen
import org.sharad.velvetinvestment.shared.compose.FundIcon
import org.sharad.velvetinvestment.shared.compose.LoaderScreen
import org.sharad.velvetinvestment.shared.compose.ShadowCard
import org.sharad.velvetinvestment.shared.dottedBorder
import org.sharad.velvetinvestment.shared.theme.LocalVelvetShapes
import org.sharad.velvetinvestment.shared.theme.VelvetTheme
import org.sharad.velvetinvestment.shared.theme.subHeading
import org.sharad.velvetinvestment.shared.theme.tinyLabel
import org.sharad.velvetinvestment.shared.theme.titlesStyle
import org.sharad.velvetinvestment.utils.trimTo
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.icon_warning
import velvet.composeapp.generated.resources.ic_pointer_right
import velvet.composeapp.generated.resources.plus_icon
import velvet.composeapp.generated.resources.tick_icon

@Composable
fun SelectFundScreen(
    state: BundleDetailsUiState,
    onEvent: (BundleDetailsEvent) -> Unit
) {
    val selection = state.fundSelection
    val category = selection?.let { state.categoryFor(it) }

    Scaffold(containerColor = Color.White) {
        Box(modifier = Modifier.fillMaxSize()) {
            when {
                selection != null && category != null -> Column(modifier = Modifier.fillMaxSize()) {
                    BackHeader(
                        title = "Select Fund",
                        showBack = true,
                        onBack = { onEvent(BundleDetailsEvent.OnFundSelectionBackClicked) }
                    )
                    SelectFundContent(
                        category = category,
                        selection = selection,
                        purchaseMode = state.purchaseMode,
                        onEvent = onEvent,
                        modifier = Modifier.weight(1f)
                    )
                    NextButtonFooter(
                        onClick = { onEvent(BundleDetailsEvent.OnSaveFundsClicked) },
                        value = "Save Fund",
                        enabled = category.slots.all { selection.effectiveFund(it) != null }
                    )
                }

                state.bundle == null && state.error != null -> ErrorScreen(
                    errorMessage = state.error,
                    onRetryClick = { onEvent(BundleDetailsEvent.Retry) }
                )

                else -> LoaderScreen()
            }
        }
    }
}

@Composable
private fun SelectFundContent(
    category: BundleCategoryDomain,
    selection: FundSelectionState,
    purchaseMode: PurchaseMode,
    onEvent: (BundleDetailsEvent) -> Unit,
    modifier: Modifier
) {
    // Funds that can't be bought as the selected mode stay listed but can't be picked.
    val unavailableNote = purchaseMode.unavailableNote()
    val activeSlot = category.activeSlot(selection)
    val otherSlots = category.slots.filter { it.id != activeSlot?.id }
    val fundIdsInOtherSlots = otherSlots.mapNotNull { selection.effectiveFund(it)?.id }.toSet()
    val selectedFund = activeSlot?.let { selection.effectiveFund(it) }
    val defaultFund = activeSlot?.defaultFund
    // A pick that differs from the recommendation gets its own section above it; while the
    // recommendation is the pick, the recommended card alone shows it as selected.
    val showSelectedSection = selectedFund != null && selectedFund.id != defaultFund?.id
    val otherFunds = category.funds.filter { fund ->
        fund.id != defaultFund?.id && fund.id != selectedFund?.id && fund.id !in fundIdsInOtherSlots
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { FundCategoryHeader(category) }

        item { InfoBox("These are category-based fund options. This is not personal investment advice.") }

        item {
            SlotsRow(
                slots = category.slots,
                selection = selection,
                activeSlotId = activeSlot?.id,
                purchaseMode = purchaseMode,
                onSlotClick = { onEvent(BundleDetailsEvent.OnSlotSelected(it)) }
            )
        }

        if (activeSlot != null) {
            if (showSelectedSection) {
                item {
                    FundSectionHeader(
                        title = "Currently selected",
                        subtitle = "Your pick for Fund ${activeSlot.rank} in ${category.displayName}"
                    )
                }
                item {
                    // Still shown when it can't be bought this way, so the user sees what to replace.
                    FeaturedFundCard(
                        fund = selectedFund,
                        isSelected = true,
                        onSelect = {},
                        unavailableReason = unavailableNote.takeUnless { selectedFund.supports(purchaseMode) }
                    )
                }
            }

            item {
                FundSectionHeader(
                    title = "Recommended for you",
                    subtitle = "Popular and well-performing funds in ${category.displayName} category",
                    modifier = if (showSelectedSection) Modifier.padding(top = 8.dp) else Modifier
                )
            }

            item {
                if (defaultFund == null) {
                    Text(
                        text = "This slot has no default fund",
                        style = titlesStyle.copy(fontSize = 13.sp),
                        color = titleColor.copy(alpha = 0.6f)
                    )
                } else {
                    // The default can be picked again at any time, unless another slot of this
                    // category is holding it.
                    val takenElsewhere = defaultFund.id in fundIdsInOtherSlots
                    val isSupported = defaultFund.supports(purchaseMode)
                    val isSelected = selectedFund?.id == defaultFund.id
                    FeaturedFundCard(
                        fund = defaultFund,
                        isSelected = isSelected,
                        enabled = !takenElsewhere && (isSupported || isSelected),
                        onSelect = { onEvent(BundleDetailsEvent.OnFundPicked(defaultFund)) },
                        unavailableReason = unavailableNote.takeUnless { isSupported }
                    )
                }
            }

            item {
                FundSectionHeader(
                    title = "Other funds in ${category.displayName}",
                    subtitle = "You can choose any one fund from this category.",
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            items(items = otherFunds, key = { it.id }) { fund ->
                val isSupported = fund.supports(purchaseMode)
                FundOptionCard(
                    name = fund.displayName(),
                    subtitle = if (isSupported) fund.subtitle() else unavailableNote,
                    subtitleColor = if (isSupported) titleColor else appRed,
                    enabled = isSupported,
                    iconUrl = fund.imageUrl,
                    return1Y = fund.metrics.return1Y,
                    return3Y = fund.metrics.return3Y,
                    return5Y = fund.metrics.return5Y,
                    onSelect = { onEvent(BundleDetailsEvent.OnFundPicked(fund)) }
                )
            }

            item {
                ExploreMoreCard(
                    categoryName = category.displayName,
                    onClick = { onEvent(BundleDetailsEvent.OnExploreMoreClicked) }
                )
            }
        }
    }
}

@Composable
private fun ExploreMoreCard(
    categoryName: String,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(16.dp)
    Row(
        modifier = Modifier.fillMaxWidth()
            .dottedBorder(color = lightGray, cornerRadius = 16.dp, strokeWidth = 1.dp)
            .clip(shape)
            .clickable(onClick = onClick)
            .padding(16.dp),
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
        Column(modifier = Modifier.weight(1f)) {
            Text(text = "Explore more $categoryName funds", style = subHeading)
            Text(
                text = "View the full category list with filters",
                style = titlesStyle.copy(fontSize = 12.sp),
                color = titleColor
            )
        }
        Icon(
            painter = painterResource(Res.drawable.ic_pointer_right),
            contentDescription = null,
            modifier = Modifier.size(width = 8.dp, height = 16.dp),
            tint = titleColor
        )
    }
}

@Composable
private fun FundCategoryHeader(category: BundleCategoryDomain) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = category.displayName,
                style = MaterialTheme.typography.headlineLarge
            )
            Text(
                text = "Select a fund for the ${category.allocationPercentage} allocation",
                style = MaterialTheme.typography.displaySmall,
                color = titleColor
            )
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = "${category.allocationPercentage.toInt()}%",
                style = MaterialTheme.typography.headlineLarge,
                color = Primary
            )
            Text(text = "OF PORTFOLIO", style = titlesStyle, color = Secondary)
        }
    }
}

@Composable
internal fun InfoBox(text: String) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFFFFF7E6)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                painter = painterResource(Res.drawable.icon_warning),
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = titleColor
            )
            Text(text = text, style = titlesStyle.copy(fontSize = 13.sp), color = titleColor)
        }
    }
}

@Composable
private fun SlotsRow(
    slots: List<PortfolioSlotDomain>,
    selection: FundSelectionState,
    activeSlotId: String?,
    purchaseMode: PurchaseMode,
    onSlotClick: (String) -> Unit
) {
    val useWeight = slots.size > 1
    val shape = LocalVelvetShapes.current.roundedDp12

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        slots.forEach { slot ->
            val isActive = slot.id == activeSlotId
            // A slot holding a fund that can't be bought as the selected mode is flagged in red.
            val isUnsupported = selection.effectiveFund(slot)?.supports(purchaseMode) == false
            val outline = when {
                isUnsupported -> appRed
                isActive -> Color.Black
                else -> null
            }

            ShadowCard(
                modifier = Modifier
                    .then(if (useWeight) Modifier.weight(1f) else Modifier.wrapContentWidth())
                    .then(
                        if (outline != null) {
                            Modifier.border(if (isUnsupported) 1.dp else 0.7.dp, outline, shape)
                        } else Modifier
                    ),
                shape = shape,
                clickable = true,
                onClick = { onSlotClick(slot.id) },
                contentAlignment = Alignment.CenterStart
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "FUND ${slot.rank} • ${slot.allocationPercentage.toInt()}%",
                        style = titlesStyle.copy(fontSize = 10.sp, fontWeight = FontWeight.Bold),
                        color = titleColor.copy(alpha = 0.6f)
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = selection.effectiveFund(slot)?.name ?: "--",
                        style = titlesStyle.copy(fontWeight = FontWeight.SemiBold),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

/**
 * The large card for the recommended fund and for the user's own pick: full name, and the three
 * returns spread across the bottom. A selected card is outlined and badged.
 */
@Composable
private fun FeaturedFundCard(
    fund: FundDomain,
    isSelected: Boolean,
    onSelect: () -> Unit,
    enabled: Boolean = true,
    /** Why the fund can't be bought as the selected mode; shown in red in place of its subtitle. */
    unavailableReason: String? = null
) {
    val shape = RoundedCornerShape(16.dp)
    ShadowCard(
        modifier = Modifier.fillMaxWidth()
            .alpha(if (enabled) 1f else 0.5f)
            .then(
                when {
                    unavailableReason != null && isSelected -> Modifier.border(1.dp, appRed, shape)
                    isSelected -> Modifier.border(1.dp, SelectedFundBorder, shape)
                    else -> Modifier
                }
            ),
        shape = shape,
        clickable = enabled && !isSelected,
        onClick = onSelect,
        contentAlignment = Alignment.CenterStart
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.Top
            ) {
                FundIcon(iconUrl = fund.imageUrl, name = fund.name)

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = fund.displayName(),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Black,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = unavailableReason ?: fund.subtitle(),
                        style = titlesStyle.copy(fontSize = 12.sp),
                        color = if (unavailableReason != null) appRed else titleColor.copy(alpha = 0.6f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                if (isSelected) CurrentlySelectedPill() else SelectionIndicator(isSelected = false)
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 16.dp),
                thickness = 0.7.dp,
                color = backgroundGray
            )

            Row(modifier = Modifier.fillMaxWidth()) {
                FeaturedMetric("1Y RETURN", fund.metrics.return1Y, Modifier.weight(1f))
                FeaturedMetric("3Y RETURN", fund.metrics.return3Y, Modifier.weight(1f), highlight = true)
                FeaturedMetric("5Y RETURN", fund.metrics.return5Y, Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun CurrentlySelectedPill() {
    Row(
        modifier = Modifier
            .clip(CircleShape)
            .background(SelectedFundPillBackground)
            .padding(horizontal = 10.dp, vertical = 5.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(14.dp).background(SelectedFundAccent, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(Res.drawable.tick_icon),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.fillMaxSize().padding(2.dp)
            )
        }
        Text(
            text = "Currently Selected",
            style = tinyLabel.copy(fontWeight = FontWeight.Medium),
            color = SelectedFundAccent
        )
    }
}

@Composable
private fun FeaturedMetric(
    label: String,
    value: Double,
    modifier: Modifier = Modifier,
    highlight: Boolean = false
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            style = titlesStyle.copy(fontSize = 10.sp, letterSpacing = 0.5.sp),
            color = titleColor.copy(alpha = 0.7f)
        )
        Spacer(Modifier.height(2.dp))
        Text(
            text = "${value.trimTo(1)}%",
            style = subHeading.copy(fontSize = 15.sp, fontWeight = FontWeight.Bold),
            color = returnColor(value, highlight)
        )
    }
}

private fun FundDomain.displayName(): String = fundDisplayName(name)

// Only funds fetched on their own know their AMC.
private fun FundDomain.subtitle(): String =
    fundPlanLabel(name) ?: amcName.ifBlank { "NAV ₹${latestNav}" }

internal val previewFund = FundDomain(
    id = "fund1",
    name = "Invesco India Midcap Fund - Regular Plan - Growth Option",
    isin = "INF205K01BC9",
    imageUrl = "",
    latestNav = "197.13",
    latestNavDate = "2026-09-18T00:00:00.000Z",
    metrics = FundMetricsDomain(
        return1M = -1.302,
        return1Y = 6.759,
        return3Y = 21.944,
        return5Y = 18.167,
        return6M = 17.193,
        return90D = 2.549
    )
)

private fun previewFlexiFund(id: String, name: String, return1Y: Double, return3Y: Double, return5Y: Double) =
    previewFund.copy(
        id = id,
        name = name,
        metrics = previewFund.metrics.copy(return1Y = return1Y, return3Y = return3Y, return5Y = return5Y)
    )

@Preview(heightDp = 1400)
@Composable
private fun SelectFundScreenPreview() {
    val ppfas = previewFlexiFund("ppfas", "Parag Parikh Flexi Cap Fund - Direct Plan - Growth", 15.2, 21.7, 19.2)
    val absl = previewFlexiFund("absl", "Aditya Birla Sun Life Flexi Cap Fund - Direct Plan - Growth", 11.4, 17.3, 15.1)
    val hdfc = previewFlexiFund("hdfc", "HDFC Flexi Cap Fund - Direct Plan - Growth", 14.1, 19.8, 17.6)
    val category = BundleCategoryDomain(
        id = "flexi",
        categoryName = "flexi_cap",
        displayName = "Flexi Cap",
        allocationPercentage = 30.0,
        funds = listOf(ppfas, absl, hdfc),
        slots = listOf(
            PortfolioSlotDomain("slot1", 15.0, 1, ppfas, ppfas),
            PortfolioSlotDomain("slot2", 15.0, 2, absl, absl)
        )
    )

    VelvetTheme {
        SelectFundScreen(
            state = BundleDetailsUiState(
                bundle = BundleDetailsDomain(
                    name = "Aggressive",
                    description = "",
                    assetAllocation = AssetAllocationDomain(95.0, 0.0, 0.0, 5.0),
                    metaData = BundleMetaDataDomain("", "", ""),
                    categories = listOf(category)
                ),
                fundSelection = FundSelectionState(
                    categoryId = "flexi",
                    activeSlotId = "slot1",
                    pendingSelections = mapOf("slot1" to hdfc)
                )
            ),
            onEvent = {}
        )
    }
}
