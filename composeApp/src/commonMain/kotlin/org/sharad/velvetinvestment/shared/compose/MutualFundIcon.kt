package org.sharad.velvetinvestment.shared.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.sharad.emify.core.ui.theme.Primary

/**
 * A fund's two-letter monogram. Moved out of the old `presentation/mutualfund` details screen
 * (now in `composeApp/later/`) because portfolio and the shared fund cards still use it.
 */
@Composable
fun MutualFundIcon(
    schemeName: String,
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
    cornerRadius: Dp = 12.dp,
    backgroundColor: Color = Primary,
    textColor: Color = Color.White
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(cornerRadius))
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = schemeName
                .take(2).capitalize(Locale.current),
            style = MaterialTheme.typography.headlineSmall,
            color = textColor
        )
    }
}
