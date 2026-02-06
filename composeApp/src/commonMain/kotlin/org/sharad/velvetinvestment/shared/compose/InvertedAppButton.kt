package org.sharad.velvetinvestment.shared.compose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.sharad.emify.core.ui.theme.Primary
import org.sharad.emify.core.ui.theme.Secondary
import org.sharad.velvetinvestment.utils.theme.buttonTextStyle

@Composable
fun InvertedAppButton(
    modifier: Modifier=Modifier,
    text:String,
    onClick:()->Unit,
    enabled: Boolean=true
){
    Button(
        onClick=onClick,
        modifier=modifier.fillMaxWidth().height(50.dp),
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = Primary,
            disabledContentColor = Primary.copy(alpha = 0.5f) ,
            disabledContainerColor = Color.Black.copy(alpha = 0.05f)
        ),
        border = BorderStroke(width = 1.5.dp, color = Secondary)
    ){
        Text(
            text = text,
            style = buttonTextStyle
        )
    }
}