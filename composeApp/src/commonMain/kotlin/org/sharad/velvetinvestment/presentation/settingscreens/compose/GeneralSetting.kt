package org.sharad.velvetinvestment.presentation.settingscreens.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.sharad.emify.core.ui.theme.blueColor
import org.sharad.emify.core.ui.theme.darkBlue
import org.sharad.emify.core.ui.theme.goldenColor
import org.sharad.emify.core.ui.theme.grayColor
import org.sharad.velvetinvestment.shared.compose.BackHeader
import org.sharad.velvetinvestment.shared.compose.BarHeader
import org.sharad.velvetinvestment.utils.genericDropShadow
import org.sharad.velvetinvestment.utils.theme.Poppins
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.clipboard
import velvet.composeapp.generated.resources.forward_arrow
import velvet.composeapp.generated.resources.key_icon
import velvet.composeapp.generated.resources.lock_icon
import velvet.composeapp.generated.resources.moveforward
import velvet.composeapp.generated.resources.person3_icon
import velvet.composeapp.generated.resources.person_add_icon

@Preview(showSystemUi = true)
@Composable
fun GeneralSetting(){
    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        BackHeader("General Setting",true)
          BarHeader(modifier = Modifier.fillMaxWidth(),"Privacy & Security")
        ChangePin("Change PIN",Res.drawable.key_icon)
        ChangePin("Change Password", icon = Res.drawable.lock_icon)
BarHeader(modifier = Modifier.fillMaxWidth(),"Demat Details")
        DematDetail("Demat Acc Number (BOID)", account = "1234567890123456", icon = Res.drawable.clipboard)
BarHeader(Modifier.fillMaxWidth(),"Nomination")
        ChangePin("My Nominees",Res.drawable.person_add_icon)
Text("Velvet Investing v2.4.1", fontFamily = Poppins, fontWeight = FontWeight.Medium, fontSize = 12.sp, color = grayColor, letterSpacing = 0.14.sp, modifier = Modifier)
    }
}

@Composable
fun ChangePin(text:String,icon: DrawableResource){
    Box(modifier = Modifier.fillMaxWidth().genericDropShadow(RoundedCornerShape(24.dp)).clip(RoundedCornerShape(24.dp)).background(color = Color.White).padding(16.dp)) {
        Row(Modifier.fillMaxWidth(),verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Icon(
                    painter = painterResource(icon),
                    contentDescription = "key icon",
                    tint = darkBlue,
                    modifier = Modifier.background(
                        color = blueColor.copy(0.1f),
                        shape = RoundedCornerShape(10.dp)
                    ).padding(8.dp)
                )
                Text(text, fontFamily = Poppins, fontWeight = FontWeight.Medium, fontSize = 18.sp)
            }
            Icon(
                    painter = painterResource(Res.drawable.moveforward),
                    contentDescription = "forward arrow",
                    tint = goldenColor, modifier = Modifier.height(16.dp)
                )

        }
    }
}



@Composable
fun DematDetail(text:String,account:String,icon: DrawableResource) {
    Box(
        modifier = Modifier.fillMaxWidth().genericDropShadow(RoundedCornerShape(24.dp))
            .clip(RoundedCornerShape(24.dp)).background(color = Color.White).padding(16.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Icon(
                    painter = painterResource(Res.drawable.person3_icon),
                    contentDescription = "key icon",
                    tint = darkBlue,
                    modifier = Modifier.background(
                        color = blueColor.copy(0.1f),
                        shape = RoundedCornerShape(10.dp)
                    ).padding(8.dp).size(24.dp)
                )
                Text(text, fontFamily = Poppins, fontWeight = FontWeight.Medium, fontSize = 18.sp)
            }
            

            Icon(painter = painterResource(icon), contentDescription = "clipboard", tint = goldenColor)
        }
    }
}
