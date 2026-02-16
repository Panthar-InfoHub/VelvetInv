package org.sharad.velvetinvestment.shared.compose

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.sharad.emify.core.ui.theme.appRed
import org.sharad.velvetinvestment.utils.theme.Poppins


@Composable
fun DotCapCircularProgress(
    percentage: Float,
    modifier: Modifier = Modifier,
    strokeWidth: Dp = 4.dp,
    color: Color = when(percentage){
        in 0f..30f -> appRed
        in 30f..70f -> Color.Yellow
        else -> Color.Green
    },
    trackColor: Color = Color(0xffD9D9D9),
    textColor: Color = when(percentage){
        in 0f..30f -> appRed
        in 30f..70f -> Color.Yellow
        else -> Color.Green
    }
) {
    Box(modifier=modifier,
        contentAlignment = Alignment.Center){
        CircularProgressIndicator(
            progress = { percentage / 100f },
            modifier = modifier,
            strokeWidth = strokeWidth,
            color = color,
            trackColor = trackColor,
            strokeCap = Paint().strokeCap,
        )
        Text(
            text="${percentage.toInt().coerceIn(0,100)}%",
            color = textColor,
            fontFamily = Poppins,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
