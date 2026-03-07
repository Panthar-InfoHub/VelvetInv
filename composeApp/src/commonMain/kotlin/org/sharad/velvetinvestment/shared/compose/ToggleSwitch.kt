package org.sharad.velvetinvestment.shared.compose

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import org.sharad.velvetinvestment.utils.genericDropShadow

@Composable
fun ToggleSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    width: Dp = 34.dp,
    height: Dp = 20.dp,
    thumbSize: Dp = 16.dp,
    checkedTrackColor: Color = Primary,
    uncheckedTrackColor: Color = Color(0xFFD9D9D9),
    checkedThumbColor: Color = Color.White,
    uncheckedThumbColor: Color = Primary
)  {


    val thumbOffset by animateDpAsState(
        targetValue = if (checked) width - thumbSize - 2.5.dp else 2.5.dp,
        label = "thumbOffset"
    )

    Box(
        modifier = Modifier
            .width(width)
            .height(height)
            .genericDropShadow(shape = CircleShape)
            .clip(CircleShape)
            .background(if (checked) checkedTrackColor else uncheckedTrackColor)
            .border(0.5.dp, Primary, CircleShape)
            .clickable { onCheckedChange(!checked) },
        contentAlignment = Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .size(thumbSize)
                .offset(x = thumbOffset)
                .clip(CircleShape)
                .background(if (checked) checkedThumbColor else uncheckedThumbColor)
        )
    }
}