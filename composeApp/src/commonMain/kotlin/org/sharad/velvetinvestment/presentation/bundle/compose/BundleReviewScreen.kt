package org.sharad.velvetinvestment.presentation.bundle.compose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import org.jetbrains.compose.resources.painterResource
import org.sharad.emify.core.ui.theme.Primary
import org.sharad.emify.core.ui.theme.Secondary
import org.sharad.emify.core.ui.theme.appGreen
import org.sharad.emify.core.ui.theme.titleColor
import org.sharad.velvetinvestment.domain.models.bundle.AssetAllocationDomain
import org.sharad.velvetinvestment.domain.models.bundle.BundleCategoryDomain
import org.sharad.velvetinvestment.domain.models.bundle.BundleDomain
import org.sharad.velvetinvestment.domain.models.bundle.BundleMetaDataDomain
import org.sharad.velvetinvestment.domain.models.bundle.FundDomain
import org.sharad.velvetinvestment.domain.models.bundle.FundMetricsDomain
import org.sharad.velvetinvestment.domain.models.bundle.PortfolioSlotDomain
import org.sharad.velvetinvestment.presentation.bundle.viewmodel.BundleDetailsState
import org.sharad.velvetinvestment.presentation.bundle.viewmodel.BundleDetailsViewModel
import org.sharad.velvetinvestment.presentation.mutualfund.compose.MutualFundIcon
import org.sharad.velvetinvestment.presentation.onboarding.compose.personaldetails.NextButtonFooter
import org.sharad.velvetinvestment.shared.UiStateContainer
import org.sharad.velvetinvestment.shared.compose.BackHeader
import org.sharad.velvetinvestment.shared.compose.ListWheelPicker
import org.sharad.velvetinvestment.shared.compose.ShadowCard
import org.sharad.velvetinvestment.shared.theme.LocalVelvetShapes
import org.sharad.velvetinvestment.shared.theme.VelvetTheme
import org.sharad.velvetinvestment.shared.theme.titlesStyle
import org.sharad.velvetinvestment.utils.FundTypeSelector
import org.sharad.velvetinvestment.utils.SelectedFundType
import org.sharad.velvetinvestment.utils.formatWithCommas
import org.sharad.velvetinvestment.utils.groupDigitsIndian
import org.sharad.velvetinvestment.utils.trimTo
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.arrow_down
import velvet.composeapp.generated.resources.plus_icon
import velvet.composeapp.generated.resources.success_icon

@Composable
fun BundleReviewScreenRoot(
    viewModel: BundleDetailsViewModel,
    onBackClick: () -> Unit,
    onProceedClick: () -> Unit,
    onChangeFundClick: (BundleCategoryDomain) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val investmentAmount by viewModel.investmentAmount.collectAsStateWithLifecycle()
    val selectedSipDay by viewModel.selectedSipDay.collectAsStateWithLifecycle()
    val isAddingToCart by viewModel.isAddingToCart.collectAsStateWithLifecycle()
    val fundType by FundTypeSelector.fundType.collectAsStateWithLifecycle()

    var showDayPicker by remember { mutableStateOf(false) }

    UiStateContainer(
        uiState = uiState.toUiState(),
        onRetry = { viewModel.loadBundleDetails() },
        modifier = Modifier.imePadding()
    ) { bundle ->
        val rules = (uiState as? BundleDetailsState.Success)?.transactionRules
        if (rules != null) {
            LaunchedEffect(bundle, rules) {
                println("============= BUNDLE REVIEW =============")

                bundle.categories.forEach { category ->
                    println("")
                    println("Category: ${category.displayName}")
                    println("Category Allocation: ${category.allocationPercentage}%")

                    category.slots.forEach { slot ->
                        println("  -------------------------")
                        println("  Slot Rank: ${slot.rank}")
                        println("  Slot Allocation: ${slot.allocationPercentage}%")

                        val fund = slot.selectedFund

                        if (fund == null) {
                            println("  Selected Fund: NULL")
                        } else {
                            val tr = fund.transactionRules

                            println("  Selected Fund : ${fund.schemeName}")
                            println("  SIP Min       : ${tr.minSipAmount}")
                            println("  Lumpsum Min   : ${tr.minLumpSumAmount}")
                            println("  Daily SIP     : ${tr.minDailySipAmount}")
                            println("  Weekly SIP    : ${tr.minWeeklySipAmount}")
                            println("  Fortnightly   : ${tr.minFortnightlySipAmount}")
                            println("  Monthly SIP   : ${tr.minMonthlySipAmount}")
                            println("  Quarterly SIP : ${tr.minQuarterlySipAmount}")
                            println("  Semi Annual   : ${tr.minSemiAnnualSipAmount}")
                            println("  Annual SIP    : ${tr.minAnnualSipAmount}")
                        }
                    }
                }

                println("")
                println("============= DERIVED RULES =============")
                println("Bundle SIP Min       : ${rules.minBundleSipAmount}")
                println("Bundle Lumpsum Min   : ${rules.minBundleLumpsumAmount}")
                println("Daily SIP            : ${rules.minDailySipAmount}")
                println("Weekly SIP           : ${rules.minWeeklySipAmount}")
                println("Fortnightly SIP      : ${rules.minFortnightlySipAmount}")
                println("Monthly SIP          : ${rules.minMonthlySipAmount}")
                println("Quarterly SIP        : ${rules.minQuarterlySipAmount}")
                println("Semi Annual SIP      : ${rules.minSemiAnnualSipAmount}")
                println("Annual SIP           : ${rules.minAnnualSipAmount}")
                println("Allowed Dates        : ${rules.sipAllowedDates}")
                println("Frequencies          : ${rules.sipFrequencies}")
                println("=========================================")
            }
            Box(modifier = Modifier.fillMaxSize()) {
                BundleReviewScreen(
                    bundle = bundle,
                    investmentAmount = investmentAmount,
                    selectedSipDay = selectedSipDay,
                    fundType = fundType,
                    isAddingToCart = isAddingToCart,
                    onAmountChange = { viewModel.setInvestmentAmount(it) },
                    onSipDayClick = { showDayPicker = true },
                    onBackClick = onBackClick,
                    onProceedClick = {
                        viewModel.addBundleToCart {
                            onProceedClick()
                        }
                    },
                    onChangeFundClick = onChangeFundClick,
                    minAmount = if (fundType == SelectedFundType.SIP) rules.minBundleSipAmount.toLong() else rules.minBundleLumpsumAmount.toLong()
                )

                if (showDayPicker) {
                    ListWheelPicker(
                        title = "Select SIP Date",
                        items = rules.sipAllowedDates,
                        selectedItem = selectedSipDay,
                        onItemSelected = {
                            viewModel.setSelectedSipDay(it)
                            showDayPicker = false
                        },
                        onDismiss = {
                            showDayPicker = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun BundleReviewScreen(
    bundle: BundleDomain,
    investmentAmount: Long,
    selectedSipDay: Int?,
    fundType: SelectedFundType,
    isAddingToCart: Boolean,
    onAmountChange: (Long) -> Unit,
    onSipDayClick: () -> Unit,
    onBackClick: () -> Unit,
    onProceedClick: () -> Unit,
    onChangeFundClick: (BundleCategoryDomain) -> Unit,
    minAmount: Long
) {
    val totalSlots = bundle.categories.sumOf { it.slots.size }
    val selectedSlots = bundle.categories.sumOf { category ->
        category.slots.count { it.selectedFund != null }
    }
    val allFundsSelected = selectedSlots == totalSlots

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        BackHeader(
            onBackClick = onBackClick,
            heading = "Review Your Portfolio",
            showBack = true
        )
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Column {
                    Text(
                        text = "REVIEW YOUR PORTFOLIO",
                        style = titlesStyle.copy(fontSize = 12.sp),
                        color = Secondary
                    )
                    Text(
                        text = "Review Your Portfolio",
                        style = MaterialTheme.typography.displayMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            }
            item {
                SuccessInfoBox("$selectedSlots of $totalSlots funds selected. Amounts auto-split by the locked allocation.")
            }

            if (!allFundsSelected) {
                item {
                    InfoBox("Select a fund for every slot to proceed with this portfolio.")
                }
            }

            item {
                InvestmentSummaryCard(
                    bundleName = bundle.name,
                    investmentAmount = investmentAmount,
                    fundType = fundType,
                    selectedSipDay = selectedSipDay,
                    onAmountChange = onAmountChange,
                    onSipDayClick = onSipDayClick,
                    minAmount = minAmount,
                    assetAllocation = bundle.assetAllocation
                )
            }

            item {
                Text(
                    text = "PORTFOLIO COMPOSITION",
                    style = titlesStyle.copy(fontSize = 12.sp, fontWeight = FontWeight.Bold),
                    color = titleColor.copy(alpha = 0.6f),
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            bundle.categories.forEach { category ->
                item {
                    CategoryCompositionHeader(category)
                }
                items(category.slots) { slot ->
                    val allocatedAmount = (investmentAmount * slot.allocationPercentage / 100).toLong()
                    SlotReviewItem(
                        slot = slot,
                        amount = allocatedAmount,
                        onChangeClick = { onChangeFundClick(category) }
                    )
                }
            }
        }
        NextButtonFooter(
            onClick = onProceedClick,
            value = "Proceed to Invest",
            loading = isAddingToCart,
            enabled = allFundsSelected && investmentAmount >= minAmount && when(fundType){
                SelectedFundType.SIP -> selectedSipDay != null
                SelectedFundType.LUMSUM -> true
            }
        )
    }
}

@Composable
fun SuccessInfoBox(text: String) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color(0xffD1FAE5).copy(0.5f)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                painter = painterResource(Res.drawable.success_icon),
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = appGreen
            )
            Text(
                text = text,
                style = titlesStyle.copy(fontSize = 13.sp),
                color = appGreen
            )
        }
    }
}

@Composable
fun InvestmentSummaryCard(
    bundleName: String,
    investmentAmount: Long,
    fundType: SelectedFundType,
    selectedSipDay: Int?,
    onAmountChange: (Long) -> Unit,
    onSipDayClick: () -> Unit,
    minAmount: Long,
    assetAllocation: AssetAllocationDomain
) {
    ShadowCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = bundleName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Total Investment Amount",
                        style = titlesStyle.copy(fontSize = 10.sp),
                        color = titleColor.copy(alpha = 0.6f)
                    )
                }
                FundTypeTag(fundType)
            }

            Spacer(Modifier.height(24.dp))

            // Local text mirror of the amount so the field can be edited freely.
            // An empty field reads as 0, so clearing it doesn't get overwritten with "0".
            var amountText by remember {
                mutableStateOf(if (investmentAmount == 0L) "" else investmentAmount.toString())
            }
            LaunchedEffect(investmentAmount) {
                if ((amountText.toLongOrNull() ?: 0L) != investmentAmount) {
                    amountText = investmentAmount.toString()
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Surface(
                    modifier = Modifier
                        .size(40.dp)
                        .clickable { onAmountChange((investmentAmount - 500).coerceAtLeast(0)) },
                    shape = LocalVelvetShapes.current.roundedDp12,
                    color = Color(0xffF3F4F5)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Box(Modifier.width(16.dp).height(2.dp).background(Color.Black))
                    }
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    BasicTextField(
                        value = amountText,
                        onValueChange = { input ->
                            val digits = input.filter { it.isDigit() }.take(9)
                            amountText = digits
                            onAmountChange(digits.toLongOrNull() ?: 0L)
                        },
                        textStyle = MaterialTheme.typography.displayMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            textAlign = TextAlign.Center
                        ),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        cursorBrush = SolidColor(Color.Black),
                        // The ₹ prefix is dropped while empty so it can't sit next to the placeholder.
                        visualTransformation = if (amountText.isEmpty()) {
                            VisualTransformation.None
                        } else {
                            RupeeGroupingTransformation
                        },
                        decorationBox = { innerTextField ->
                            Box(contentAlignment = Alignment.Center) {
                                if (amountText.isEmpty()) {
                                    Text(
                                        text = "Enter amount",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color(0xffC5C5C5),
                                        textAlign = TextAlign.Center
                                    )
                                }
                                innerTextField()
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = if (fundType == SelectedFundType.SIP) "MONTHLY" else "ONE-TIME",
                        style = titlesStyle.copy(fontSize = 10.sp, fontWeight = FontWeight.Bold),
                        color = titleColor.copy(alpha = 0.8f)
                    )
                }

                Surface(
                    modifier = Modifier
                        .size(40.dp)
                        .clickable { onAmountChange(investmentAmount + 500) },
                    shape = LocalVelvetShapes.current.roundedDp12,
                    color = Color(0xffF3F4F5)
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.plus_icon),
                        null,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }

            if (investmentAmount < minAmount) {
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Amount is less than the minimum of ₹${formatWithCommas(minAmount)}",
                    style = titlesStyle.copy(fontSize = 12.sp),
                    color = Color.Red,
                    textAlign = TextAlign.Center
                )
            }

            if (fundType == SelectedFundType.SIP) {
                Spacer(Modifier.height(24.dp))
                SipDaySelector(selectedDay = selectedSipDay, onSipDayClick = onSipDayClick)
            }

            Spacer(Modifier.height(32.dp))

            AssetAllocationMiniLegend(assetAllocation)
        }
    }
}

/**
 * Displays a digits-only amount as "₹33,33,333". Display only — the field's value
 * stays digits-only, so nothing downstream ever sees the ₹ or the commas.
 */
private val RupeeGroupingTransformation = VisualTransformation { text ->
    val digits = text.text
    val grouped = groupDigitsIndian(digits)

    // Transformed index each original digit lands on, offset by 1 for the ₹ prefix.
    val digitOffsets = IntArray(digits.length + 1)
    var digitIndex = 0
    grouped.forEachIndexed { index, char ->
        if (char != ',') {
            digitOffsets[digitIndex] = index + 1
            digitIndex++
        }
    }
    digitOffsets[digits.length] = grouped.length + 1

    TransformedText(
        AnnotatedString("₹$grouped"),
        object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int =
                digitOffsets[offset.coerceIn(0, digits.length)]

            override fun transformedToOriginal(offset: Int): Int {
                val clamped = offset.coerceIn(0, grouped.length + 1)
                if (clamped <= 1) return 0
                return grouped.take(clamped - 1).count { it != ',' }
            }
        }
    )
}

@Composable
fun FundTypeTag(type: SelectedFundType) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color=Color(0xffD1FAE5).copy(0.5f)
    ) {
        Text(
            text = if (type == SelectedFundType.SIP) "ACTIVE SIP" else "LUMPSUM",
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = titlesStyle.copy(fontSize = 10.sp),
            color = appGreen
        )
    }
}

@Composable
fun SipDaySelector(selectedDay: Int?, onSipDayClick: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "SIP DAY",
            style = titlesStyle.copy(fontSize = 12.sp, fontWeight = FontWeight.Bold),
            color = titleColor
        )
        Spacer(Modifier.height(8.dp))
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onSipDayClick() },
            shape = RoundedCornerShape(12.dp),
            color = Color(0xffF3F4F5),
            border = BorderStroke(1.dp, titleColor.copy(alpha = 0.1f))
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = selectedDay?.let { "$it${getDaySuffix(it)}" } ?: "--"
                )
                Icon(painter = painterResource(Res.drawable.arrow_down), null, modifier = Modifier.size(16.dp))
            }
        }
    }
}

fun getDaySuffix(day: Int): String {
    return when {
        day in 11..13 -> "th"
        day % 10 == 1 -> "st"
        day % 10 == 2 -> "nd"
        day % 10 == 3 -> "rd"
        else -> "th"
    }
}

@Composable
fun AssetAllocationMiniLegend(allocation: AssetAllocationDomain) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        if (allocation.equity > 0) MiniLegendItem("Equity", allocation.equity, Primary)
        if (allocation.commodity > 0) MiniLegendItem("Commodities", allocation.commodity, Secondary)
        if (allocation.debt > 0) MiniLegendItem("Debt", allocation.debt, Color(0xFF4CAF50))
        if (allocation.hybrid > 0) MiniLegendItem("Hybrid", allocation.hybrid, Color(0xFF4CAF50))
    }
}

@Composable
fun MiniLegendItem(label: String, percentage: Double, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.height(IntrinsicSize.Min)
    ) {
        Box(
            Modifier
                .width(6.dp)
                .fillMaxHeight()
                .padding(vertical = 2.dp)
                .clip(CircleShape)
                .background(color))
        Column {
            Text(text = "${percentage.toInt()}%", fontWeight = FontWeight.Bold, fontSize = 14.sp, lineHeight = 14.sp)
            Text(text = label.uppercase(), style = titlesStyle.copy(fontSize = 10.sp), color = titleColor)
        }
    }
}

@Composable
fun CategoryCompositionHeader(category: BundleCategoryDomain) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "${category.displayName.uppercase()} • ${category.allocationPercentage.toInt()}%",
            style = titlesStyle.copy(fontSize = 12.sp, fontWeight = FontWeight.Bold)
        )
        Text(
            text = "${category.slots.size} FUNDS",
            style = titlesStyle.copy(fontSize = 10.sp),
            color = titleColor.copy(alpha = 0.4f)
        )
    }
}

@Composable
fun SlotReviewItem(
    slot: PortfolioSlotDomain,
    amount: Long,
    onChangeClick: () -> Unit
) {
    val fund = slot.selectedFund
    if (fund == null) {
        EmptySlotReviewItem(
            allocationPercentage = slot.allocationPercentage,
            onSelectClick = onChangeClick
        )
        return
    }

    ShadowCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
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
            Column(modifier = Modifier.weight(1f)) {
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = fund.schemeName,
                        style = titlesStyle.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.weight(1f),
                        maxLines = 1,
                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis)
                    Spacer(modifier = Modifier.padding(start = 4.dp))
                    Text(text = "₹${amount}", style = titlesStyle.copy(fontWeight = FontWeight.Bold))
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(
                        text = "Allocation: ${slot.allocationPercentage.toInt()}%",
                        style = titlesStyle.copy(fontSize = 10.sp),
                        color = titleColor
                    )
                    val return1Y = fund.metrics.return1Y

                    Text(
                        text = "${if (return1Y >= 0) "+" else ""}${return1Y.trimTo(1)}% 1Y",
                        style = titlesStyle.copy(fontSize = 10.sp),
                        color = if (return1Y >= 0) appGreen else Color.Red
                    )
                }
            }

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = appGreen.copy(alpha = 0.1f),
                modifier = Modifier.clickable { onChangeClick() }
            ) {
                Text(
                    text = "Change",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    style = titlesStyle.copy(fontSize = 12.sp, fontWeight = FontWeight.SemiBold),
                    color = appGreen
                )
            }
        }
    }
}

@Composable
fun EmptySlotReviewItem(
    allocationPercentage: Double,
    onSelectClick: () -> Unit
) {
    ShadowCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier.size(38.dp)
                    .clip(LocalVelvetShapes.current.roundedDp12)
                    .background(Color(0xffF3F4F5)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(Res.drawable.plus_icon),
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    tint = titleColor.copy(alpha = 0.5f)
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "No fund selected for this category",
                    style = titlesStyle.copy(fontWeight = FontWeight.Bold),
                    maxLines = 2,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                )
                Text(
                    text = "Allocation: ${allocationPercentage.toInt()}%",
                    style = titlesStyle.copy(fontSize = 10.sp),
                    color = titleColor
                )
            }

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = appGreen.copy(alpha = 0.1f),
                modifier = Modifier.clickable { onSelectClick() }
            ) {
                Text(
                    text = "Select",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    style = titlesStyle.copy(fontSize = 12.sp, fontWeight = FontWeight.SemiBold),
                    color = appGreen
                )
            }
        }
    }
}

@Preview(heightDp = 1000, showBackground = true)
@Composable
private fun BundleReviewScreenPreview() {
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
            return1Y = 12.4,
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

    val sampleBundle = BundleDomain(
        name = "Aggressive",
        description = "High growth",
        assetAllocation = AssetAllocationDomain(95.0, 0.0, 0.0, 5.0),
        metaData = BundleMetaDataDomain("High", "5y", "15%"),
        categories = listOf(sampleCategory)
    )

    VelvetTheme {
        BundleReviewScreen(
            bundle = sampleBundle,
            investmentAmount = 10000L,
            selectedSipDay = null,
            fundType = SelectedFundType.SIP,
            isAddingToCart = false,
            onAmountChange = {},
            onSipDayClick = {},
            onBackClick = {},
            onProceedClick = {},
            onChangeFundClick = {},
            minAmount = 5000L
        )
    }
}
