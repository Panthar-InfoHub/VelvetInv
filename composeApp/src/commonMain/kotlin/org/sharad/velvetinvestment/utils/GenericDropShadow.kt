package org.sharad.velvetinvestment.utils

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow

import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.unit.dp
import org.sharad.emify.core.ui.theme.shadowColor

fun Modifier.genericDropShadow(
    shape: Shape
): Modifier = this.then(
    Modifier.dropShadow(
        shadow = Shadow(
            radius = 24.dp,
            color = shadowColor
        ),
        shape = shape
    )
)
