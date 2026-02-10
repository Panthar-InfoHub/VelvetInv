package org.sharad.velvetinvestment.presentation.onboarding.compose.goals

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.sharad.emify.core.ui.theme.Primary
import org.sharad.emify.core.ui.theme.titleColor
import org.sharad.velvetinvestment.presentation.onboarding.models.GoalInfo
import org.sharad.velvetinvestment.utils.DateTimeUtils
import org.sharad.velvetinvestment.utils.formatMoneyWithUnits
import org.sharad.velvetinvestment.utils.genericDropShadow
import org.sharad.velvetinvestment.utils.theme.subHeading
import org.sharad.velvetinvestment.utils.theme.subHeadingMedium
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.icon_cross


@Composable
fun GoalEntry(goalInfo: GoalInfo, onDeleteClick: (GoalInfo) -> Unit) {

    Box(
        modifier=Modifier.fillMaxWidth()
            .height(76.dp)
            .genericDropShadow(RoundedCornerShape(15.dp))
            .clip(RoundedCornerShape(15.dp))
            .background(Color.White, RoundedCornerShape(15.dp)),
        contentAlignment = Alignment.Center
    ){
        Row(
            modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Text(
                modifier = Modifier.weight(1f)
                    .fillMaxWidth(),
                text = goalInfo.name,
                style = subHeading,
                maxLines = 1,
                color = Color.Black,
            )
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "₹${ formatMoneyWithUnits(goalInfo.targetAmount)}" ,
                    style = subHeadingMedium.copy(fontWeight = FontWeight.Bold),
                    color = Primary,
                )
                Text(
                    text = "${(goalInfo.targetYear?: DateTimeUtils.getCurrentYear()) - DateTimeUtils.getCurrentYear()} years to go",
                    style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Medium),
                    color = titleColor,
                )
            }
            Icon(
                painter = painterResource(Res.drawable.icon_cross),
                contentDescription = null,
                modifier=Modifier.size(16.dp)
                    .clickable(
                        onClick = {
                            onDeleteClick(goalInfo)
                        },
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    )
            )
        }
    }
}