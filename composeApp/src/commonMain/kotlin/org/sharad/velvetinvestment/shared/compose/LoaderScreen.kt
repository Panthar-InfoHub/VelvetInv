package org.sharad.velvetinvestment.shared.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.sharad.emify.core.ui.theme.Primary
import org.sharad.emify.core.ui.theme.Secondary
import org.sharad.velvetinvestment.utils.loadingQuotes
import org.sharad.velvetinvestment.utils.theme.subHeading
import org.sharad.velvetinvestment.utils.theme.titlesStyle
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.app_logo_transparent
import velvet.composeapp.generated.resources.icon_mf

@Composable
fun LoaderScreen(){

    var quote by remember{mutableStateOf(loadingQuotes.random())}

    Box(
        modifier = Modifier.fillMaxSize()
            .clickable(
                onClick = {
                    quote=loadingQuotes.random()
                },
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ),
        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ){
            Box(
                contentAlignment = Alignment.Center
            )
            {

                CircularProgressIndicator(
                    modifier = Modifier.size(92.dp).graphicsLayer { scaleX = -1f },
                    color = Secondary,
                    strokeWidth = 4.dp
                )
                CircularProgressIndicator(
                    modifier = Modifier.size(108.dp),
                    color = Primary,
                    strokeWidth = 4.dp
                )

                Box(
                    modifier = Modifier.size(72.dp)
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

            Text(
                text = quote,
                color = Primary,
                style = titlesStyle,
                modifier = Modifier.fillMaxWidth().padding(24.dp),
                textAlign = TextAlign.Center
            )
        }

    }
}