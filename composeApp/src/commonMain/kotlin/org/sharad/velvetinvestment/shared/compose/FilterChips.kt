package org.sharad.velvetinvestment.shared.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.sharad.emify.core.ui.theme.Primary
import org.sharad.velvetinvestment.utils.theme.titlesStyle

@Composable
fun FilterChip(
    title:String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val background =
        if (isSelected) Primary
        else Color(0xFFDEE2F6).copy(0.7f)

    val contentColor =
        if (isSelected) Color.White
        else Color.Black

    Box(
        modifier = Modifier
            .height(32.dp)
            .clip(RoundedCornerShape(50))
            .background(background)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            color = contentColor,
            style = titlesStyle,
            lineHeight = 14.sp,
            modifier = Modifier.padding(horizontal = 12.dp)
        )
    }
}
