package org.sharad.velvetinvestment.shared.compose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.unit.DpOffset
import org.jetbrains.compose.resources.DrawableResource
import org.sharad.emify.core.ui.theme.ButtonShadow
import org.sharad.emify.core.ui.theme.Primary
import org.sharad.velvetinvestment.shared.theme.LocalVelvetShapes
import org.sharad.velvetinvestment.shared.theme.Spacing

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun InvertedAppButton(
    text: String,
    onClick: () -> Unit,
    style: AppButtonStyle = AppButtonDefaults.style(
        containerColor = White,
        contentColor = Primary
    ),
    enabled: Boolean = true,
    loading: Boolean = false,
    trailingIcon: DrawableResource? = null,
    modifier: Modifier = Modifier
) {

    OutlinedButton(
        onClick = onClick,
        enabled = enabled && !loading,
        shape = style.shape,
        border = BorderStroke(
            width = Spacing.dp1,
            color = style.contentColor
        ),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = style.containerColor,
            contentColor = style.contentColor,
            disabledContainerColor = style.containerColor,
            disabledContentColor = style.contentColor.copy(alpha = 0.5f)
        ),
        modifier = modifier
            .height(style.height)
            .dropShadow(
                shape = LocalVelvetShapes.current.roundedDp12,
                shadow = Shadow(
                    radius = Spacing.dp16,
                    spread = Spacing.dp12,
                    offset = DpOffset(
                        x = Spacing.dp0,
                        y = Spacing.dp8
                    ),
                    color = ButtonShadow
                )
            )
    ) {
        AppButtonContent(
            text = text,
            loading = loading,
            indicatorColor = style.contentColor,
            indicatorSize = style.indicatorSize,
            trailingIcon = trailingIcon
        )
    }
}