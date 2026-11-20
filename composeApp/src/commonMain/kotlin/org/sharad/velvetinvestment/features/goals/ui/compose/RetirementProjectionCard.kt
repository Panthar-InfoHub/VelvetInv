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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sharad.emify.core.ui.theme.Primary
import org.sharad.emify.core.ui.theme.greenColor
import org.sharad.emify.core.ui.theme.orangeColor
import org.sharad.emify.core.ui.theme.redColor
import org.sharad.velvetinvestment.features.goals.domain.models.RetirementProjection
import org.sharad.velvetinvestment.shared.genericDropShadow
import org.sharad.velvetinvestment.shared.theme.Poppins
import org.sharad.velvetinvestment.shared.theme.VelvetTheme
import org.sharad.velvetinvestment.shared.theme.subHeading
import org.sharad.velvetinvestment.utils.DateTimeUtils

/**
 * Retirement's projection card, styled like [GoalProjectionCard]: the monthly cost the plan has
 * to fund once retired, the corpus that takes, when retirement starts, and what is still needed
 * to get there. Every figure is the server's — nothing is recomputed here.
 */
@Composable
fun RetirementProjectionCard(
    title: String,
    projection: RetirementProjection,
    modifier: Modifier = Modifier,
    projecting: Boolean = false
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
                    label = "Monthly Cost at Retirement",
                    value = projection.monthlyCostAtRetirement.asRupees(),
                    valueColor = Color.Black,
                    modifier = Modifier.weight(1f)
                )
                ProjectionCell(
                    label = "Target Corpus",
                    value = projection.targetCorpus.asRupees(),
                    valueColor = orangeColor,
                    alignEnd = true,
                    modifier = Modifier.weight(1f)
                )
            }

            Row(modifier = Modifier.fillMaxWidth()) {
                ProjectionCell(
                    label = "Retire In",
                    value = "${DateTimeUtils.getCurrentYear() + projection.yearsToRetirement}" +
                        " (Age ${projection.retirementAge})",
                    valueColor = redColor,
                    modifier = Modifier.weight(1f)
                )
                ProjectionCell(
                    label = "Monthly SIP",
                    value = projection.totalMonthlySip.asRupees(),
                    valueColor = greenColor,
                    alignEnd = true,
                    modifier = Modifier.weight(1f)
                )
            }

            Row(modifier = Modifier.fillMaxWidth()) {
                ProjectionCell(
                    label = "Savings Grow To",
                    value = projection.fvExistingSavings.asRupees(),
                    valueColor = Color.Black,
                    modifier = Modifier.weight(1f)
                )
                ProjectionCell(
                    label = "Still to Build",
                    value = projection.remainingCorpusToBuild.asRupees(),
                    valueColor = Color.Black,
                    alignEnd = true,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RetirementProjectionCardPreview() {
    VelvetTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            RetirementProjectionCard(
                title = "Projected Impact",
                projection = RetirementProjection(
                    currentAge = 30,
                    retirementAge = 70,
                    planUntilAge = 85,
                    yearsToRetirement = 40,
                    monthlyCostAtRetirement = 205_714.36,
                    targetCorpus = 40_731_443.03,
                    fvExistingSavings = 45_259_255.57,
                    remainingCorpusToBuild = 0.0,
                    totalMonthlySip = 0.0,
                    additionalLumpsumToday = 0.0
                )
            )
        }
    }
}
