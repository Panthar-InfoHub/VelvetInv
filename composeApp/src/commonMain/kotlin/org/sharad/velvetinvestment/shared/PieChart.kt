package org.sharad.velvetinvestment.shared

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sharad.velvetinvestment.shared.theme.VelvetTheme

@Composable
fun PieChart(
    data: List<PieChartEntry>,
    modifier: Modifier = Modifier,
    strokeWidth: Float = 84f,
    animationProgress: () -> Float = { 1f }
) {
    val filteredData = remember(data) { data.filter { it.value != 0f } }
    Canvas(
        modifier = modifier
    ) {
        val progress = animationProgress()
        val spaceSize = 2f
        val totalWhiteSpace = spaceSize * filteredData.size

        var startAngle = -140f
        val total = data.map { it.value }.sum()

        drawArc(
            color = Color.Black.copy(alpha = 0.05f),
            startAngle = startAngle,
            sweepAngle = 360f,
            useCenter = false,
            style = Stroke(width = strokeWidth + 20f),
        )

        drawArc(
            color = Color.White,
            startAngle = startAngle,
            sweepAngle = 360f,
            useCenter = false,
            style = Stroke(width = strokeWidth + 12f),
        )

        filteredData.forEach { item ->
            val sweepAngle = (item.value / total) * (360f - totalWhiteSpace) * progress
            drawArc(
                color = Color.White,
                startAngle = startAngle,
                sweepAngle = spaceSize,
                useCenter = false,
                style = Stroke(width = strokeWidth + 12f),
            )

            startAngle += spaceSize

            drawArc(
                color = item.color,
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = false,
                style = Stroke(width = strokeWidth),
            )

            startAngle += sweepAngle
        }
    }
}

data class PieChartEntry(
    val value:Float,
    val color: Color
)

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun PieChartPreview() {
    val pieChartData = listOf(
        PieChartEntry(
            value = 40f,
            color = Color(0xFF4CAF50)
        ),
        PieChartEntry(
            value = 40f,
            color = Color(0xFF2196F3)
        ),
        PieChartEntry(
            value = 20f,
            color = Color(0xFFFF9800)
        ),
        PieChartEntry(
            value = 0f,
            color = Color(0xFFE91E63)
        )
    )
    VelvetTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.White
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                PieChart(
                    data = pieChartData,
                    modifier = Modifier.size(260.dp)
                )
            }
        }
    }
}
