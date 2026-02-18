package org.sharad.velvetinvestment.shared

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun PieChart(
    data:List<PieChartEntry>,
    modifier: Modifier=Modifier
){

    Canvas(
        modifier= modifier
    ){

        val strokeWidth = 88f
        val spaceSize=2f
        val totalWhiteSpace= spaceSize*data.size

        var startAngle= -140f
        val total= data.map { it.value }.sum()

        drawArc(
            color = Color.Black.copy(alpha = 0.05f),
            startAngle = startAngle,
            sweepAngle = 360f,
            useCenter = false,
            style = Stroke(width = strokeWidth+20f),
        )

        drawArc(
            color = Color.White,
            startAngle = startAngle,
            sweepAngle = 360f,
            useCenter = false,
            style = Stroke(width = strokeWidth + 12f),
        )

        data.forEach{item->
            val sweepAngle= (item.value/total) * (360f-totalWhiteSpace)
            drawArc(
                color = Color.White,
                startAngle = startAngle,
                sweepAngle = spaceSize,
                useCenter = false,
                style = Stroke(width = strokeWidth + 12f),
            )

            startAngle=startAngle+spaceSize


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
