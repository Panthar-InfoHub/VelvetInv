package org.sharad.velvetinvestment.shared.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.sharad.velvetinvestment.utils.genericDropShadow

@Composable
fun CircleButton(
    icon:@Composable ()->Unit,
    onClick:()->Unit,
    modifier: Modifier= Modifier.size(54.dp).genericDropShadow().clip(CircleShape).background(Color.White),
    enabled:Boolean=true,
){

    Box(
        modifier=modifier
            .clickable(
                onClick={onClick()}
            ),
        contentAlignment = Alignment.Center
    ){
        icon()
    }

}