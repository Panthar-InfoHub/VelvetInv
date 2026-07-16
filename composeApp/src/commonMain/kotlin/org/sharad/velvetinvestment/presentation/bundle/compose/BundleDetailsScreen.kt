package org.sharad.velvetinvestment.presentation.bundle.compose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.painterResource
import org.sharad.emify.core.ui.theme.Primary
import org.sharad.emify.core.ui.theme.Secondary
import org.sharad.emify.core.ui.theme.appGreen
import org.sharad.emify.core.ui.theme.backgroundGray
import org.sharad.emify.core.ui.theme.titleColor
import org.sharad.velvetinvestment.domain.models.bundle.AssetAllocationDomain
import org.sharad.velvetinvestment.domain.models.bundle.BundleCategoryDomain
import org.sharad.velvetinvestment.domain.models.bundle.BundleDomain
import org.sharad.velvetinvestment.domain.models.bundle.BundleMetaDataDomain
import org.sharad.velvetinvestment.domain.models.bundle.FundDomain
import org.sharad.velvetinvestment.domain.models.bundle.FundMetricsDomain
import org.sharad.velvetinvestment.domain.models.bundle.TransactionRulesDomain
import org.sharad.velvetinvestment.presentation.bundle.viewmodel.BundleDetailsState
import org.sharad.velvetinvestment.presentation.bundle.viewmodel.BundleDetailsViewModel
import org.sharad.velvetinvestment.presentation.bundle.viewmodel.BundleTransactionRules
import org.sharad.velvetinvestment.presentation.onboarding.compose.personaldetails.NextButtonFooter
import org.sharad.velvetinvestment.shared.PieChart
import org.sharad.velvetinvestment.shared.PieChartEntry
import org.sharad.velvetinvestment.shared.UiStateContainer
import org.sharad.velvetinvestment.shared.compose.BackHeader
import org.sharad.velvetinvestment.shared.compose.ShadowCard
import org.sharad.velvetinvestment.shared.theme.LocalVelvetShapes
import org.sharad.velvetinvestment.shared.theme.VelvetTheme
import org.sharad.velvetinvestment.shared.theme.backgroundLight
import org.sharad.velvetinvestment.shared.theme.tinyLabel
import org.sharad.velvetinvestment.shared.theme.titlesStyle
import org.sharad.velvetinvestment.utils.UiState
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.icon_warning

@Composable
fun BundleDetailsScreenRoot(
    viewModel: BundleDetailsViewModel,
    onBackClick: () -> Unit,
    onReviewClick: () -> Unit,
    onChangeFundClick: (BundleCategoryDomain) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    BundleDetailsScreenRootContent(
        uiState = uiState,
        onBackClick = onBackClick,
        onReviewClick = onReviewClick,
        onChangeFundClick = onChangeFundClick,
        onRetry = { viewModel.loadBundleDetails() }
    )
}

@Composable
fun BundleDetailsScreenRootContent(
    uiState: BundleDetailsState,
    onBackClick: () -> Unit,
    onReviewClick: () -> Unit,
    onChangeFundClick: (BundleCategoryDomain) -> Unit,
    onRetry: () -> Unit
) {
    Scaffold(
        bottomBar = {
            if (uiState is BundleDetailsState.Success) {
                NextButtonFooter(
                    onClick = onReviewClick,
                    value = "Review Selected Fund",
                )
            }
        },
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(bottom = it.calculateBottomPadding())
        ) {
            BackHeader(
                onBackClick = onBackClick,
                heading = "",
                modifier = Modifier.fillMaxWidth(),
                showBack = true
            )

            UiStateContainer(
                uiState = uiState.toUiState(),
                onRetry = onRetry
            ) { data ->
                BundleDetailsScreen(
                    bundle = data,
                    onChangeFundClick = onChangeFundClick
                )
            }
        }
    }
}

fun BundleDetailsState.toUiState(): UiState<BundleDomain> {
    return when (this) {
        BundleDetailsState.Loading -> UiState.Loading
        is BundleDetailsState.Success -> UiState.Success(this.data)
        is BundleDetailsState.Error -> UiState.Error(this.message)
    }
}

@Composable
fun BundleDetailsScreen(
    bundle: BundleDomain,
    onChangeFundClick: (BundleCategoryDomain) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column {
                Text(
                    text = "Velvet Curated Bundles",
                    style = titlesStyle.copy(fontSize = 12.sp),
                    color = Secondary
                )
                Text(
                    text = bundle.name,
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Created based on your risk profile and investment timeline. Select a bundle fund strategy to start your investment journey.",
                    style = titlesStyle,
                    color = titleColor
                )
            }
        }

        item {
            BundleOverviewCard(bundle)
        }

        item {
            Text(
                text = "SUB-CATEGORY DISTRIBUTION",
                style = titlesStyle,
                color = titleColor,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        items(bundle.categories) { category ->
            CategoryDistributionItem(category, onChangeFundClick)
        }
    }
}

@Composable
fun BundleOverviewCard(bundle: BundleDomain) {
    ShadowCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(
                    space = 8.dp,
                    alignment = Alignment.CenterHorizontally
                ),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                BundleTag(bundle.metaData.investmentGrowth)
                BundleTag(bundle.metaData.investmentTime)
                BundleTag(bundle.metaData.riskLevel)
            }

            Text(
                text = bundle.description,
                style = MaterialTheme.typography.displayMedium,
                color = Color.Black,
                textAlign = TextAlign.Center
            )

            Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(vertical = 12.dp)) {
                val pieData = remember(bundle.assetAllocation) {
                    listOf(
                        PieChartEntry(bundle.assetAllocation.equity.toFloat(), Primary),
                        PieChartEntry(bundle.assetAllocation.commodity.toFloat(), Secondary),
                        PieChartEntry(bundle.assetAllocation.debt.toFloat(), Color(0xFF4CAF50)),
                        PieChartEntry(bundle.assetAllocation.hybrid.toFloat(), Color(0xFF2196F3))
                    )
                }
                PieChart(
                    data = pieData,
                    modifier = Modifier.size(140.dp),
                    strokeWidth = 80f
                )

                val highestAllocation = remember(bundle.assetAllocation) {
                    listOf(
                        "Equity" to bundle.assetAllocation.equity,
                        "Commodity" to bundle.assetAllocation.commodity,
                        "Debt" to bundle.assetAllocation.debt,
                        "Hybrid" to bundle.assetAllocation.hybrid
                    ).maxBy { it.second }
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "${highestAllocation.second.toInt()}%",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Text(
                        text = highestAllocation.first,
                        fontSize = 12.sp,
                        color = titleColor
                    )
                }
            }


            AssetLegend(bundle.assetAllocation)

            AllocationLockedCard()
        }
    }
}

@Composable
fun BundleTag(text: String) {
    Surface(
        shape = LocalVelvetShapes.current.circle,
        color = backgroundLight,
        border = BorderStroke(1.dp, backgroundGray)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            style = tinyLabel.copy(fontWeight = FontWeight.Normal),
            color = Color.Black
        )
    }
}

@Composable
fun AssetLegend(allocation: AssetAllocationDomain) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        if (allocation.equity > 0) LegendItem("Equity", allocation.equity, Primary)
        if (allocation.commodity > 0) LegendItem("Commodities", allocation.commodity, Secondary)
        if (allocation.debt > 0) LegendItem("Debt", allocation.debt, Color(0xFF4CAF50))
        if (allocation.hybrid > 0) LegendItem("Hybrid", allocation.hybrid, Color(0xFF2196F3))
    }
}

@Composable
fun LegendItem(label: String, percentage: Double, color: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Box(Modifier.size(14.dp)
                .clip(RoundedCornerShape(20))
                .background(color)
            )
            Text(label, style = MaterialTheme.typography.labelSmall)
        }
        Text("${percentage.toInt()}%", fontWeight = FontWeight.Bold)
    }
}

@Composable
fun AllocationLockedCard() {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFFFFF7E6)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                painter = painterResource(Res.drawable.icon_warning),
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = titleColor
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = "Allocation is locked as per selected portfolio. You can change individual funds, not the weights.",
                style = titlesStyle.copy(fontSize = 12.sp),
                color = titleColor
            )
        }
    }
}

@Composable
fun CategoryDistributionItem(
    category: BundleCategoryDomain,
    onChangeFundClick: (BundleCategoryDomain) -> Unit
) {
    ShadowCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = category.displayName,
                        style = MaterialTheme.typography.labelLarge
                    )
                    Surface(
                        shape = LocalVelvetShapes.current.circle,
                        color = appGreen.copy(alpha = 0.1f)
                    ) {
                        Text(
                            text = "${category.slots.size}" + if (category.slots.size > 1) " Funds" else " Fund",
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 2.dp),
                            style = titlesStyle.copy(fontSize = 12.sp),
                            color = appGreen
                        )
                    }
                }
                Text(
                    text = category.slots.joinToString(" & ") { it.selectedFund?.let { it.displayName1.ifEmpty { it.schemeName.ifEmpty { it.displayName2 } } }?:"--" },
                    style = titlesStyle,
                    color = titleColor.copy(alpha = 0.6f),
                )
            }

            Surface(
                shape = LocalVelvetShapes.current.roundedDp8,
                color = appGreen.copy(alpha = 0.1f)
            ) {
                Text(
                    text = "Change",
                    color = appGreen,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp,
                    modifier = Modifier.clickable { onChangeFundClick(category) }
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                )
            }
        }
    }
}

@Preview(
    heightDp = 2000
)
@Composable
private fun BundleDetailsScreenRootPreview() {
    VelvetTheme {
        BundleDetailsScreenRootContent(
            uiState = BundleDetailsState.Success(sampleBundle, previewBundleTransactionRules),
            onBackClick = {},
            onReviewClick = {},
            onChangeFundClick = {},
            onRetry = {}
        )
    }
}

val previewTransactionRules = TransactionRulesDomain(
    id = "rule_1",
    mfProductId = "fund_1",

    minSipAmount = 500,
    minLumpSumAmount = 500,
    minInvestmentAmount = 0,

    minLumpsumAddOnAmount = 500,
    minRedemptionQty = 0,
    minRedemptionAmount = 500,

    minDailySipAmount = 100,
    minWeeklySipAmount = 500,
    minFortnightlySipAmount = 500,
    minMonthlySipAmount = 500,
    minQuarterlySipAmount = 1500,
    minSemiAnnualSipAmount = 0,
    minAnnualSipAmount = 500,

    sipAllowedDates = (1..28).toList(),
    sipFrequencies = listOf(
        "Y",
        "D",
        "FN",
        "OM",
        "Q",
        "WD"
    ),

    createdAt = "2026-06-04T10:31:01.635Z",
    updatedAt = "2026-07-14T19:30:41.853Z"
)

val previewBundleTransactionRules = BundleTransactionRules(
    minBundleSipAmount = 5000,
    minBundleLumpsumAmount = 5000,

    minDailySipAmount = 1000,
    minWeeklySipAmount = 5000,
    minFortnightlySipAmount = 5000,
    minMonthlySipAmount = 5000,
    minQuarterlySipAmount = 15000,
    minSemiAnnualSipAmount = 0,
    minAnnualSipAmount = 5000,

    sipAllowedDates = (1..28).toList(),

    sipFrequencies = listOf(
        "D",
        "FN",
        "OM",
        "Q",
        "Y"
    )
)
private val sampleBundle = BundleDomain(
    name = "Growth Multiplier",
    description = "A high-growth bundle focusing on equity and commodities.",
    assetAllocation = AssetAllocationDomain(
        equity = 70.0,
        debt = 10.0,
        hybrid = 10.0,
        commodity = 10.0
    ),
    metaData = BundleMetaDataDomain(
        riskLevel = "High Risk",
        investmentTime = "5+ Years",
        investmentGrowth = "15% p.a."
    ),
    categories = listOf(
        BundleCategoryDomain(
            id = "cat1",
            categoryName = "Large Cap",
            displayName = "Large Cap Funds",
            allocationPercentage = 40.0,
            funds = listOf(
                FundDomain(
                    id = "fund1",
                    schemeId = "s1",
                    isin = "isin1",
                    imageUrl = "",
                    mappingCode = "m1",
                    schemeName = "SBI Bluechip Fund",
                    displayName1 = "SBI Bluechip",
                    displayName2 = "Growth",
                    amcId = "amc1",
                    amcCode = "amcc1",
                    amcName = "SBI Mutual Fund",
                    assetType = "Equity",
                    schemeType = "Open Ended",
                    structure = "Growth",
                    riskName = "High",
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
                        return3Y = 12.0,
                        return5Y = 14.0,
                        return6M = 7.0,
                        return90D = 3.0
                    ),
                    transactionRules = previewTransactionRules
                )
            ),
            slots = emptyList()
        ),
    )
)
