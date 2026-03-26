package org.sharad.velvetinvestment.shared.compose

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import org.sharad.emify.core.ui.theme.Secondary
import org.sharad.velvetinvestment.domain.models.mutualfunds.MutualFundGraphPointsDomain
import org.sharad.velvetinvestment.utils.theme.titlesStyle

@Composable
fun NavLineChart(
    data: List<MutualFundGraphPointsDomain>,
    color: Color = Secondary,
    modifier: Modifier = Modifier.height(140.dp),
    progress: Float = 1f
) {
    var touchX by remember { mutableStateOf<Float?>(null) }
    val alpha by animateFloatAsState(if (touchX != null) 1f else 0f)
    val pathMeasureLine = remember { PathMeasure() }
    val textMeasurer = rememberTextMeasurer()
    val textStyle=titlesStyle

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { offset -> touchX = offset.x },
                    onDrag = { change, _ -> touchX = change.position.x },
                    onDragEnd = { touchX = null },
                    onDragCancel = { touchX = null }
                )
            }
    ) {


        if (data.size < 2) return@Canvas

        val width = size.width
        val height = size.height

        val horizontalPadding = 12f
        val verticalPadding = 80f

        val chartStartX = horizontalPadding
        val chartEndX = width - horizontalPadding
        val chartTop = verticalPadding
        val chartBottom = height - verticalPadding

        val chartWidth = chartEndX - chartStartX
        val chartHeight = chartBottom - chartTop

        val maxNav = data.maxOf { it.navValue }
        val minNav = data.minOf { it.navValue }

        val stepX = chartWidth / (data.size - 1)

        val points = data.mapIndexed { index, item ->
            val x = chartStartX + index * stepX

            val normalized =
                if (maxNav == minNav) 0.5f
                else ((item.navValue - minNav) / (maxNav - minNav)).toFloat()

            val y = chartBottom - normalized * chartHeight

            Offset(x, y)
        }

        val activePoint = touchX?.let { tx ->
            points.minByOrNull { kotlin.math.abs(it.x - tx) }
        }

        val activeIndex = activePoint?.let { points.indexOf(it) }

        /* ---------- LINE PATH ---------- */

//        val linePath = Path().apply {
//            moveTo(points.first().x, points.first().y)
//            for (i in 1 until points.size) {
//                lineTo(points[i].x, points[i].y)
//            }
//        }

        val linePath = Path().apply {
            if (points.isEmpty()) return@apply

            moveTo(points.first().x, points.first().y)

            if (points.size == 1) return@apply

            // Catmull-Rom to Cubic Bezier conversion
            for (i in 0 until points.size - 1) {
                val p0 = points[(i - 1).coerceAtLeast(0)]
                val p1 = points[i]
                val p2 = points[i + 1]
                val p3 = points[(i + 2).coerceAtMost(points.size - 1)]

                // Control points derived from Catmull-Rom tension (0.3f = subtle smoothing)
                val tension = 0.3f

                val cp1x = p1.x + (p2.x - p0.x) * tension
                val cp1y = p1.y + (p2.y - p0.y) * tension

                val cp2x = p2.x - (p3.x - p1.x) * tension
                val cp2y = p2.y - (p3.y - p1.y) * tension

                cubicTo(cp1x, cp1y, cp2x, cp2y, p2.x, p2.y)
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

        /* ---------- AREA FILL ---------- */

        val areaPath = Path()
        pathMeasureLine.getSegment(
            0f,
            pathMeasureLine.length * progress,
            areaPath,
            true
        )

        val visibleX = chartStartX + chartWidth * progress

        areaPath.lineTo(visibleX, chartBottom)
        areaPath.lineTo(points.first().x, chartBottom)
        areaPath.close()

        drawPath(
            path = areaPath,
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
                width = 5f,
                cap = StrokeCap.Round
            )
        )

        /*-------------Label Line & Text ------------------*/

        val topLinePadding= textStyle.fontSize.toPx() +12f

        activePoint?.let { point ->
            drawLine(
                color = color.copy(alpha = 0.6f),
                start = Offset(point.x, topLinePadding),
                end = Offset(point.x, size.height),
                strokeWidth = 2f,
                alpha=alpha
            )
        }


        activeIndex?.let { index ->
            val labelText = data[index].label

            val textLayout = textMeasurer.measure(
                AnnotatedString(labelText),
                style = textStyle
            )


            var labelX = points[index].x - textLayout.size.width / 2


            labelX = labelX.coerceIn(
                0f,
                size.width - textLayout.size.width
            )

            val labelY = 0f

            drawText(
                textLayout,
                topLeft = Offset(labelX, labelY),
                alpha = alpha
            )
        }
    }
}
