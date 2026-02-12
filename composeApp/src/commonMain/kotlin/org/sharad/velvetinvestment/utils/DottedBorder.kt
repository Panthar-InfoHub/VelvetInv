package org.sharad.velvetinvestment.utils

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.dottedBorder(
    color: Color,
    strokeWidth: Dp = 4.dp,
    cornerRadius: Dp = 0.dp,
    on: Float = 12f,
    off: Float = 12f
) = drawBehind {
    val stroke = strokeWidth.toPx()

    drawRoundRect(
        color = color,
        size = size,
        cornerRadius = CornerRadius(cornerRadius.toPx()),
        style = Stroke(
            width = stroke,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(on, off), 0f)
        )
    )
}
