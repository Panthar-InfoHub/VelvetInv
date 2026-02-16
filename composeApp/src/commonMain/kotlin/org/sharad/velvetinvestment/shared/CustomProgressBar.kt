package org.sharad.velvetinvestment.shared

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.sharad.emify.core.ui.theme.Primary

@Composable
fun CustomProgressFillBar(
    modifier: Modifier = Modifier,
    thickness: Dp = 12.dp,
    trackColor: Color = Color(0xffD9D9D9),
    progressColor: Color = Primary,
    progress: Float = 0.0f
) {

    val animatedProgress by animateFloatAsState(targetValue = progress, label = "Progress")

    Box(
        modifier=modifier.height(thickness)
            .clip(CircleShape)
            .background(trackColor),
        contentAlignment = Alignment.CenterStart
    ){
        Box(
            modifier = Modifier
                .fillMaxWidth(animatedProgress)
                .fillMaxHeight()
                .clip(CircleShape)
                .background(progressColor)
        )
    }

}