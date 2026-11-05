package org.sharad.velvetinvestment.shared.compose

import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.arrowback_icon

@Composable
fun AppBackButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    IconButton(
        onClick = onClick,
        modifier = modifier.offset(x = (-12).dp),
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = Color.Transparent,
        )
    ) {
        Icon(
            painter = painterResource(Res.drawable.arrowback_icon),
            contentDescription = "Go Back"
        )
    }
}
