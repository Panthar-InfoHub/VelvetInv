package org.sharad.velvetinvestment.shared.compose

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.sharad.emify.core.ui.theme.Primary
import org.sharad.velvetinvestment.utils.theme.Poppins
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun PercentageCircle(
    percentage: Float,
    modifier: Modifier = Modifier,
    strokeWidth: Dp = 14.dp,
    progressColor: Color = Color(0xFFC8A46B),
    backgroundColor: Color = Color(0xffD9D9D9)
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {

        Canvas(modifier = Modifier.fillMaxSize()) {

            val stroke = strokeWidth.toPx()
            val radius = size.minDimension / 2
            val arcRadius = radius - stroke / 2

            drawArc(
                color = backgroundColor,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(
                    width = stroke,
                    cap = StrokeCap.Round
                )
            )

            val sweepAngle = 360f * percentage

            drawArc(
                color = progressColor,
                startAngle = -90f,
                sweepAngle = sweepAngle,
                useCenter = false,
                style = Stroke(
                    width = stroke,
                    cap = StrokeCap.Round
                )
            )

            val angleRad = (sweepAngle - 90f) * (PI / 180f)


            val dotRadius = stroke * 1.1f
            val extraOffset = dotRadius - (stroke / 2f)

            val adjustedRadius = arcRadius + extraOffset

            val x = center.x + adjustedRadius * cos(angleRad).toFloat()
            val y = center.y + adjustedRadius * sin(angleRad).toFloat()

            drawCircle(
                color = progressColor,
                radius = stroke * 1.1f,
                center = Offset(x, y)
            )
            drawCircle(
                color = Color.White,
                radius = stroke * 0.5f,
                center = Offset(x, y)
            )

        }

        Text(
            text = "${(percentage * 100).toDouble()} %",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = Poppins,
            color = Primary
        )
    }
}