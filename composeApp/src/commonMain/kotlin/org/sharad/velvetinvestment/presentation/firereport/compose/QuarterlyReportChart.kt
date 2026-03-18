package org.sharad.velvetinvestment.presentation.firereport.compose

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.sharad.velvetinvestment.presentation.firereport.viewmodel.SelectedYear
import kotlin.math.max

@Composable
fun LineChart(
    data: List<LineChartData>,
    color: Color = Color(0xff10B981),
    modifier: Modifier = Modifier.height(140.dp),
    progress: Float=1f
) {

    val textMeasurer = rememberTextMeasurer()
    val textStyle = MaterialTheme.typography.labelSmall.copy(fontSize = 12.sp)
    val pathMeasureLine = remember { PathMeasure() }



    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {


        if (data.size < 2) return@Canvas

        val width = size.width
        val height = size.height

        val leftAxisWidth = 12f
        val rightAxisWidth = 12f
        val bottomAxisHeight = 20f
        val topPadding = 24f

        val chartStartX = leftAxisWidth
        val chartEndX = width - rightAxisWidth
        val chartTop = topPadding
        val chartBottom = height - bottomAxisHeight

        val chartWidth = chartEndX - chartStartX
        val chartHeight = chartBottom - chartTop

        val maxNet = data.maxOf { it.value }

        val stepX = chartWidth / (data.size - 1)

        val points = data.mapIndexed { index, item ->
            val x = chartStartX + index * stepX
            val y = chartBottom - (item.value / maxNet).toFloat() * chartHeight
            Offset(x, y)
        }



        /* ---------- STRAIGHT LINE PATH ---------- */

        val linePath = Path().apply {
            moveTo(points.first().x, points.first().y)
            for (i in 1 until points.size) {
                lineTo(points[i].x, points[i].y)
            }
        }

        pathMeasureLine.setPath(linePath, false)

        val animatedPath = Path()
        pathMeasureLine.getSegment(
            0f,
            pathMeasureLine.length * progress,
            animatedPath,
            true
        )

        /* ---------- AREA PATH  ---------- */

//        val areaPath = Path().apply {
//            moveTo(points.first().x, chartBottom)
//
//            points.forEachIndexed { index, point ->
//                if (index == 0) lineTo(point.x, point.y)
//                else lineTo(point.x, point.y)
//            }
//
//            lineTo(points.last().x, chartBottom)
//            close()
//        }

        val areaAnimated = Path()

        pathMeasureLine.getSegment(
            0f,
            pathMeasureLine.length * progress,
            areaAnimated,
            true
        )

        /* ---------- GRADIENT FILL ---------- */

        val visibleX = chartStartX + chartWidth * progress

        areaAnimated.lineTo(visibleX, chartBottom)
        areaAnimated.lineTo(points.first().x, chartBottom)
        areaAnimated.close()
        drawPath(
            path = areaAnimated,
            brush = Brush.verticalGradient(
                colors = listOf(
                    color.copy(alpha = 0.35f),
                    color.copy(alpha = 0.05f),
                    Color.Transparent
                ),
                startY = chartTop,
                endY = chartBottom
            )
        )

        /* ---------- LINE ---------- */

        drawPath(
            animatedPath,
            color,
            style = Stroke(
                width = 3f,
                cap = StrokeCap.Round
            )
        )

        /* ---------- DOTS ---------- */

        val highlightStep = max(1, data.size / 5)


        points.forEachIndexed { index, point ->

            val isHighlight = index % highlightStep == 0
            val radius = if (point.x <= visibleX) {
                if (isHighlight) 10f else 5f
            } else 0f

            if (point.x <= visibleX){
                drawCircle(
                    color = color,
                    radius = radius,
                    center = point
                )

                if (isHighlight) {

                    val label = textMeasurer.measure(
                        AnnotatedString(data[index].floatingLabel),
                        style = textStyle
                    )

                    val labelX = (point.x - label.size.width / 2)
                        .coerceIn(0f, size.width - label.size.width)

                    drawText(
                        label,
                        topLeft = Offset(
                            labelX,
                            point.y - label.size.height - 12f
                        )
                    )
                }
            }
        }

        //BottomLine

        drawLine(
            color = Color.Black.copy(alpha = 0.2f),
            strokeWidth = 2f,
            start = Offset(chartStartX, chartBottom),
            end = Offset(chartEndX, chartBottom)
        )

        /* ---------- START + END Bottom LABEL ---------- */

        val firstLabel = textMeasurer.measure(
            AnnotatedString(data.first().axisLabel),
            style = textStyle
        )

        drawText(
            firstLabel,
            topLeft = Offset(
                chartStartX,
                chartBottom + 6f
            )
        )

        val lastLabel = textMeasurer.measure(
            AnnotatedString(data.last().axisLabel),
            style = textStyle
        )

        drawText(
            lastLabel,
            topLeft = Offset(
                chartEndX - lastLabel.size.width,
                chartBottom + 6f
            )
        )
    }
}

data class LineChartData(
    val floatingLabel: String = "",
    val value: Double,
    val axisLabel: String
)