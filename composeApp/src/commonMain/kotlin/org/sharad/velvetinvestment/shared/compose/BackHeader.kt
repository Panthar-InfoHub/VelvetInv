package org.sharad.velvetinvestment.shared.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.sharad.emify.core.ui.theme.Primary
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.arrow_down
import velvet.composeapp.generated.resources.back_arrow

@Composable
fun BackHeader(
    heading: String,
    showBack: Boolean = false,
    onBackClick: () -> Unit={}
){
    Box(
        modifier=Modifier.fillMaxWidth().padding(vertical = 16.dp, horizontal = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = heading,
            style = MaterialTheme.typography.headlineLarge,
            color = Primary
        )

        if (showBack){
            Icon(
                painter = painterResource(Res.drawable.back_arrow),
                contentDescription = null,
                modifier = Modifier.size(22.dp).clickable(
                    onClick = onBackClick,
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ).align(Alignment.CenterStart)
            )
        }
    }
}