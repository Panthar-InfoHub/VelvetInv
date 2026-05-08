package org.sharad.velvetinvestment.presentation.investmentrate.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.sharad.emify.core.ui.theme.Primary
import org.sharad.emify.core.ui.theme.Secondary
import org.sharad.emify.core.ui.theme.TextGray
import org.sharad.velvetinvestment.domain.models.user.SavingTrendsDomain
import org.sharad.velvetinvestment.utils.formatMoneyWithUnits
import org.sharad.velvetinvestment.shared.theme.Poppins
import org.sharad.velvetinvestment.shared.theme.VelvetTheme
import kotlin.math.roundToInt

data class SavingTrendBarFraction(
    val monthText: String,
    val savings: Long,
    val investments: Long,
    val savingsFraction: Float,
    val investmentsFraction: Float,
)

fun List<SavingTrendsDomain>.toBarFractions(): List<SavingTrendBarFraction> {
    val maxValue = flatMap {
        listOf(it.savings, it.investments)
    }.maxOrNull()?.takeIf { it > 0 } ?: 1L

    return map { item ->
        SavingTrendBarFraction(
            monthText = item.monthText,
            savings = item.savings,
            investments = item.investments,
            savingsFraction = item.savings.toFloat() / maxValue.toFloat(),
            investmentsFraction = item.investments.toFloat() / maxValue.toFloat(),
        )
    }
}

data class SelectedBarData(
    val label: LabelType,
    val amount: Long,
    val offsetX: Float,
    val offsetY: Float
)

enum class LabelType(val label: String) {
    SAVINGS("Savings"),
    INVESTMENTS("Investments")
}

@Composable
fun SavingVsInvestingBarChart(
    trends: List<SavingTrendsDomain>,
    modifier: Modifier = Modifier,
    animationProgress: () -> Float = { 1f }
) {
    val barShape = remember { RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp) }
    val tooltipShape = remember { RoundedCornerShape(6.dp) }

    val chartData = remember(trends) { trends.toBarFractions() }
    
    var selectedBar by remember { mutableStateOf<SelectedBarData?>(null) }
    var chipWidth by remember { mutableFloatStateOf(0f) }
    var chartPosition by remember { mutableStateOf(Offset.Zero) }
    var chartWidth by remember { mutableFloatStateOf(0f) }

    Box(modifier = modifier
        .onGloballyPositioned {
            chartPosition = it.positionInRoot()
            chartWidth = it.size.width.toFloat()
        }
        .clickable(
            onClick = { selectedBar = null },
            indication = null,
            interactionSource = remember { MutableInteractionSource() }
        )
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(top = 32.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            chartData.forEach { trend ->
                BarColumn(
                    trend = trend,
                    barShape = barShape,
                    chartPosition = chartPosition,
                    animationProgress = animationProgress,
                    onBarSelected = { selectedBar = it }
                )
            }
        }

        selectedBar?.let { bar ->
            TooltipOverlay(
                bar = bar,
                chipWidth = chipWidth,
                chartWidth = chartWidth,
                shape = tooltipShape,
                onChipWidthMeasured = { chipWidth = it }
            )
        }
    }
}

@Composable
private fun BarColumn(
    trend: SavingTrendBarFraction,
    barShape: Shape,
    chartPosition: Offset,
    animationProgress: () -> Float,
    onBarSelected: (SelectedBarData) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier.fillMaxHeight()
    ) {
        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.weight(1f)
        ) {
            TrendBar(
                fraction = trend.savingsFraction,
                amount = trend.savings,
                label = LabelType.SAVINGS,
                color = Primary,
                shape = barShape,
                chartPosition = chartPosition,
                animationProgress = animationProgress,
                onSelect = onBarSelected
            )
            TrendBar(
                fraction = trend.investmentsFraction,
                amount = trend.investments,
                label = LabelType.INVESTMENTS,
                color = Secondary,
                shape = barShape,
                chartPosition = chartPosition,
                animationProgress = animationProgress,
                minHeight = 2.dp,
                onSelect = onBarSelected
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = trend.monthText,
            fontSize = 10.sp,
            lineHeight = 10.sp,
            fontFamily = Poppins,
            textAlign = TextAlign.Center,
            color = TextGray,
            modifier = Modifier.width(IntrinsicSize.Min)
        )
    }
}

@Composable
private fun TrendBar(
    fraction: Float,
    amount: Long,
    label: LabelType,
    color: Color,
    shape: Shape,
    chartPosition: Offset,
    animationProgress: () -> Float,
    minHeight: Dp = 0.dp,
    onSelect: (SelectedBarData) -> Unit
) {
    var barOffset by remember { mutableStateOf(Offset.Zero) }
    Box(
        modifier = Modifier
            .width(14.dp)
            .then(if (minHeight > 0.dp) Modifier.heightIn(min = minHeight) else Modifier)
            .fillMaxHeightAnimated(fraction, animationProgress)
            .clip(shape)
            .background(color)
            .onGloballyPositioned { coordinates ->
                barOffset = coordinates.positionInRoot() - chartPosition
            }
            .pointerInput(label, amount) {
                detectTapGestures(
                    onTap = {
                        onSelect(
                            SelectedBarData(
                                label = label,
                                amount = amount,
                                offsetX = barOffset.x,
                                offsetY = barOffset.y
                            )
                        )
                    }
                )
            }
    )
}

private fun Modifier.fillMaxHeightAnimated(
    fraction: Float,
    progress: () -> Float
) = layout { measurable, constraints ->
    val currentFraction = fraction * progress()
    val height = (constraints.maxHeight * currentFraction).roundToInt()
        .coerceIn(constraints.minHeight, constraints.maxHeight)
    val placeable = measurable.measure(
        constraints.copy(minHeight = height, maxHeight = height)
    )
    layout(placeable.width, placeable.height) {
        placeable.placeRelative(0, 0)
    }
}

@Composable
private fun TooltipOverlay(
    bar: SelectedBarData,
    chipWidth: Float,
    chartWidth: Float,
    shape: Shape,
    onChipWidthMeasured: (Float) -> Unit
) {
    Box(
        modifier = Modifier
            .offset {
                val desiredX = bar.offsetX + (14.dp.toPx() / 2f) - (chipWidth / 2f)
                val coercedX = desiredX.coerceIn(0f, chartWidth - chipWidth)
                IntOffset(
                    x = coercedX.roundToInt(),
                    y = (bar.offsetY - 35.dp.toPx()).roundToInt().coerceAtLeast(0)
                )
            }
            .onGloballyPositioned {
                onChipWidthMeasured(it.size.width.toFloat())
            }
            .clip(shape)
            .border(1.dp, Color.White.copy(alpha = 0.5f), shape)
            .background(
                color = if (bar.label == LabelType.SAVINGS) Primary else Secondary,
                shape = shape
            )
            .padding(horizontal = 8.dp, vertical = 1.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = bar.label.label,
                color = Color.White,
                fontSize = 10.sp,
                fontFamily = Poppins
            )
            Text(
                text = formatMoneyWithUnits(bar.amount),
                color = Color.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = Poppins
            )
        }
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFFFFFFFF
)
@Composable
fun SavingVsInvestingBarChartPreview() {
    val trends = listOf(
        SavingTrendsDomain("Jan", 4000, 0),
        SavingTrendsDomain("Feb", 6000, 4500),
        SavingTrendsDomain("Mar", 7000, 4000),
        SavingTrendsDomain("Apr", 12000, 7000),
        SavingTrendsDomain("May", 3000, 2500),
        SavingTrendsDomain("Jun", 9000, 8000)
    )
    VelvetTheme {
        Surface(
            color = Color.White
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(24.dp)
            ) {
                SavingVsInvestingBarChart(
                    trends = trends,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}
