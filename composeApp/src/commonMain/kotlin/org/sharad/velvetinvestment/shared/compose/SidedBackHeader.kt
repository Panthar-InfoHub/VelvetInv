package org.sharad.velvetinvestment.shared.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import org.sharad.emify.core.ui.theme.Secondary
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.back_arrow

@Composable
fun SidedBackHeader(
    heading: String,
    showBack: Boolean = false,
    onBackClick: () -> Unit={},
    textIcon:String?=null,
    onTextClick:()->Unit={}
){
    Row(
        modifier=Modifier.fillMaxWidth().padding(vertical = 16.dp, horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (showBack){
            Icon(
                painter = painterResource(Res.drawable.back_arrow),
                contentDescription = null,
                modifier = Modifier.size(20.dp).clickable(
                    onClick = onBackClick,
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                )
            )
        }
        Text(
            text = heading,
            style = MaterialTheme.typography.headlineLarge,
            color = Primary,
            modifier = Modifier.weight(1f)
        )
        textIcon?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.labelSmall,
                color = Secondary,
                modifier = Modifier.clickable(
                    onClick = onTextClick,
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                )
            )
        }

    }
}