package org.sharad.velvetinvestment.presentation.firereport.compose

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.sharad.velvetinvestment.presentation.firereport.uimodels.QuarterlyTrendUI
import org.sharad.velvetinvestment.utils.formatMoneyWithUnits
import org.sharad.velvetinvestment.utils.theme.Poppins

@Composable
fun QuarterlyTrendChart(
    data: List<QuarterlyTrendUI>,
    modifier: Modifier = Modifier.height(280.dp)
) {

    val textMeasurer = rememberTextMeasurer()
    val textStyle= MaterialTheme.typography.labelSmall.copy(fontSize = 12.sp)

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {

        if (data.size < 2) return@Canvas

        val width = size.width
        val height = size.height

        val leftAxisWidth = 112f
        val rightAxisWidth = 112f
        val bottomAxisHeight = 40f
        val topPadding = 20f

        val chartStartX = leftAxisWidth
        val chartEndX = width - rightAxisWidth
        val chartTop = topPadding
        val chartBottom = height - bottomAxisHeight

        val chartWidth = chartEndX - chartStartX
        val chartHeight = chartBottom - chartTop

        val maxNet = data.maxOf { it.netWorth }
        val maxFire = data.maxOf { it.fireProgress }

        val stepX = chartWidth / ((data.size - 1)/2)

        val netPoints = data.mapIndexed { index, item ->
            val x = chartStartX + index * stepX
            val y = chartBottom -
                    (item.netWorth / maxNet).toFloat() * chartHeight
            Offset(x, y)
        }

        val firePoints = data.mapIndexed { index, item ->
            val x = chartStartX + index * stepX
            val y = chartBottom -
                    (item.fireProgress / maxFire).toFloat() * chartHeight
            Offset(x, y)
        }

        // AXIS
        drawLine(
            Color.Gray,
            Offset(chartStartX, chartTop),
            Offset(chartStartX, chartBottom),
            2f
        )

        drawLine(
            Color.Gray,
            Offset(chartEndX, chartTop),
            Offset(chartEndX, chartBottom),
            2f
        )

        drawLine(
            Color.Gray,
            Offset(chartStartX, chartBottom),
            Offset(chartEndX, chartBottom),
            2f
        )

        // SMOOTH PATH
        fun smoothPath(points: List<Offset>): Path {

            val path = Path()

            path.moveTo(points.first().x, points.first().y)

            for (i in 1 until points.size) {

                val prev = points[i - 1]
                val cur = points[i]

                val controlX = (prev.x + cur.x) / 2

                path.cubicTo(
                    controlX, prev.y,
                    controlX, cur.y,
                    cur.x, cur.y
                )
            }

            return path
        }

        val netPath = smoothPath(netPoints)
        val firePath = smoothPath(firePoints)

        drawPath(
            netPath,
            Color(0xFF243A6B),
            style = Stroke(4f)
        )

        drawPath(
            firePath,
            Color(0xFFD6B585),
            style = Stroke(4f)
        )

        // DOTS
        netPoints.forEach {
            drawCircle(Color(0xFF243A6B), 6f, it)
        }

        firePoints.forEach {
            drawCircle(Color(0xFFD6B585), 6f, it)
        }

        // Y AXIS TICKS (5)
        val ticks = 5

        repeat(ticks) { i ->

            val fraction = i / (ticks - 1f)

            val y = chartBottom - fraction * chartHeight

            val netValue = maxNet * fraction
            val fireValue = maxFire * fraction

            val netLabel = formatMoneyWithUnits(netValue.toLong())
            val fireLabel = "${fireValue.toInt()}%"

            val netText = textMeasurer.measure(
                AnnotatedString(netLabel+"-"),
                        style = textStyle
            )

            val fireText = textMeasurer.measure(
                AnnotatedString("-"+fireLabel),
                style = textStyle
            )

            drawText(
                netText,
                topLeft = Offset(
                    chartStartX - netText.size.width - 8,
                    y - netText.size.height / 2
                )
            )

            drawText(
                fireText,
                topLeft = Offset(
                    chartEndX + 8,
                    y - fireText.size.height / 2
                )
            )
        }

        // X AXIS LABELS
        data.forEachIndexed { index, item ->

            val x = chartStartX + index * stepX

            val text = textMeasurer.measure(
                AnnotatedString(item.quarter),
            )

            drawText(
                text,
                topLeft = Offset(
                    x - text.size.width / 2,
                    chartBottom + 10
                )
            )
        }
    }
}