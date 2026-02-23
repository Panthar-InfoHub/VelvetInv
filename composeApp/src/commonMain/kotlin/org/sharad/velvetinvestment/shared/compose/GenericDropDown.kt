package org.sharad.velvetinvestment.shared.compose

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import org.sharad.emify.core.ui.theme.Primary
import org.sharad.emify.core.ui.theme.titleColor
import org.sharad.velvetinvestment.utils.theme.Poppins
import org.sharad.velvetinvestment.utils.theme.subHeadingMedium
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.arrow_down

@Composable
fun GenericDropDownField(
    value: String,
    onValueChange:(String)->Unit,
    placeHolder:String,
    mandatory:Boolean=false,
    label:String,
    modifier: Modifier = Modifier,
    list:List<String>
){
    var extended by remember { mutableStateOf(false) }
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row{
            Text(
                text = label,
                style = subHeadingMedium,
                color = Color.Black
            )
            if (mandatory){
                Text(
                    text = " *",
                    style = subHeadingMedium,
                    color = Color.Red
                )
            }
        }
        Column(
            modifier=Modifier
                .shadow(elevation = 8.dp,RoundedCornerShape(15.dp), spotColor = Color.Black.copy(alpha = 0.4f))
                .clip(RoundedCornerShape(15.dp))
                .background(Color.White, RoundedCornerShape(15.dp))
                .border(
                    width = 0.7.dp,
                    shape = RoundedCornerShape(15.dp),
                    color = Color(0xFFC5A572)
                )
                .animateContentSize()
        ) {
            GenericDropDownHeader(
                value = value,
                placeHolder = placeHolder,
                onClick = {extended=!extended},
                extended =extended
            )
            if (extended){
                GenericDropDownContent(
                    list=list,
                    onSelected={it->
                        onValueChange(it)
                        extended=false
                    }
                )
            }
        }
    }
}

@Composable
fun GenericDropDownContent(onSelected: (String) -> Unit, list: List<String>) {

    Column(
        modifier = Modifier.fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, bottom = 12.dp)
    ) {

        list.forEach { it->
            Text(
                text = it,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                fontFamily = Poppins,
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                    .clickable(
                        onClick = {onSelected(it)}
                    )
            )
        }

    }

}

@Composable
fun GenericDropDownHeader(value:String, placeHolder: String, onClick: () -> Unit, extended: Boolean) {
    val animatedIcon by animateFloatAsState(
        targetValue = if (extended) 180f else 0f,
        label = "arrow")
    Row(
        modifier = Modifier.fillMaxWidth()
            .height(54.dp)
            .clickable(
                onClick={onClick()}
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text= (value.ifEmpty { placeHolder }).capitalize(Locale.current),
            style = subHeadingMedium,
            color = if (value.isBlank()) titleColor.copy(alpha = 0.5f) else Primary,
            modifier = Modifier.weight(1f).padding(start = 16.dp)
        )
        Icon(
            painter = painterResource(Res.drawable.arrow_down),
            contentDescription = null,
            modifier = Modifier.padding(end = 24.dp).rotate(animatedIcon),
            tint = Primary
        )
    }

}