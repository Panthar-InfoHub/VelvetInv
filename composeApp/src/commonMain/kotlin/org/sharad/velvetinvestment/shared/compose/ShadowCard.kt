package org.sharad.velvetinvestment.shared.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import org.sharad.velvetinvestment.utils.genericDropShadow

@Composable
fun ShadowCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(15.dp),
    backgroundColor: Color = Color.White,
    onClick: () -> Unit = {},
    clickable: Boolean = false,
    contentAlignment: Alignment = Alignment.Center,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .genericDropShadow(shape)
            .clip(shape)
            .background(backgroundColor)
            .then(
                if (clickable) Modifier.clickable(onClick = onClick)
                else Modifier
            ),
        contentAlignment = contentAlignment,
        content = content
    )
}
