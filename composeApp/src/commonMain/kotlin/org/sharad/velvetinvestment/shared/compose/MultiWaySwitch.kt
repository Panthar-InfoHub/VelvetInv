package org.sharad.velvetinvestment.shared.compose

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sharad.emify.core.ui.theme.Primary
import org.sharad.velvetinvestment.shared.theme.VelvetTheme
import org.sharad.velvetinvestment.shared.theme.titlesStyle

/**
 * [TwoWaySwitch]'s look for any number of options: a grey track with a white thumb that slides to
 * the selected segment. Used where three purchase modes (daily, monthly, one-time) are
 * offered.
 */
@Composable
fun <T> MultiWaySwitch(
    options: List<T>,
    selected: T,
    label: (T) -> String,
    onSelected: (T) -> Unit,
    modifier: Modifier = Modifier
) {
    if (options.isEmpty()) return

    val selectedIndex = options.indexOf(selected).coerceAtLeast(0)
    var parentWidth by remember { mutableStateOf(0) }
    val segmentFraction = 1f / options.size

    val offsetPx by animateFloatAsState(
        targetValue = parentWidth * segmentFraction * selectedIndex,
        animationSpec = tween(250),
        label = "multiWaySwitchThumb"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(44.dp)
            .onSizeChanged { parentWidth = it.width }
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xffF3F4F6))
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(segmentFraction)
                .graphicsLayer { translationX = offsetPx }
                .padding(4.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.White)
        )

        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            options.forEachIndexed { index, option ->
                val color by animateColorAsState(
                    targetValue = if (index == selectedIndex) Primary else Primary.copy(alpha = 0.5f),
                    animationSpec = tween(250),
                    label = "multiWaySwitchText"
                )
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) { onSelected(option) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = label(option),
                        style = titlesStyle,
                        color = color,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun MultiWaySwitchPreview() {
    VelvetTheme {
        Column(Modifier.background(Color.White).padding(16.dp)) {
            MultiWaySwitch(
                options = listOf("Daily", "Monthly", "One-time"),
                selected = "Daily",
                label = { it },
                onSelected = {}
            )
        }
    }
}
