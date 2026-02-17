package org.sharad.velvetinvestment.shared.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.sharad.emify.core.ui.theme.Primary
import org.sharad.emify.core.ui.theme.Secondary
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.app_logo_transparent
import velvet.composeapp.generated.resources.icon_mf

@Composable
fun LoaderScreen(){
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Box(
            contentAlignment = Alignment.Center
        ){

            CircularProgressIndicator(
                modifier = Modifier.size(96.dp).graphicsLayer{scaleX = -1f},
                color = Secondary,
                strokeWidth = 4.dp
            )
            CircularProgressIndicator(
                modifier = Modifier.size(112.dp),
                color = Primary,
                strokeWidth = 4.dp
            )

            Box(
                modifier = Modifier.size(80.dp)
                    .clip(CircleShape)
                    .background(Primary),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(Res.drawable.app_logo_transparent),
                    contentDescription = null,
                    modifier = Modifier.size(64.dp)
                )
            }
        }

    }
}