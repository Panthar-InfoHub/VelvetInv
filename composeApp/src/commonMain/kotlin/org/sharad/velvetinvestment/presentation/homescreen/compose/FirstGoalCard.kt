package org.sharad.velvetinvestment.presentation.homescreen.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import org.sharad.emify.core.ui.theme.appYellow
import org.sharad.velvetinvestment.utils.genericDropShadow
import org.sharad.velvetinvestment.utils.theme.titlesStyle
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.arrow_right
import velvet.composeapp.generated.resources.goal_icon

@Composable
fun FirstGoalCard(onClick: () -> Unit) {
    Row(
        modifier=Modifier.fillMaxWidth()
            .height(80.dp)
            .genericDropShadow()
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White,RoundedCornerShape(12.dp))
            .clickable(onClick=onClick),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.fillMaxHeight(0.8f)
                .width(2.dp)
                .clip(
                    RoundedCornerShape(
                        topStartPercent = 50,
                        bottomStartPercent = 50,
                        topEndPercent = 20,
                        bottomEndPercent = 20
                    )
                )
                .background(appYellow)

        )

        Box(modifier=Modifier.size(44.dp).clip(RoundedCornerShape(10.dp))
            .background(appYellow.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ){
            Icon(
                painter = painterResource(Res.drawable.goal_icon),
                contentDescription = null,
                tint = appYellow,
                modifier = Modifier.width(24.dp)
            )
        }

        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)){
            Text(
                text = "Set Your First Goal",
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 15.sp, fontWeight = FontWeight.SemiBold),
                color = Color.Black,

            )
            Text(
                text="Dreaming for a car or Home?",
                style = titlesStyle,
                color = Color.Black
            )
        }


        Icon(
            painter = painterResource(Res.drawable.arrow_right),
            contentDescription = null,
            tint = appYellow,
            modifier = Modifier.padding(end = 16.dp).height(24.dp)
        )

    }
}