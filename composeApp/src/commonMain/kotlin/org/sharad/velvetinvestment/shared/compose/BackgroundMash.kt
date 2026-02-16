package org.sharad.velvetinvestment.shared.compose

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun MeshSquareBackground(
    modifier: Modifier = Modifier,
    squareSize: Dp = 44.dp,
    lineWidth: Dp = 0.4.dp,
    color: Color = Color.White.copy(0.1f)
) {
    val squarePx = with(LocalDensity.current) { squareSize.toPx() }
    val strokePx = with(LocalDensity.current) { lineWidth.toPx() }

    Canvas(modifier = modifier.fillMaxSize()) {

        val columns = (size.width / squarePx).toInt() + 1
        val rows = (size.height / squarePx).toInt() + 1

        // Vertical lines
        for (i in 0..columns) {
            val x = i * squarePx
            drawLine(
                color = color,
                start = Offset(x, 0f),
                end = Offset(x, size.height),
                strokeWidth = strokePx
            )
        }

        // Horizontal lines
        for (j in 0..rows) {
            val y = j * squarePx
            drawLine(
                color = color,
                start = Offset(0f, y),
                end = Offset(size.width, y),
                strokeWidth = strokePx
            )
        }
    }
}
