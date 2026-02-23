package org.sharad.velvetinvestment.shared.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.sharad.emify.core.ui.theme.Primary
import org.sharad.emify.core.ui.theme.Secondary
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.icon_arrow_right

@Composable
fun BarHeader(
    heading:String,
    modifier: Modifier=Modifier,
    showArrow:Boolean=false,
    onArrowClick:()->Unit={}
){
    Column(
        modifier=modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Box(
                modifier = Modifier.fillMaxHeight()
                    .width(5.dp)
                    .clip(CircleShape)
                    .background(Secondary, CircleShape)
            )

            Text(
                text = heading,
                style = MaterialTheme.typography.headlineSmall,
                color = Primary,
                modifier = Modifier.weight(1f)
                    .fillMaxWidth()
            )

            if (showArrow){
                Icon(
                    painter = painterResource(Res.drawable.icon_arrow_right),
                    contentDescription = null,
                    tint = Primary,
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .size(16.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = { onArrowClick() }
                        )
                )
            }

        }
    }
}