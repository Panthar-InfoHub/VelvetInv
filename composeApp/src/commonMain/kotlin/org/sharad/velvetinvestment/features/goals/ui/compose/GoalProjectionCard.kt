package org.sharad.velvetinvestment.features.goals.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sharad.emify.core.ui.theme.Primary
import org.sharad.emify.core.ui.theme.greenColor
import org.sharad.emify.core.ui.theme.orangeColor
import org.sharad.emify.core.ui.theme.redColor
import org.sharad.emify.core.ui.theme.titleColor
import org.sharad.velvetinvestment.shared.genericDropShadow
import org.sharad.velvetinvestment.shared.theme.Poppins
import org.sharad.velvetinvestment.shared.theme.VelvetTheme
import org.sharad.velvetinvestment.shared.theme.subHeading
import org.sharad.velvetinvestment.shared.theme.titlesStyle
import org.sharad.velvetinvestment.utils.formatMoneyAfterL
import org.sharad.velvetinvestment.utils.withInterRupee

/**
 * Velvet's goal projection card: what the goal costs today (or the corpus asked for), what it
 * will cost at the target date, the target year and the monthly SIP that closes the gap. Every
 * figure is the server's `v2.0` projection — nothing is recomputed here.
 *
 * Used both as the live preview while a goal is being created and on the goal's own screen, so
 * the numbers a user saves are the numbers they then see.
 */
@Composable
fun GoalProjectionCard(
    title: String,
    isFixedCorpus: Boolean,
    todaysCost: Double,
    futureValue: Double,
    targetText: String,
    monthlySip: Double,
    modifier: Modifier = Modifier,
    /** Shows a small loader beside [title] while a fresh projection is on its way. */
    projecting: Boolean = false,
    footer: @Composable () -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .genericDropShadow(RoundedCornerShape(24.dp))
            .clip(RoundedCornerShape(24.dp))
            .background(Color.White)
            .padding(20.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = subHeading,
                    fontFamily = Poppins,
                    color = orangeColor,
                    modifier = Modifier.weight(1f, fill = false)
                )
                if (projecting) {
                    CircularProgressIndicator(
                        strokeWidth = 2.dp,
                        color = Primary,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }

            Row(modifier = Modifier.fillMaxWidth()) {
                ProjectionCell(
                    label = if (isFixedCorpus) "Target Corpus" else "Today's Cost",
                    value = todaysCost.asRupees(),
                    valueColor = Color.Black,
                    modifier = Modifier.weight(1f)
                )
                ProjectionCell(
                    label = "Future Value",
                    value = futureValue.asRupees(),
                    valueColor = orangeColor,
                    alignEnd = true,
                    modifier = Modifier.weight(1f)
                )
            }

            Row(modifier = Modifier.fillMaxWidth()) {
                ProjectionCell(
                    label = "Target",
                    value = targetText,
                    valueColor = redColor,
                    modifier = Modifier.weight(1f)
                )
                ProjectionCell(
                    label = "Monthly SIP",
                    value = monthlySip.asRupees(),
                    valueColor = greenColor,
                    alignEnd = true,
                    modifier = Modifier.weight(1f)
                )
            }

            footer()
        }
    }
}

@Composable
private fun ProjectionCell(
    label: String,
    value: String,
    valueColor: Color,
    modifier: Modifier = Modifier,
    alignEnd: Boolean = false
) {
    Column(
        modifier = modifier,
        horizontalAlignment = if (alignEnd) Alignment.End else Alignment.Start
    ) {
        Text(
            text = label,
            style = titlesStyle,
            fontFamily = Poppins,
            color = titleColor
        )
        Text(
            text = value.withInterRupee(),
            style = subHeading,
            color = valueColor,
            fontWeight = FontWeight.Bold
        )
    }
}

/** Whole rupees in Velvet's lakh/crore shorthand: paise in a 20-year projection are noise. */
internal fun Double.asRupees(): String = "₹ ${formatMoneyAfterL(coerceAtLeast(0.0).toLong())}"

@Preview(showBackground = true)
@Composable
private fun GoalProjectionCardPreview() {
    VelvetTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            GoalProjectionCard(
                title = "Projected Impact",
                isFixedCorpus = false,
                todaysCost = 1_000_000.0,
                futureValue = 1_276_281.56,
                targetText = "2031",
                monthlySip = 14_401.77,
                projecting = true
            )
        }
    }
}
