package org.sharad.velvetinvestment.features.goals.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.sharad.velvetinvestment.features.bottomNavigation.domain.models.GoalsSummaryDomain
import org.sharad.velvetinvestment.features.goals.domain.models.goalOptionFor
import org.sharad.velvetinvestment.shared.CustomProgressFillBar
import org.sharad.velvetinvestment.shared.genericDropShadow
import org.sharad.velvetinvestment.shared.theme.VelvetTheme
import org.sharad.velvetinvestment.shared.theme.subHeading
import org.sharad.velvetinvestment.utils.formatMoneyAfterL
import org.sharad.velvetinvestment.utils.formatMoneyWithUnits
import org.sharad.velvetinvestment.utils.withInterRupee
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.arrow_right

/**
 * A goal as the home and goals lists show it. Velvet's card; the icon follows the v2 goal type
 * ([goalIconFor]) and the progress is the server's own `progress_percent`, as in JN's `GoalCard`.
 */
@Composable
fun GoalCard(goal: GoalsSummaryDomain, onClick: () -> Unit) {

    val tintColor = goal.goalTypes.color
    val icon = goalIconFor(goal.goalTypes.type)
    val progress = goal.progressPercent.coerceIn(0, 100)

    Box(
        modifier = Modifier.fillMaxWidth()
            .genericDropShadow()
            .clip(RoundedCornerShape(10.dp))
            .background(Color.White, RoundedCornerShape(10.dp))
            .clickable(
                onClick=onClick
            ),
        contentAlignment = Alignment.Center
    ){
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Column(
                modifier=Modifier.weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    Box(modifier = Modifier.size(48.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(tintColor.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center){
                        Icon(
                            painter = painterResource(icon),
                            contentDescription = null,
                            tint = tintColor,
                            modifier = Modifier.height(24.dp)
                        )
                    }

                   Column(
                       verticalArrangement = Arrangement.spacedBy(4.dp),
                       modifier = Modifier.weight(1f).fillMaxWidth()
                   ) {
                       Text(
                           text=goal.title,
                           style = subHeading,
                           color = tintColor
                       )
                       Text(
                           text="₹${formatMoneyAfterL(goal.amount)}/${formatMoneyWithUnits(goal.targetAmount)}".withInterRupee(),
                           style = MaterialTheme.typography.labelSmall,
                           color = Color.Black
                       )
                   }

                    Text(
                        text= "$progress%",
                        style = MaterialTheme.typography.labelSmall,
                        color = tintColor,
                        modifier=Modifier.align(Alignment.Bottom)
                    )

                }

                CustomProgressFillBar(
                    modifier = Modifier.fillMaxWidth(),
                    thickness= 10.dp,
                    progressColor=tintColor,
                    progress = progress / 100f
                )

            }

            Icon(
                painter = painterResource(Res.drawable.arrow_right),
                contentDescription = null,
                tint = tintColor,
                modifier = Modifier.height(24.dp)
            )

        }
    }

}

@Preview
@Composable
private fun GoalCardPreview() {
    VelvetTheme {
        GoalCard(
            goal = GoalsSummaryDomain(
                goalId = "goal_1",
                goalTypes = goalOptionFor(1, "Aarav's Education"),
                title = "Aarav's Education",
                amount = 100_000,
                targetAmount = 1_790_847,
                progressPercent = 6
            ),
            onClick = {}
        )
    }
}
