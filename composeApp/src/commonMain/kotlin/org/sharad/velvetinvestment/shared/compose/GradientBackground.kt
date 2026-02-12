package org.sharad.velvetinvestment.shared.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun GradientBackground() {
    Box(
        modifier=Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopEnd
    ){
        Box(
            modifier=Modifier
                .offset(
                    x = 120.dp,
                    y = (-90).dp
                )
                .size(340.dp)
                .clip(CircleShape)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFF91B6FF).copy(alpha = 0.45f),
                            Color.Transparent
                        ),
                    ), CircleShape
                )
                .blur(200.dp)
                .align(Alignment.TopEnd)

        )
    }
}