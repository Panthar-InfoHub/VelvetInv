package org.sharad.velvetinvestment.shared.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.sharad.emify.core.ui.theme.Primary
import org.sharad.emify.core.ui.theme.titleColor
import org.sharad.velvetinvestment.shared.theme.Poppins

/** Checkbox + consent line. Moved here from the removed old-KYC `KycContractScreen`. */
@Composable
fun ConsentSection(
    modifier: Modifier= Modifier,
    isChecked: Boolean,
    onToggle: () -> Unit,
    text:String = "I have read and understood the KYC document and confirm that the provided details are accurate.",

    ) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onToggle() },
        verticalAlignment = Alignment.Top
    ) {

        Box(
            modifier = Modifier
                .size(22.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(
                    if (isChecked) Primary else Color.Transparent
                )
                .border(
                    width = 1.dp,
                    color = if (isChecked) Primary else titleColor.copy(0.5f),
                    shape = RoundedCornerShape(6.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isChecked) {
                Text(
                    text = "✓",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(Modifier.width(12.dp))

        Text(
            text = text,
            fontSize = 14.sp,
            fontFamily = Poppins,
            fontWeight = FontWeight.Normal,
            color = titleColor,
            lineHeight = 20.sp
        )
    }
}
