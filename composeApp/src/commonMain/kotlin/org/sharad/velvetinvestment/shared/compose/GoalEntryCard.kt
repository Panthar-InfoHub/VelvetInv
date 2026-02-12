package org.sharad.velvetinvestment.shared.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.sharad.emify.core.ui.theme.Primary
import org.sharad.velvetinvestment.domain.GoalTypes
import org.sharad.velvetinvestment.domain.models.home.GoalsSummaryDomain
import org.sharad.velvetinvestment.domain.models.home.progressPercent
import org.sharad.velvetinvestment.shared.CustomProgressFillBar
import org.sharad.velvetinvestment.utils.formatMoneyWithUnits
import org.sharad.velvetinvestment.utils.genericDropShadow
import org.sharad.velvetinvestment.utils.theme.subHeading
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.arrow_right
import velvet.composeapp.generated.resources.education_icon
import velvet.composeapp.generated.resources.icon_callender
import velvet.composeapp.generated.resources.plus_icon
import velvet.composeapp.generated.resources.ring_icon
import velvet.composeapp.generated.resources.ruppee_circle

@Composable
fun GoalEntryCard(goal: GoalsSummaryDomain, onClick: () -> Unit) {

    val tintColor=goal.goalTypes.color
    val icon=when(goal.goalTypes){
        GoalTypes.WEALTH_BUILDING -> {
            Res.drawable.ruppee_circle
        }
        GoalTypes.RETIREMENT -> {Res.drawable.icon_callender}
        GoalTypes.EDUCATION -> {Res.drawable.education_icon}
        GoalTypes.MARRIAGE -> { Res.drawable.ring_icon }
        GoalTypes.CUSTOM_GOAL -> { Res.drawable.plus_icon }
    }


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
                           text=goal.goalTypes.displayName,
                           style = subHeading,
                           color = tintColor
                       )
                       Text(
                           text="₹${goal.amount}/${formatMoneyWithUnits(goal.targetAmount)}",
                           style = MaterialTheme.typography.labelSmall,
                           color = Color.Black
                       )
                   }

                    Text(
                        text= "${goal.progressPercent()}%",
                        style = MaterialTheme.typography.labelSmall,
                        color = tintColor,
                        modifier=Modifier.align(Alignment.Bottom)
                    )

                }

                CustomProgressFillBar(
                    modifier = Modifier.fillMaxWidth(),
                    thickness= 14.dp,
                    progressColor=tintColor,
                    progress = goal.progressPercent() / 100f
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

